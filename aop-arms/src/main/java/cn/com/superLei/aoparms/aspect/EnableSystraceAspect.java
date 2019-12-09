package cn.com.superLei.aoparms.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import cn.com.superLei.aoparms.annotation.EnableSystrace;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class EnableSystraceAspect {
//    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.EnableSystrace * *(..))";
    private static final String POINTCUT_METHOD = "execution(* android.app.Application.onCreate(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onSystraceMethod() {

    }

    @Around("onSystraceMethod() && @annotation(enableSystrace)")
    public Object doSystraceMethod(ProceedingJoinPoint joinPoint, EnableSystrace enableSystrace) throws Throwable {
        long filter = enableSystrace.filter();
        boolean containNative = enableSystrace.containNative();
        Class clazz = joinPoint.getClass();
        if (clazz.isAnnotationPresent(EnableSystrace.class)){

        }
        return joinPoint.proceed();
    }
}
