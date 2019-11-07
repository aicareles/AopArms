package cn.com.superLei.aoparms;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import cn.com.superLei.aoparms.callback.Interceptor;
import cn.com.superLei.aoparms.callback.StatisticCallback;
import cn.com.superLei.aoparms.common.statistic.StatisticsLife;

/**
 * description $desc$
 * created by jerry on 2019/5/31.
 */
public class AopArms {
    @SuppressLint("StaticFieldLeak")
    private static Application application;
    private static Interceptor sInterceptor;
    private static StatisticCallback statisticCallback;

    public static void init(Application app){
        application = app;
        StatisticsLife.registerStatisticsLife(application);
    }

    public static Application getApplication(){
        if (application == null){
            throw new IllegalStateException("please init AopArms");
        }
        return application;
    }

    public static Context getContext(){
        return getApplication().getApplicationContext();
    }

    public static void setInterceptor(Interceptor interceptor){
        sInterceptor = interceptor;
    }

    public static Interceptor getInterceptor(){
        return sInterceptor;
    }

    public static StatisticCallback getStatisticCallback() {
        return statisticCallback;
    }

    public static void setStatisticCallback(StatisticCallback statisticCallback) {
        AopArms.statisticCallback = statisticCallback;
    }
}
