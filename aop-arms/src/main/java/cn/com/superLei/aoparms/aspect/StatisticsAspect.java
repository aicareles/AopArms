package cn.com.superLei.aoparms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Statistics;
import cn.com.superLei.aoparms.callback.StatisticCallback;
import cn.com.superLei.aoparms.common.statistic.StatisticInfo;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class StatisticsAspect {
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Statistics * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onStatisticsMethod() {
    }

    @Around("onStatisticsMethod() && @annotation(statistics)")
    public Object doStatisticsMethod(ProceedingJoinPoint joinPoint, Statistics statistics) throws Throwable {
        int value = statistics.value();
        StatisticCallback callback = AopArms.getStatisticCallback();
        if (callback != null){
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature){
                StatisticInfo info = new StatisticInfo(value, System.currentTimeMillis(),signature.getName(),true);
                callback.statistics(info);
            }
        }
        return joinPoint.proceed();
    }
}
