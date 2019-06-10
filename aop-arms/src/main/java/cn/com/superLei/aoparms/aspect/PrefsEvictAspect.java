package cn.com.superLei.aoparms.aspect;

import android.text.TextUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.PrefsEvict;
import cn.com.superLei.aoparms.common.utils.ArmsPreference;


/**
 * Created by jerry on 2018/6/13.
 */

@Aspect
public class PrefsEvictAspect {

    @Pointcut("execution(@cn.com.superLei.aoparms.annotation.PrefsEvict * *(..))")
    public void onPrefsEvictMethod() {
    }

    @Around("onPrefsEvictMethod() && @annotation(prefsEvict)")
    public Object doPrefsEvictMethod(final ProceedingJoinPoint joinPoint, PrefsEvict prefsEvict) throws Throwable {
        Object result = null;
        if (prefsEvict!=null) {
            String key = prefsEvict.key();
            boolean allEntries = prefsEvict.allEntries();
            result = joinPoint.proceed();
            if (allEntries){
                if (!TextUtils.isEmpty(key))
                    throw new IllegalArgumentException("Key cannot have value when cleaning all caches");
                ArmsPreference.clear(AopArms.getContext());
            }
            ArmsPreference.remove(AopArms.getContext(), key);
        } else {
            // 不影响原来的流程
            result = joinPoint.proceed();
        }

        return result;
    }

}
