package com.inscripts.cometchatsample;

import android.support.multidex.MultiDex;

import cometchat.inscripts.com.readyui.CCCometchatUI;

/**
 * Created by adityagokula on 30/05/18.
 */

public class CometChatApplication extends CCCometchatUI {


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
