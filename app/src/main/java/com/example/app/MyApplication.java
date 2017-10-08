package com.example.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 635901193 on 2017/10/8.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
