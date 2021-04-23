package com.example.kojimachi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.example.kojimachi.constant.AppConstants;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    //
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private int screenWidth = 0;

    protected int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }


    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / AppConstants.SCREEN_WIDTH_DESIGN;
        return scaleValue;
    }

    protected int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

    private int screenHeight = 0;

    public int getScreenHeight() {
        if (screenHeight == 0) {
            int statusBarHeight = 0;
            try {
                int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getContext().getResources().getDimensionPixelSize(resourceId);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            screenHeight = getDisplayMetrics().heightPixels - statusBarHeight;
        }
        return screenHeight;
    }
}
