package cn.com.superLei.aoparms.common.statistic;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.annotation.Annotation;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.annotation.Statistics;
import cn.com.superLei.aoparms.callback.StatisticCallback;

public class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

    private void proceedStatistic(Activity activity, StatisticInfo.ActivityLife activityLife){
        Class<? extends Activity> cls = activity.getClass();
        if (cls.isAnnotationPresent(Statistics.class)){
            for(Annotation ann : cls.getDeclaredAnnotations()){
                if(ann instanceof Statistics){
                    int value = ((Statistics) ann).value();
                    StatisticInfo info = new StatisticInfo(value,System.currentTimeMillis(),cls.getSimpleName(),true, activityLife);
                    StatisticCallback callback = AopArms.getStatisticCallback();
                    if (callback != null){
                        callback.statistics(info);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        proceedStatistic(activity, StatisticInfo.ActivityLife.CREATE);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        proceedStatistic(activity, StatisticInfo.ActivityLife.PAUSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        proceedStatistic(activity, StatisticInfo.ActivityLife.DESTROY);
    }
}
