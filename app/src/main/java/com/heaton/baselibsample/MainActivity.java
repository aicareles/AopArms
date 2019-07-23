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

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.superLei.aoparms.annotation.Async;
import cn.com.superLei.aoparms.annotation.Cache;
import cn.com.superLei.aoparms.annotation.CacheEvict;
import cn.com.superLei.aoparms.annotation.Delay;
import cn.com.superLei.aoparms.annotation.DelayAway;
import cn.com.superLei.aoparms.annotation.Intercept;
import cn.com.superLei.aoparms.annotation.Permission;
import cn.com.superLei.aoparms.annotation.Prefs;
import cn.com.superLei.aoparms.annotation.PrefsEvict;
import cn.com.superLei.aoparms.annotation.Retry;
import cn.com.superLei.aoparms.annotation.Safe;
import cn.com.superLei.aoparms.annotation.Scheduled;
import cn.com.superLei.aoparms.annotation.SingleClick;
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

    @Permission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void permission(View view) {
        Log.e(TAG, "permission: ");
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

    private void throwMethod(Throwable throwable) {
        Log.e(TAG, "throwMethod: >>>>>" + throwable.toString());
    }

    @Retry(count = 3, delay = 1000, asyn = true, retryCallback = "retryCallback")
    public boolean retry(View view) {
        Log.e(TAG, "retryDo: >>>>>>" + Thread.currentThread().getName());
        return false;
    }

    private void retryCallback(boolean result) {
        Log.e(TAG, "retryCallback: >>>>" + result);
    }

    @Scheduled(interval = 1000L, count = 10, taskExpiredCallback = "taskExpiredCallback")
    public void scheduled(View view) {
        Log.e(TAG, "scheduled: >>>>");
    }

    private void taskExpiredCallback() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: " + requestCode + "permissions:" + permissions.toString());
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
}
