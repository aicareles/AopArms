package com.heaton.baselibsample;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.Options;
import cn.com.superLei.aoparms.annotation.Delay;
import cn.com.superLei.aoparms.annotation.EnableSystrace;
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
@EnableSystrace(filter = 60L, containNative = true)//开启耗时监控,过滤时间>60ms,包含native系统方法
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static MyApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        AopArms.init(this, new Options().setLoggable(true));

        AopArms.setInterceptor((key, methodName) -> {
            Log.e(TAG, "intercept methodName:>>>>>"+methodName);
            if ("login_intercept".equals(key)){
                String userId = ArmsPreference.get(mApplication, "userId", "");
                if (TextUtils.isEmpty(userId)){
                    Toast.makeText(mApplication, "您还没有登录", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            return false;
        });
        AopArms.setStatisticCallback(statisticInfo -> {
            Log.e(TAG, "statisticInfo: "+statisticInfo.toString());
        });

        initSDK();

    }

    /**
     * 异步初始化第三方sdk,可设置线程优先级
     */
    @Delay(delay = 100L, asyn = true, priority = 10)
    public void initSDK(){
        //...
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
