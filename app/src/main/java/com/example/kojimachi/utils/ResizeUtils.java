package jp.co.kojimachi.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import jp.co.kojimachi.constant.AppConstants;

public class ResizeUtils {
    private Context mContext;

    public ResizeUtils(Context mContext) {
        this.mContext = mContext;
    }

    //
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null && mContext != null)
            displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private int screenWidth = 0;

    public int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }


    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / AppConstants.SCREEN_WIDTH_DESIGN;
        return scaleValue;
    }

    public int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

    private int screenHeight = 0;

    public int getScreenHeight() {
        if (screenHeight == 0 && mContext != null) {
            int statusBarHeight = 0;
            try {
                int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            screenHeight = getDisplayMetrics().heightPixels - statusBarHeight;
        }
        return screenHeight;
    }
}
