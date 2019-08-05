package cn.com.superLei.aoparms.aspect;

import android.app.Fragment;
import android.content.Context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Permission;
import cn.com.superLei.aoparms.annotation.PermissionDenied;
import cn.com.superLei.aoparms.annotation.PermissionNoAskDenied;
import cn.com.superLei.aoparms.common.permission.IPermission;
import cn.com.superLei.aoparms.common.permission.AopPermissionActivity;


/**
 * Created by jerry on 2018/6/13.
 */

@Aspect
public class PermissionAspect {

    @Around("execution(@cn.com.superLei.aoparms.annotation.Permission * *(..)) && @annotation(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable {
        Context context = null;
        final Object object = joinPoint.getThis();
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else {
            //获取切入点方法上的参数列表
            Object[] objects = joinPoint.getArgs();
            if (objects.length > 0) {
                //非静态方法且第一个参数为context
                if (objects[0] instanceof Context) {
                    context = (Context) objects[0];
                } else {
                    //没有传入context 默认使用application
                    context = AopArms.getContext();
                }
            } else {
                context = AopArms.getContext();
            }
        }
        if (context == null || permission == null) {
            joinPoint.proceed();
            return;
        }
        AopPermissionActivity.PermissionRequest(context, permission.value(),
                permission.requestCode(), permission.rationale(), new IPermission() {
                    @Override
                    public void permissionGranted() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void permissionNoAskDenied(int requestCode, List<String> denyNoAskList) {
                        Class<?> cls = object.getClass();
                        Method[] methods = cls.getDeclaredMethods();
                        if (methods.length == 0) return;
                        for (Method method : methods) {
                            //过滤不含自定义注解PermissionNoAskDenied的方法
                            boolean isHasAnnotation = method.isAnnotationPresent(PermissionNoAskDenied.class);
                            if (isHasAnnotation) {
                                method.setAccessible(true);
                                //获取方法类型
                                Class<?>[] types = method.getParameterTypes();
                                if (types.length != 2) return;
                                //获取方法上的注解
                                PermissionNoAskDenied aInfo = method.getAnnotation(PermissionNoAskDenied.class);
                                if (aInfo == null) return;
                                //解析注解上对应的信息
                                try {
                                    method.invoke(object, requestCode, denyNoAskList);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void permissionDenied(int requestCode, List<String> denyList) {
                        Class<?> cls = object.getClass();
                        Method[] methods = cls.getDeclaredMethods();
                        if (methods.length == 0) return;
                        for (Method method : methods) {
                            //过滤不含自定义注解PermissionDenied的方法
                            boolean isHasAnnotation = method.isAnnotationPresent(PermissionDenied.class);
                            if (isHasAnnotation) {
                                method.setAccessible(true);
                                //获取方法类型
                                Class<?>[] types = method.getParameterTypes();
                                if (types.length != 2) return;
                                //获取方法上的注解
                                PermissionDenied aInfo = method.getAnnotation(PermissionDenied.class);
                                if (aInfo == null) return;
                                //解析注解上对应的信息
                                try {
                                    method.invoke(object, requestCode, denyList);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                });
    }
}
