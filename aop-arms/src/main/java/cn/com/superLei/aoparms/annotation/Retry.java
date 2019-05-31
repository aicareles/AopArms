package cn.com.superLei.aoparms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {
    /**
     * 重试次数
     * @return
     */
    int count() default 0;


    /**
     * 重试的间隔时间
     * @return
     */
    long delay() default 0L;


    /**
     * 是否支持异步重试方式
     * @return
     */
    boolean asyn() default false;

    /**
     * 重试n次后，结果的回调
     * @return
     */
    String retryCallback() default "";
}
