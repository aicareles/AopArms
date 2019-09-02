package com.heaton.baselibsample;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.superLei.aoparms.annotation.*;
import cn.com.superLei.aoparms.common.permission.AopPermissionUtils;
import cn.com.superLei.aoparms.common.utils.ArmsCache;
import cn.com.superLei.aoparms.common.utils.ArmsPreference;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private String str;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        initArticle();

    }

    /**
     * 开启请求权限注解
     * @ value 权限值
     * @ rationale 拒绝后的下一次提示(开启后，拒绝后，下一次会先提示该权限申请提示语)
     * @ requestCode 权限请求码标识
     */
    @Permission(value = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, rationale = "为了更好的体验，请打开相关权限")
    public void permission(View view) {
        Log.e(TAG, "permission: 权限已打开");
    }

    /**
     * 请求拒绝注解回调
     * @param requestCode 权限请求码标识
     * @param denyList 被拒绝的权限集合
     */
    @PermissionDenied
    public void permissionDenied(int requestCode, List<String> denyList){
        Log.e(TAG, "permissionDenied: "+requestCode);
        Log.e(TAG, "permissionDenied>>>: "+denyList.toString());
    }

    /**
     * 请求拒绝且不在提示注解回调
     * @param requestCode 权限请求码标识
     * @param denyNoAskList 被拒绝且不再提示的权限集合
     */
    @PermissionNoAskDenied
    public void permissionNoAskDenied(int requestCode, List<String> denyNoAskList){
        Log.e(TAG, "permissionNoAskDenied: "+requestCode);
        Log.e(TAG, "permissionNoAskDenied>>>: "+denyNoAskList.toString());
        AopPermissionUtils.showGoSetting(this, "为了更好的体验，建议前往设置页面打开权限");
    }

    /**
     * key：缓存的键
     * expiry：缓存过期时间,单位s
     *
     * @return 缓存的值
     */
    @Cache(key = "userList", expiry = 60 * 60 * 24)
    private ArrayList<User> initData() {
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("艾神一不小心:" + i);
            user.setPassword("密码:" + i);
            list.add(user);
        }
        return list;
    }

    @Prefs(key = "article")
    private Article initArticle() {
        Article article = new Article();
        article.author = "jerry";
        article.title = "hello android";
        article.createDate = "2019-05-31";
        article.content = "this is a test demo";
        return article;
    }

    public void getArticle(View view) {
        Article article = ArmsPreference.get(this, "article", null);
        Log.e(TAG, "getArticle: " + article);
    }

    public void getUser(View view) {
        ArrayList<User> users = ArmsCache.get(this).getAsList("userList", User.class);
        Log.e(TAG, "getUser: " + users);
    }

    /**
     * key:缓存的键
     * beforeInvocation:缓存的清除是否在方法之前执行, 如果出现异常缓存就不会清除   默认false
     * allEntries：是否清空所有缓存(与key互斥)  默认false
     */
    @CacheEvict(key = "userList", beforeInvocation = true, allEntries = false)
    public void removeUser(View view) {
        Log.e(TAG, "removeUser: >>>>");
    }

    /**
     * key:sp的键
     * allEntries：是否清空所有存储(与key互斥)  默认false
     */
    @PrefsEvict(key = "article", allEntries = false)
    public void removeArticle(View view) {
        Log.e(TAG, "removeArticle: >>>>");
    }

    @Async
    public void asyn(View view) {
        Log.e(TAG, "useAync: " + Thread.currentThread().getName());
    }

    @Safe(callBack = "throwMethod")
    public void safe(View view) {
        str.toString();
    }

    @SingleClick(value = 2000L)
    private void onclick() {
        Log.e(TAG, "onclick: >>>>");
    }

    @Callback
    public void throwMethod(Throwable throwable) {
        Log.e(TAG, "throwMethod: >>>>>" + throwable.toString());
    }

    @Retry(count = 3, delay = 1000, asyn = true, retryCallback = "retryCallback")
    public boolean retry(View view) {
        Log.e(TAG, "retryDo: >>>>>>" + Thread.currentThread().getName());
        return false;
    }

    @Callback
    public void retryCallback(boolean result) {
        Log.e(TAG, "retryCallback: >>>>" + result);
    }

    @Scheduled(interval = 1000L, count = 10, taskExpiredCallback = "taskExpiredCallback")
    public void scheduled(View view) {
        Log.e(TAG, "scheduled: >>>>");
    }

    @Callback
    public void taskExpiredCallback() {
        Log.e(TAG, "taskExpiredCallback: >>>>");
    }

    @Delay(key = "test", delay = 10000L)
    public void delay(View view) {
        Log.e(TAG, "delay: >>>>>");
    }

    @DelayAway(key = "test")
    public void cancelDelay(View view) {
        Log.e(TAG, "cancelDelay: >>>>");
    }

    @Intercept("login_intercept")
    public void intercept(View view) {
        Log.e(TAG, "intercept: 已登陆>>>>");
    }

    @Prefs(key = "userId")
    public String login(View view) {
        return "1";
    }

    @PrefsEvict(key = "userId")
    public void logout(View view) {
        Log.e(TAG, "logout: >>>>>");
    }

    @SingleClick(ids = {R.id.singleClick, R.id.singleClick2})
    @OnClick({R.id.singleClick1, R.id.singleClick, R.id.singleClick2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.singleClick1:
                Log.e(TAG, "我不防抖");
                break;
            case R.id.singleClick:
                Log.e(TAG, "我防抖");
                break;
            case R.id.singleClick2:
                Log.e(TAG, "我防抖2");
                break;
        }
    }
}
