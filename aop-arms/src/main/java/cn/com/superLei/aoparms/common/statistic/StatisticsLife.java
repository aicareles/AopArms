package cn.com.superLei.aoparms.common.statistic;

import android.app.Application;
import android.util.Log;

public class StatisticsLife {
    private static final String TAG = StatisticsLife.class.getSimpleName();

    private static ActivityLifecycleImpl sLifecycleImpl;

    public static ActivityLifecycleImpl getLifecycle() {
        return sLifecycleImpl;
    }

    public static void registerStatisticsLife(Application application) {
        if (sLifecycleImpl == null) {
            synchronized (StatisticsLife.class) {
                if (sLifecycleImpl == null) {
                    sLifecycleImpl = new ActivityLifecycleImpl();
                }
            }
        }
        application.registerActivityLifecycleCallbacks(sLifecycleImpl);
    }

    public static void unregisterStatisticsLife(Application application) {
        if (sLifecycleImpl != null) {
            application.unregisterActivityLifecycleCallbacks(sLifecycleImpl);
        } else {
            Log.i(TAG, "ActivityLifecycleImpl不能为空");
        }
    }

}
