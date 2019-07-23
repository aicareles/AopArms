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
    implementation 'cn.com.superLei:aop-arms:1.0.2'
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
3、在Application中初始化
```
AopArms.init(this);
```
#### 三、基本使用
1、缓存篇(可缓存任意类型)
```
1、插入缓存
    /**
     * key：缓存的键
     * expiry：缓存过期时间,单位s
     * @return 缓存的值
     */
    @Cache(key = "userList", expiry = 60 * 60 * 24)
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
        return ArmsCache.get(this).getAsList("userList", User.class);
    }

3、移除缓存
    /**
     * key:缓存的键
     * beforeInvocation:缓存的清除是否在方法之前执行, 如果出现异常缓存就不会清除   默认false
     * allEntries：是否清空所有缓存(与key互斥)  默认false
     */
    @CacheEvict(key = "userList", beforeInvocation = true, allEntries = false)
    public void removeUser() {
        Log.e(TAG, "removeUser: >>>>");
    }
    
```
![](https://user-gold-cdn.xitu.io/2019/6/1/16b10cdeae2bfe6a?w=1164&h=59&f=png&s=3012)

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
    /**
     * key:sp的键
     * allEntries：是否清空所有存储(与key互斥)  默认false
     */
    @PrefsEvict(key = "article", allEntries = false)
    public void removeArticle() {
        Log.e(TAG, "removeArticle: >>>>");
    }

3、通过key从sp中获取value
    public void getArticle() {
        Article article = ArmsPreference.get(this, "article", null);
        Log.e(TAG, "getArticle: "+article);
    }
```
3、异步篇
```
    @Async
    public void asyn() {
        Log.e(TAG, "useAync: "+Thread.currentThread().getName());
    }
```

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
    @OnClick({R.id.singleClick1, R.id.singleClick, R.id.singleClick2})
    @SingleClick(ids = {R.id.singleClick, R.id.singleClick2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.singleClick1:
                Log.e("singleClick", "我不防抖");
                break;
            case R.id.singleClick:
                Log.e("singleClick", "我防抖");
                break;
            case R.id.singleClick2:
                Log.e("singleClick", "我防抖2");
                break;
        }
    }
```
9、拦截篇(如登录)
```
1、在需要进行拦截的方法添加注解
    @Intercept("login_intercept")
    public void loginIntercept() {
        Log.e(TAG, "intercept: 已登陆>>>>");
    }
2、（建议,统一处理）在Application中进行进行监听拦截回调
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static MyApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        AopArms.init(this);
        AopArms.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(String key, String methodName) throws Throwable {
                Log.e(TAG, "intercept methodName:>>>>>"+methodName);
                if ("login_intercept".equals(key)){
                    String userId = ArmsPreference.get(mApplication, "userId", "");
                    if (TextUtils.isEmpty(userId)){
                        Toast.makeText(mApplication, "您还没有登录", Toast.LENGTH_SHORT).show();
                        return true;//代表拦截
                    }
                }
                return false;//放行
            }
        });
    }
}

```
#### 四、参考
* [HujiangTechnology/gradle_plugin_android_aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
* [深入理解Android之AOP](https://blog.csdn.net/innost/article/details/49387395)

#### 五、许可证
```
Copyright 2016 liulei

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
