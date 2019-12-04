package cn.com.superLei.aoparms.aspect;

import android.text.TextUtils;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import java.util.concurrent.TimeUnit;

import cn.com.superLei.aoparms.annotation.Scheduled;
import cn.com.superLei.aoparms.common.collection.NoEmptyHashMap;
import cn.com.superLei.aoparms.common.reflect.Reflect;
import cn.com.superLei.aoparms.common.reflect.ReflectException;
import cn.com.superLei.aoparms.common.utils.Preconditions;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@Aspect
public class ScheduledAspect {

    private static final String TAG = "ScheduledAspect";
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Scheduled * *(..))";
    private Disposable disposable;

    @Pointcut(POINTCUT_METHOD)
    public void onScheduledMethod() {
    }

    @Around("onScheduledMethod() && @annotation(scheduled)")
    public Object doScheduledMethod(final ProceedingJoinPoint joinPoint, Scheduled scheduled) throws Throwable {

        String key = scheduled.key();
        if (TextUtils.isEmpty(key)){
            key = joinPoint.getSignature().getName();
        }
        long initialDelay = scheduled.initialDelay();
        long interval = scheduled.interval();
        final int counts = scheduled.count();
        TimeUnit timeUnit = scheduled.timeUnit();
        final String taskExpiredCallback = scheduled.taskExpiredCallback();
        Object result = null;
        String finalKey = key;
        disposable = Observable.interval(initialDelay+interval, interval, timeUnit)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        return aLong + 1;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        Log.d(TAG, "count: " + count);
                        if (count == (counts-1)) {
                            if (disposable != null) {
                                disposable.dispose();
                            }
                            NoEmptyHashMap.getInstance().remove(finalKey);
                            doTaskExpiredCallback(joinPoint, taskExpiredCallback);
                        }
                    }
                });
        NoEmptyHashMap.getInstance().put(key, disposable);
        result = joinPoint.proceed();
        return result;
    }

    private void doTaskExpiredCallback(ProceedingJoinPoint joinPoint, String taskExpiredCallback){
        if (Preconditions.isNotBlank(taskExpiredCallback)) {

            try {
                Reflect.on(joinPoint.getTarget()).callback(taskExpiredCallback);
            } catch (ReflectException exception) {
                exception.printStackTrace();
                Log.e(TAG, "no method "+taskExpiredCallback);
            }
        }
    }

}
