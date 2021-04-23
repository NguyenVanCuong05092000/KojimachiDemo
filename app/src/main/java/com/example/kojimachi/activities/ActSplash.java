package com.example.kojimachi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.kojimachi.constant.NotificationConstants;

public class ActSplash extends Activity implements NotificationConstants {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            try {
                if (!isFinishing()) {
                    Intent intent = new Intent(ActSplash.this, ActMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            } catch (Throwable ignored) {
            }
        }, 2000);
    }
}
