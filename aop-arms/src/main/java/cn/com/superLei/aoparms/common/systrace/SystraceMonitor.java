package cn.com.superLei.aoparms.common.systrace;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Printer;

import cn.com.superLei.aoparms.AopArms;
import cn.com.superLei.aoparms.AopLog;

public class SystraceMonitor {
    private static final String TAG = "SystraceMonitor";
    private static SystraceMonitor sInstance = new SystraceMonitor();
    private Handler mIoHandler;
    //方法耗时的卡口,60毫秒
    private static final long TIME_BLOCK = 60L;
    private long filterTime = TIME_BLOCK;
    private long startMonitorTime = 0L;
    private boolean isBlock = false;
    private boolean isContainNative = false;

    private SystraceMonitor() {
        HandlerThread logThread = new HandlerThread("log");
        logThread.start();
        mIoHandler = new Handler(logThread.getLooper());
    }

    public void start() {
        Looper.getMainLooper().setMessageLogging(new Printer() {
            //分发和处理消息开始前的log
            private static final String START = ">>>>> Dispatching";
            //分发和处理消息结束后的log
            private static final String END = "<<<<< Finished";

            @Override
            public void println(String x) {
                if (x.startsWith(START)) {
                    //开始计时
                    startMonitor();
                }
                if (x.startsWith(END)) {
                    //结束计时，并计算出方法执行时间
                    removeMonitor();
                }
            }
        });

    }

    public boolean isContainNative() {
        return isContainNative;
    }

    public void setContainNative(boolean containNative) {
        isContainNative = containNative;
    }

    public long getFilterTime() {
        return filterTime;
    }

    public void setFilterTime(long filterTime) {
        this.filterTime = filterTime;
    }

    private Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            isBlock = true;
            //打印出执行的耗时方法的栈消息
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                /*if (isContainNative){
                    sb.append(s.toString());
                }else {
                    if (s.getClassName().contains(AopArms.getContext().getPackageName())){
                        sb.append(s.toString());
                    }
                }*/
                sb.append(s.toString());
                sb.append("\n");
            }
            AopLog.w(TAG, sb.toString());
        }
    };

    public static SystraceMonitor getInstance() {
        return sInstance;
    }

    /**
     * 开始计时
     */
    public void startMonitor() {
        startMonitorTime = System.currentTimeMillis();
        mIoHandler.postDelayed(mLogRunnable, filterTime);
    }

    /**
     * 停止计时
     */
    public void removeMonitor() {
        if (isBlock){
            AopLog.w(TAG, "耗时时长: ["+(System.currentTimeMillis() - startMonitorTime)+"ms]");
        }
        isBlock = false;
        startMonitorTime = 0L;
        mIoHandler.removeCallbacks(mLogRunnable);
    }
}
