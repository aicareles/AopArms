package cn.com.superLei.aoparms;

import android.annotation.SuppressLint;
import android.content.Context;

import cn.com.superLei.aoparms.callback.Interceptor;

/**
 * description $desc$
 * created by jerry on 2019/5/31.
 */
public class AopArms {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Interceptor sInterceptor;

    public static void init(Context context){
        mContext = context;
    }

    public static Context getContext(){
        if (mContext == null){
            throw new IllegalStateException("please init AopArms");
        }
        return mContext;
    }

    public static void setInterceptor(Interceptor interceptor){
        sInterceptor = interceptor;
    }

    public static Interceptor getInterceptor(){
        return sInterceptor;
    }


}
