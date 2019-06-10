package cn.com.superLei.aoparms.aspect;

import android.text.TextUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Intercept;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class InterceptAspect {
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Intercept * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onInterceptMethod() {
    }

    @Around("onInterceptMethod() && @annotation(intercept)")
    public Object doInterceptMethod(ProceedingJoinPoint joinPoint, Intercept intercept) throws Throwable {
        if (AopArms.getInterceptor() == null)return joinPoint.proceed();
        String value = intercept.value();
        if (!TextUtils.isEmpty(value)){
            //拦截
            boolean result = proceedIntercept(intercept.value(), joinPoint);
            return result ? null : joinPoint.proceed();
        }
        return joinPoint.proceed();
    }

    private boolean proceedIntercept(String value, ProceedingJoinPoint joinPoint) throws Throwable {
        boolean intercept = AopArms.getInterceptor().intercept(value, joinPoint.getSignature().getName());
        if (intercept){
            return true;
        }
        return false;
    }
}
