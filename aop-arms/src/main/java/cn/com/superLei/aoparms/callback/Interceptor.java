package cn.com.superLei.aoparms.callback;

/**
 * description $desc$
 * created by jerry on 2019/6/3.
 */
public interface Interceptor {
    boolean intercept(String key, String methodName) throws Throwable;
}
