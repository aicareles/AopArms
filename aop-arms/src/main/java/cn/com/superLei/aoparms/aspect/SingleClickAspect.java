package cn.com.superLei.aoparms.aspect;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

import cn.com.superLei.aoparms.R;
import cn.com.superLei.aoparms.annotation.SingleClick;

/**
 * Created by jerry on 2018/6/13.
 */

@Aspect
public class SingleClickAspect {
    static int TIME_TAG = R.id.click_time;

    @Pointcut("execution(@cn.com.superLei.aoparms.annotation.SingleClick * *(..))")//方法切入点
    public void onSingleClickMethod() {
    }

    @Around("onSingleClickMethod() && @annotation(singleClick)")//在连接点进行方法替换
    public void doSingleClickMethod(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs())
            if (arg instanceof View) view = (View) arg;
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = ((tag != null) ? (long) tag : 0);
            Log.d("SingleClickAspect", "lastClickTime:" + lastClickTime);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long value = singleClick.value();
            int[] ids = singleClick.ids();

            if (currentTime - lastClickTime > value || !hasId(ids, view.getId())) {//过滤掉500毫秒内的连续点击
                view.setTag(TIME_TAG, currentTime);
                Log.d("SingleClickAspect", "currentTime:" + currentTime);
                joinPoint.proceed();//执行原方法
            }

        }
    }

    public static boolean hasId(int[] arr, int value) {
        for (int i : arr) {
            if (i == value)
                return true;
        }
        return false;
    }
}
