package cn.com.superLei.aoparms.aspect;

import android.os.Looper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class MainThreadAspect {

    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.MainThread * *(..))";

    @Around("onMainThreadMethod()")
    public void doMainThreadMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        mainThreadMethod(joinPoint);
    }

    @Pointcut(POINTCUT_METHOD)
    public void onMainThreadMethod() {
    }

    private void mainThreadMethod(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            joinPoint.proceed();
        } else {
            Flowable.create(new FlowableOnSubscribe<Object>() {
                                @Override
                                public void subscribe(FlowableEmitter<Object> e) throws Exception {
                                    try {
                                        joinPoint.proceed();
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }
                    , BackpressureStrategy.BUFFER)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }
}
