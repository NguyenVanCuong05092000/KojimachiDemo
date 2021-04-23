package com.example.kojimachi.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.kojimachi.services.ServiceTrackingLocation;

import static com.example.kojimachi.constant.AppConstants.CURRENCY_FORMAT;

public class AppUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isTrackingLocationServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceTrackingLocation.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String versionName = "";
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * AndroidIDを取得
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        // メニューのID表示
        if (context == null) {
            return null;
        }
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void hideKeyboard(View view) {
        try {
            InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(EditText view) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        view.requestFocus();
                        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                        view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, 100);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static SimpleDateFormat simpleDateFormat;

    public static String parseDateToS3(Calendar calendar) {
        if (simpleDateFormat == null)
            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return calendar == null ? "" : simpleDateFormat.format(calendar.getTime());
    }

    public static int parseInt(String value) {
        int des = -1;
        try {
            des = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return des;
    }

    private static DecimalFormat formatterPrice;
    public static String formatPrice(String price) {
        String formattedString = "";
        try {
            long longVal;
            if (price.contains(",")) {
                price = price.replaceAll(",", "");
            }
            longVal = Long.parseLong(price);
            if (formatterPrice == null) {
                formatterPrice = (DecimalFormat) NumberFormat.getInstance(Locale.JAPANESE);
                formatterPrice.applyPattern(CURRENCY_FORMAT);
            }
            formattedString = formatterPrice.format(longVal);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return formattedString;
    }

}
