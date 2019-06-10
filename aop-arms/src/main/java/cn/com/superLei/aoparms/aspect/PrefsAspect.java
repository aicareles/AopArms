package cn.com.superLei.aoparms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Prefs;
import cn.com.superLei.aoparms.common.utils.ArmsPreference;


/**
 * Created by jerry on 2018/6/13.
 */

@Aspect
public class PrefsAspect {

    @Pointcut("execution(@cn.com.superLei.aoparms.annotation.Prefs * *(..))")
    public void onPrefsMethod() {
    }

    @Around("onPrefsMethod() && @annotation(prefs)")
    public Object doPrefsMethod(final ProceedingJoinPoint joinPoint, Prefs prefs) throws Throwable {
        Object result = null;
        if (prefs!=null) {
            String key = prefs.key();

            result = joinPoint.proceed();
            String type = ((MethodSignature) joinPoint.getSignature()).getReturnType().toString();

            if (!"void".equalsIgnoreCase(type)) {
                ArmsPreference.put(AopArms.getContext(), key, result);
            }
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }

}
