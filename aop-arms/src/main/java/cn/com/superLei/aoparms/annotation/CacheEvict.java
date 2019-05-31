package cn.com.superLei.aoparms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheEvict {

    String key();

    // 缓存的清除是否在方法之前执行, 默认代表缓存清除操作是在方法执行之后执行；如果出现异常缓存就不会清除
    boolean beforeInvocation() default false;

    boolean allEntries() default false;//是否清空所有缓存
}
