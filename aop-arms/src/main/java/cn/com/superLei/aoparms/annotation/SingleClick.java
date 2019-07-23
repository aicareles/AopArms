package cn.com.superLei.aoparms.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复点击
 * Created by jerry on 2018/6/13.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    long value() default  500L;

    int[] ids() default {View.NO_ID};
}
