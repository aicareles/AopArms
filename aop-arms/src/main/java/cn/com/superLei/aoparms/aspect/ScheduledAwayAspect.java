package cn.com.superLei.aoparms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.annotation.ScheduledAway;
import cn.com.superLei.aoparms.common.collection.NoEmptyHashMap;
import io.reactivex.disposables.Disposable;

@Aspect
public class ScheduledAwayAspect {

    private static final String TAG = "ScheduledAspect";
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.ScheduledAway * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onScheduledAwayMethod() {
    }

    @Around("onScheduledAwayMethod() && @annotation(scheduledAway)")
    public Object doScheduledAwayMethod(final ProceedingJoinPoint joinPoint, ScheduledAway scheduledAway) throws Throwable {
        Object result = null;
        String key = scheduledAway.key();
        Disposable subscribe = (Disposable) NoEmptyHashMap.getInstance().get(key);
        if (subscribe != null){
            subscribe.dispose();
            NoEmptyHashMap.getInstance().remove(key);
        }
        result = joinPoint.proceed();
        return result;
    }

}
