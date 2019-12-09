package cn.com.superLei.aoparms;

//配置类
public class Options {
    private boolean isLoggable = false;
    private String logTag = "AopArms";

    public Options() {
    }

    public boolean isLoggable() {
        return isLoggable;
    }

    public Options setLoggable(boolean loggable) {
        isLoggable = loggable;
        return this;
    }

    public String getLogTag() {
        return logTag;
    }

    public Options setLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }

}
