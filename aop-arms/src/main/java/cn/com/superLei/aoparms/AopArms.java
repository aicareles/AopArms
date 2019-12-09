package cn.com.superLei.aoparms;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import cn.com.superLei.aoparms.annotation.EnableSystrace;
import cn.com.superLei.aoparms.callback.Interceptor;
import cn.com.superLei.aoparms.callback.StatisticCallback;
import cn.com.superLei.aoparms.common.statistic.StatisticsLife;
import cn.com.superLei.aoparms.common.systrace.SystraceMonitor;

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
        init(app, new Options());
    }

    public static void init(Application app, Options options){
        if (app == null)throw new IllegalArgumentException("application is null");
        application = app;
        AopLog.init(options);
        StatisticsLife.registerStatisticsLife(application);
        systraceEnable(application);
    }

    private static void systraceEnable(Application app) {
        Class<? extends Application> cls = app.getClass();
        if (cls.isAnnotationPresent(EnableSystrace.class)){
            EnableSystrace systrace = cls.getAnnotation(EnableSystrace.class);
            if (systrace != null){
                long filter = systrace.filter();
                boolean containNative = systrace.containNative();
                SystraceMonitor monitor = SystraceMonitor.getInstance();
                monitor.setContainNative(containNative);
                monitor.setFilterTime(filter);
                monitor.start();
            }
        }
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
