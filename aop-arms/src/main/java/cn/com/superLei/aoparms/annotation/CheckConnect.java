package cn.com.superLei.aoparms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckConnect {
    //检查蓝牙是否连接  0代表ble   1代表spp
    int value() default 0;
}
