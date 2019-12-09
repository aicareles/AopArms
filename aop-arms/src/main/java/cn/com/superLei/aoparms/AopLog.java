package cn.com.superLei.aoparms;

import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * 蓝牙日志类
 * Created by LiuLei on 2017/5/16.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class AopLog {

    public static String TAG = "AopArms";
    private static boolean isLoggable;

    public static void init(Options options){
        isLoggable = options.isLoggable();
        if (!TextUtils.isEmpty(options.getLogTag()))
            TAG = options.getLogTag();
    }

    private static String getSubTag(Object o){
        String tag = "";
        if(o instanceof String){
            tag = (String) o;
        }else if(o instanceof Number){
            tag = String.valueOf(o);
        }else {
            tag = o.getClass().getSimpleName();
        }
        return tag;
    }

    public static void e(Object o, String msg){
        if(isLoggable){
            Log.e(TAG,buildMessge(getSubTag(o), msg));
        }
    }

    public static void i(Object o, String msg){
        if(isLoggable){
            Log.i(TAG,buildMessge(getSubTag(o), msg));
        }
    }

    public static void w(Object o, String msg){
        if(isLoggable){
            Log.w(TAG,buildMessge(getSubTag(o), msg));
        }
    }

    public static void d(Object o, String msg){
        if(isLoggable){
            Log.d(TAG,buildMessge(getSubTag(o), msg));
        }
    }

    private static String buildMessge(String subTag, String msg){
        return String.format(Locale.CHINA, "[%d] %s: %s",
                Thread.currentThread().getId(), subTag, msg);
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.CHINA, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of VolleyLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(AopLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.CHINA, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }

}
