package com.heaton.baselibsample;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.callback.Interceptor;
import cn.com.superLei.aoparms.common.utils.ArmsPreference;


/**
 * 应用入口
 * Created by LiuLei on 2016/4/25.
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　...　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃   神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃　　　　　　　┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static MyApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        AopArms.init(this);

        AopArms.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(String key, String methodName) throws Throwable {
                Log.e(TAG, "intercept methodName:>>>>>"+methodName);
                if ("login_intercept".equals(key)){
                    String userId = ArmsPreference.get(mApplication, "userId", "");
                    if (TextUtils.isEmpty(userId)){
                        Toast.makeText(mApplication, "您还没有登录", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    public static MyApplication getInstance() {
        return mApplication;
    }


}
/**
 * 　                   &#######&
 * 　                   #########&
 * 　                  ###########&
 * 　                 ##&#$###$  ##&
 * 　                ;###  ####& ####
 * 　                ###;#######  ####
 * 　               &###########  #####
 * 　              ;#########o##   #####
 * 　              #########  ##   ######
 * 　            ;########### ###   ######
 * 　            ################   #######
 * 　           #################;   ######,
 * 　           #############$####   #######
 * 　          ########&;,########   &#######
 * 　        ;#########   &###       ########
 * 　        ##########    ###;      ########
 * 　       ###########     ##$      ########
 * 　       ###########     ###     #########
 * 　       ##########&$     ##    ;########
 * 　       #########, !      ##   ########
 * 　      ;########&          ##  #######
 * 　        #&#####            ##   &#&
 * 　          o# &#             #;
 * 　             ##             ##
 * 　             &#&           ;##
 * 　              ##           ###
 * <p/>
 * 　　　　　　　　　葱官赐福　　百无禁忌
 */
