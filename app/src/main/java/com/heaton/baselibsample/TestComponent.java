package com.heaton.baselibsample;

import android.util.Log;

import cn.com.superLei.aoparms.annotation.Component;

/**
 * description $desc$
 * created by jerry on 2019/6/4.
 */
@Component
public class TestComponent {


    private static final String TAG = "TestComponent";

    public static void main(String[] args) {
        byte[] data = new byte[4];
        data[0] = (byte) 255;
        data[1] = (byte) 0xff;

        Log.e(TAG, "main: ");
    }
}
