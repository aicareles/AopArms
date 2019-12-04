package cn.com.superLei.aoparms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Delay {

    String key() default "";

    /**
     * 回调是否在异步线程
     * @return
     */
    boolean asyn() default false;

    int priority() default 5;//一旦改变优先级,则会重新new Thread

    long delay() default 0L; //延迟时间 单位是毫秒

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
