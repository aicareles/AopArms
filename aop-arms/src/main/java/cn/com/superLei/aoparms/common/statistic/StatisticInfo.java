package cn.com.superLei.aoparms.common.statistic;

public class StatisticInfo {
    private int key;
    private long startTime;
    private String statisticName;
    private boolean isActivity;
    private boolean isMethod;
    private Enum<ActivityLife> activityLife;

    public StatisticInfo(int key, long startTime, String statisticName, boolean isMethod) {
        this.key = key;
        this.startTime = startTime;
        this.statisticName = statisticName;
        this.isMethod = isMethod;
    }

    public StatisticInfo(int key, long startTime, String statisticName, boolean isActivity, Enum<ActivityLife> activityLife) {
        this.key = key;
        this.startTime = startTime;
        this.statisticName = statisticName;
        this.isActivity = isActivity;
        this.activityLife = activityLife;
    }

    public Enum<ActivityLife> getActivityLife() {
        return activityLife;
    }

    public void setActivityLife(Enum<ActivityLife> activityLife) {
        this.activityLife = activityLife;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getStatisticName() {
        return statisticName;
    }

    public void setStatisticName(String statisticName) {
        this.statisticName = statisticName;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public void setMethod(boolean method) {
        isMethod = method;
    }

    @Override
    public String toString() {
        return "StatisticInfo{" +
                "key=" + key +
                ", startTime=" + startTime +
                ", statisticName='" + statisticName + '\'' +
                ", isActivity=" + isActivity +
                ", isMethod=" + isMethod +
                ", activityLife=" + activityLife +
                '}';
    }

    public enum ActivityLife {
        CREATE,
        RESTART,
        PAUSE,
        DESTROY
    }
}
