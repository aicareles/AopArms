#### 一、简介
当下Java后端的SpringBoot微服务框架大火，原因离不开注解的使用，其简单易配置的注解方式使得更多的社区为其编写适用于SpringBoot的框架，也就是注解逐渐取代了传统的xml配置方式。那么注解在Android中也同样的得到了升华，著名的框架有ButterKnife、 Dagger2、Retrofit等等。今天带来一款Android中比较实用的注解框架[AopArms](https://github.com/AICareless/AopArms)，其用法简单，里面编写了Android开发中常用的一套注解，如日志、异步处理、缓存、SP、延迟操作、定时任务、重试机制、try-catch安全机制、过滤频繁点击等，后续还会有更多更强大的注解功能加入。
本篇主要内容讲解在Android中的基本用法，关于AOP在Android中的实践请参考另外一篇[Android开发之AOP编程](https://juejin.im/post/5ce749b6f265da1bba58de15)。

#### 二、引入方式
1、在主工程中添加依赖
```
//引入aspectjx插件
apply plugin: 'android-aspectjx'

dependencies {
    ...
    implementation 'cn.com.superLei:aop-arms:1.0.1'
}
```
2、项目跟目录的gradle脚本中加入
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        //该库基于沪江aspect插件库
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
    }
}

```
#### 三、基本使用
1、缓存篇(可缓存任意类型)
```
1、插入缓存
    @Cache(key = "userList")
    private ArrayList<User> initData() {
        ArrayList<User> list = new ArrayList<>();
        for (int i=0; i<5; i++){
            User user = new User();
            user.setName("艾神一不小心:"+i);
            user.setPassword("密码:"+i);
            list.add(user);
        }
        return list;
    }
    
2、获取缓存
    private ArrayList<User> getUser() {
        return ACache.get(this).getAsList("userList", User.class);
    }

3、移除缓存
    @CacheEvict(key = "userList")
    public void removeUser() {
        Log.e(TAG, "removeUser: >>>>");
    }
    
```
2、SharedPreferences篇(可保存对象)
```
1、保存key到sp
    @Prefs(key = "article")
    private Article initArticle() {
        Article article = new Article();
        article.author = "jerry";
        article.title = "hello android";
        article.createDate = "2019-05-31";
        article.content = "this is a test demo";
        return article;
    }
    
2、从sp中移除key
    @PrefsEvict(key = "article")
    public void removeArticle() {
        Log.e(TAG, "removeArticle: >>>>");
    }
```
3、异步篇
```
    @Async
    public void asyn() {
        Log.e(TAG, "useAync: "+Thread.currentThread().getName());
    }
```

![](https://user-gold-cdn.xitu.io/2019/6/1/16b10cdeae2bfe6a?w=1164&h=59&f=png&s=3012)

4、try-catch安全机制篇
```
    //自动帮你try-catch   允许你定义回调方法
    @Safe(callBack = "throwMethod")
    public void safe() {
        String str = null;
        str.toString();
    }
    
    //自定义回调方法（注意要和callBack的值保持一致）
    private void throwMethod(Throwable throwable){
        Log.e(TAG, "throwMethod: >>>>>"+throwable.toString());
    }
```
5、重试机制篇
```
     /**
     * @param count 重试次数
     * @param delay 每次重试的间隔
     * @param asyn 是否异步执行
     * @param retryCallback 自定义重试结果回调
     * @return 当前方法是否执行成功
     */
    @Retry(count = 3, delay = 1000, asyn = true, retryCallback = "retryCallback")
    public boolean retry() {
        Log.e(TAG, "retryDo: >>>>>>"+Thread.currentThread().getName());
        return false;
    }
    
    private void retryCallback(boolean result){
        Log.e(TAG, "retryCallback: >>>>"+result);
    }
```

![](https://user-gold-cdn.xitu.io/2019/6/1/16b10d47fc840ef9?w=741&h=107&f=png&s=3128)

6、定时任务篇
```
     /**
     * @param interval 初始化延迟
     * @param interval 时间间隔
     * @param timeUnit 时间单位
     * @param count 执行次数
     * @param taskExpiredCallback 定时任务到期回调
     */
    @Scheduled(interval = 1000L, count = 10, taskExpiredCallback = "taskExpiredCallback")
    public void scheduled() {
        Log.e(TAG, "scheduled: >>>>");
    }
    
    private void taskExpiredCallback(){
        Log.e(TAG, "taskExpiredCallback: >>>>");
    }
```

![](https://user-gold-cdn.xitu.io/2019/6/1/16b10d9680d978d2?w=741&h=202&f=png&s=3283)

7、延迟任务篇
```
    //开启延迟任务（10s后执行该方法）
    @Delay(key = "test", delay = 10000L)
    public void delay() {
        Log.e(TAG, "delay: >>>>>");
    }
    
    //移除延迟任务
    @DelayAway(key = "test")
    public void cancelDelay() {
        Log.e(TAG, "cancelDelay: >>>>");
    }
```

8、过滤频繁点击
```
    //value默认500ms
    @SingleClick(value = 2000L)
    private void onclick(){
        Log.e(TAG, "onclick: >>>>");
    }
```
