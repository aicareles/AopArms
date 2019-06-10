package cn.com.superLei.aoparms.aspect;

import android.text.TextUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.CacheEvict;
import cn.com.superLei.aoparms.common.utils.ArmsCache;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class CacheEvictAspect {
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.CacheEvict * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onCacheEvictMethod() {
    }

    @Around("onCacheEvictMethod() && @annotation(cacheEvict)")
    public Object doCacheEvictMethod(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        String key = cacheEvict.key();
        boolean beforeInvocation = cacheEvict.beforeInvocation();
        boolean allEntries = cacheEvict.allEntries();
        ArmsCache aCache = ArmsCache.get(AopArms.getContext());
        Object result = null;
        if (allEntries){
            if (!TextUtils.isEmpty(key))
                throw new IllegalArgumentException("Key cannot have value when cleaning all caches");
            aCache.clear();
        }
        if (beforeInvocation){
            aCache.remove(key);
            result = joinPoint.proceed();
        }else {
            result = joinPoint.proceed();
            aCache.remove(key);
        }
        return result;
    }
}
