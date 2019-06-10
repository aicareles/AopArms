package cn.com.superLei.aoparms.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.Serializable;
import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Cache;
import cn.com.superLei.aoparms.common.utils.ArmsCache;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class CacheAspect {
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Cache * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onCacheMethod() {
    }

    @Around("onCacheMethod() && @annotation(cache)")
    public Object doCacheMethod(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        String key = cache.key();
        int expiry = cache.expiry();

        Object result = joinPoint.proceed();
        ArmsCache aCache = ArmsCache.get(AopArms.getContext());
        if (expiry>0) {
            aCache.put(key,(Serializable)result,expiry);
        } else {
            aCache.put(key,(Serializable)result);
        }
        return result;
    }
}
