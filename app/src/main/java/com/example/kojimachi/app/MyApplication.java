package com.example.kojimachi.app;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
    private static com.example.kojimachi.app.MyApplication _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
    }

    public static com.example.kojimachi.app.MyApplication getInstance() {
        return _instance;
    }
}
