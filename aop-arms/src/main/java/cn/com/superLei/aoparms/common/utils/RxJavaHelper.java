package cn.com.superLei.aoparms.common.utils;

import android.os.Process;
import android.support.annotation.Size;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class RxJavaHelper {

    public static ThreadFactory newFactory(@Size(min = 1, max = 10) int priority){
        return new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull final Runnable r) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //这里的r是线程池或Sceduler的Worker，所以我们要在调用r.run()方法前提前设置优先级
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        r.run();
                    }
                });
                thread.setPriority(priority);
                return thread;
            }
        };
    }

    public static Scheduler scheduler(int priority){
        if (priority!=5){
            return Schedulers.from(Executors.newScheduledThreadPool(1, newFactory(priority)));
        }
        return Schedulers.io();
    }

}
