package cn.com.superLei.aoparms.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.com.superLei.aoparms.annotation.Safe;
import cn.com.superLei.aoparms.common.reflect.Reflect;
import cn.com.superLei.aoparms.common.reflect.ReflectException;
import cn.com.superLei.aoparms.common.utils.Preconditions;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Aspect
public class SafeAspect {

    private static final String TAG = "SafeAspect";
    private static final String POINTCUT_METHOD = "execution(@cn.com.superLei.aoparms.annotation.Safe * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onSafeMethod() {
    }

    @Around("onSafeMethod() && @annotation(safe)")
    public Object doSafeMethod(final ProceedingJoinPoint joinPoint, Safe safe) throws Throwable {

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            Log.w(TAG, getStringFromException(e));

            String callBack = safe.callBack();

            if (Preconditions.isNotBlank(callBack)) {

                try {
                    Reflect.on(joinPoint.getTarget()).callback(callBack, e);
                } catch (ReflectException exception) {
                    exception.printStackTrace();
                    Log.e(TAG, "no method "+callBack);
                }
            }
        }
        return result;
    }

    private static String getStringFromException(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
