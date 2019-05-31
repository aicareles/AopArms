package cn.com.superLei.aoparms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.annotation.DelayAway;
import cn.com.superLei.aoparms.common.collection.NoEmptyHashMap;
import io.reactivex.disposables.Disposable;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class DelayAwayAspect {
    private static final String TAG = "DelayAwayAspect";
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.DelayAway * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onDelayAwayMethod() {
    }

    @Around("onDelayAwayMethod() && @annotation(delayAway)")
    public Object doDelayAwayMethod(final ProceedingJoinPoint joinPoint, DelayAway delayAway) throws Throwable {
        String key = delayAway.key();
        Disposable subscribe = (Disposable) NoEmptyHashMap.getInstance().get(key);
        if (subscribe != null){
            subscribe.dispose();
            NoEmptyHashMap.getInstance().remove(key);
        }
        return joinPoint.proceed();
    }
}
