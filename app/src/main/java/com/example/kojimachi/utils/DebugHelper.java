package com.example.kojimachi.utils;

import android.util.Log;

import com.example.kojimachi.constant.AppConstants;

public class DebugHelper {
    public static void Log(String TAG, String msg) {
        if (AppConstants.LOG_DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
