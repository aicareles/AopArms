package cn.com.superLei.aoparms;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * description $desc$
 * created by jerry on 2019/5/31.
 */
public class AopArms {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static void init(Context context){
        mContext = context;
    }

    public static Context getContext(){
        if (mContext == null){
            throw new IllegalStateException("please init BaseLibApi");
        }
        return mContext;
    }
}
