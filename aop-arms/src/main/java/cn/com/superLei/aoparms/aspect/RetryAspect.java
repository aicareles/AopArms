package cn.com.superLei.aoparms.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

import cn.com.superLei.aoparms.annotation.Retry;
import cn.com.superLei.aoparms.common.reflect.Reflect;
import cn.com.superLei.aoparms.common.reflect.ReflectException;
import cn.com.superLei.aoparms.common.utils.Preconditions;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Aspect
public class RetryAspect {

    private static final String TAG = "RetryAspect";
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Retry * *(..))";

    private int retryCount = 0;//当前出错重试次数

    @Pointcut(POINTCUT_METHOD)
    public void onRetryMethod() {
    }

    @Around("onRetryMethod() && @annotation(retry)")
    public Object doRetryMethod(final ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        final int count = retry.count();
        final long delay = retry.delay();
        boolean asyn = retry.asyn();
        final String retryCallback = retry.retryCallback();
        String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();
        if (!"boolean".equalsIgnoreCase(type)){
            Log.e(TAG, "方法返回值必须是boolean类型");
            return joinPoint.proceed();
        }
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Boolean value = false;
                try {
                    value = (Boolean) joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                if (value){
                    Log.e(TAG, "任务请求成功,当前重试次数>>>>"+count);
                    doRetryResult(joinPoint, retryCallback, true);
                    retryCount = 0;
                }else {
                    throw new Exception("任务请求失败,准备重试...");
                }
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable
                        .flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (++retryCount <= count) {
                                    Log.e(TAG, "An error occurred, ready to try again, retries count>>>>>" + retryCount);
                                    // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                    return Observable.timer(delay, TimeUnit.MILLISECONDS);
                                }
                                return Observable.error(throwable);
                            }
                        });
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(asyn ? Schedulers.io() : AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.e(TAG, "accept: >>>>>" + aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "throwable: >>>>>" + throwable.getMessage());
                        doRetryResult(joinPoint, retryCallback, false);
                        retryCount = 0;
                    }
                });
        return null;
    }

    private void doRetryResult(ProceedingJoinPoint joinPoint, String retryCallback, boolean result){
        if (Preconditions.isNotBlank(retryCallback)) {
            try {
                Reflect.on(joinPoint.getTarget()).callback(retryCallback, result);
            } catch (ReflectException exception) {
                exception.printStackTrace();
                Log.e(TAG, "no method "+retryCallback);
            }
        }
    }
}
