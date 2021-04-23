package com.example.kojimachi.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.RequestParams;

import com.example.kojimachi.R;
import com.example.kojimachi.app.MyApplication;
import com.example.kojimachi.constant.AppConstants;
import com.example.kojimachi.constant.IntentActionConstant;
import com.example.kojimachi.constant.NotificationConstants;
import com.example.kojimachi.constant.PrefConstants;
import com.example.kojimachi.data.PrefManager;
import com.example.kojimachi.entity.ApiResult;
import com.example.kojimachi.entity.prefentity.PrefFloat;
import com.example.kojimachi.listener.CallbackApi;
import com.example.kojimachi.network.ApiClient;
import com.example.kojimachi.secret.SecretHelper;
import com.example.kojimachi.utils.AppUtils;

import static com.example.kojimachi.constant.ApiConstants.ANDROID;
import static com.example.kojimachi.constant.ApiConstants.API_KEY;
import static com.example.kojimachi.constant.ApiConstants.API_TRACKING_LOCATION;
import static com.example.kojimachi.constant.ApiConstants.API_VERSION;
import static com.example.kojimachi.constant.ApiConstants.PARAM_API;
import static com.example.kojimachi.constant.ApiConstants.PARAM_API_VERSION;
import static com.example.kojimachi.constant.ApiConstants.PARAM_DEVICE_TYPE;
import static com.example.kojimachi.constant.ApiConstants.PARAM_DEVICE_UUID;
import static com.example.kojimachi.constant.ApiConstants.PARAM_LAT;
import static com.example.kojimachi.constant.ApiConstants.PARAM_LNG;
import static com.example.kojimachi.constant.ExtraConstants.EXTRA_LOCATION;

public class ServiceTrackingLocation extends Service implements IntentActionConstant {
    //Cmt
    private static final String TAG = "ServiceTrackingLocation";

    private static final long SMALLEST_DISPLACEMENT_REQUEST =
//            AppConstants.TEST_MODE ? 3 :
            50;
    private final long INTERVAL_BACKGROUND_MODE = 30 * 60000;
    private final long INTERVAL_FOREGROUND_MODE = 10000;

    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private boolean isTrackingLocation = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wakeLockCPU();
        if (intent != null) {
            String action = intent.getAction();
            if (AppConstants.LOG_DEBUG)
                Log.e(TAG, "onStartCommand->action->" + action + " ->isTrackingLocation:" + isTrackingLocation);
            if (ACTION_START.equals(action)) {
                if (!isTrackingLocation)
                    startLocationUpdates();
                else
                    switchModeTracking(false);
            } else if (ACTION_STOP.equals(action)) {
                stopSelf();
            } else if (ACTION_GET_CURRENT_LOCATION.equals(action)) {
                getLastLocation();
            } else if (ACTION_BACKGROUND_MODE.equals(action)) {
                switchModeTracking(true);
            }
        }
        return START_STICKY;

    }

    /**
     * Keep the CPU running in order to complete some work before the device goes to sleep.
     * https://developer.android.com/training/scheduling/wakelock
     */
    @SuppressLint("InvalidWakeLockTag")
    private void wakeLockCPU() {
        try {
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null) {
                wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPU");
                wl.acquire();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseWakeLockCPU() {
        try {
            if (wl != null) {
                wl.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWakeLockCPU();
        stopLocationUpdates();
        if (AppConstants.LOG_DEBUG) Log.e(TAG, "->onDestroy");
    }

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;

    private FusedLocationProviderClient getFusedLocationProviderClient() {
        if (mFusedLocationClient == null)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        return mFusedLocationClient;
    }

    private LocationCallback getLocationCallback() {
        if (mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
//                    if(AppConstants.LOG_DEBUG) Log.e(TAG, "onLocationResult->"+(location==null? "null" :
//                            "lat:"+location.getLatitude() + " - lng:"+location.getLongitude()));
                    if (location == null)
                        return;
                    //Update Location
                    sendBroadcast(new Intent().setAction(ACTION_UPDATE_LOCATION).putExtra(EXTRA_LOCATION, location));
                    checkDistanceLocation(location);
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                }
            };
        }
        return mLocationCallback;
    }

    private Location mCurrentLocation = null;

    private void checkDistanceLocation(@NonNull Location newLocation) {
        try {
            if (mCurrentLocation != null) {
//                if(AppConstants.LOG_DEBUG) Log.e(TAG, "checkDistanceLocation->"+mCurrentLocation.distanceTo(newLocation));
                if (mCurrentLocation.distanceTo(newLocation) < SMALLEST_DISPLACEMENT_REQUEST)
                    return;
            }

            callApiUpdateLocation(newLocation);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private SecretHelper secretHelper;

    private SecretHelper getSecretHelper() {
        if (secretHelper == null)
            secretHelper = new SecretHelper();
        return secretHelper;
    }

    private String apiKey;

    private String getApiKey(Context context) {
        if (TextUtils.isEmpty(apiKey))
            apiKey = getSecretHelper().getTextDecrypt(context, API_KEY, "API_KEY");
        return apiKey;
    }

    private void callApiUpdateLocation(Location location) {
        mCurrentLocation = location;
        Context context = MyApplication.getInstance().getApplicationContext();
        RequestParams params = new RequestParams();
        params.put(PARAM_API, getApiKey(context));
        params.put(PARAM_API_VERSION, API_VERSION);
        params.put(PARAM_LAT, location.getLatitude());
        params.put(PARAM_LNG, location.getLongitude());
        params.put(PARAM_DEVICE_TYPE, ANDROID);
        params.put(PARAM_DEVICE_UUID, AppUtils.getAndroidId(context));
        String mUserID = PrefManager.getInstance(context).getString(PrefConstants.USER_ID);
        if (!TextUtils.isEmpty(mUserID) && !mUserID.equals(AppConstants.UNKNOWN_USER_ID)) {
            ApiClient.requestApi(context,
                    String.format(API_TRACKING_LOCATION, mUserID), true, params, new CallbackApi() {
                        @Override
                        public void onFinished(ApiResult result) {
                            if (AppConstants.LOG_DEBUG)
                                Log.e(TAG, "callApiUpdateLocation.onFinished: " + result.toString());
                            //Todo something
                        }
                    });
        }
        PrefManager.getInstance(context)
                .writeValues(new PrefFloat(PrefManager.LAST_KNOWN_LAT, (float) location.getLatitude()),
                        new PrefFloat(PrefManager.LAST_KNOWN_LNG, (float) location.getLongitude()));
    }

    private void getLastLocation() {
        getFusedLocationProviderClient().getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Update Location
                sendBroadcast(new Intent().setAction(ACTION_UPDATE_LOCATION).putExtra(EXTRA_LOCATION, location));
            }
        });
    }

    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL_FOREGROUND_MODE);
            mLocationRequest.setFastestInterval(1000L);
//            mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT_REQUEST);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        return mLocationRequest;
    }

    private boolean isBackgroundMode = false;

    private void switchModeTracking(boolean isBackground) {
        try {
            if (!isTrackingLocation || mLocationRequest == null)
                return;
            if (isBackgroundMode)
                mCurrentLocation = null;

            isBackgroundMode = isBackground;
            mLocationRequest.setInterval(isBackground ? INTERVAL_BACKGROUND_MODE
                    : INTERVAL_FOREGROUND_MODE);
            mLocationRequest.setFastestInterval(isBackground ? INTERVAL_BACKGROUND_MODE : 1000L);

            getFusedLocationProviderClient()
                    .requestLocationUpdates(getLocationRequest(), getLocationCallback(), Looper.myLooper());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private LocationSettingsRequest getLocationSettingsRequest() {
        if (mLocationSettingsRequest == null) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(getLocationRequest());
            mLocationSettingsRequest = builder.build();
        }

        return mLocationSettingsRequest;
    }

    private void startLocationUpdates() {
        try {
            mCurrentLocation = null;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            isTrackingLocation = true;
            startNotificationForeground();
            getFusedLocationProviderClient()
                    .requestLocationUpdates(getLocationRequest(), getLocationCallback(), Looper.myLooper());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        try {
            isTrackingLocation = false;
            if (mLocationCallback != null)
                getFusedLocationProviderClient().removeLocationUpdates(mLocationCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startNotificationForeground() {
        NotificationCompat.Builder notiBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = getResources().getString(R.string.default_notification_channel_id);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    getResources().getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Tracking location");
            mNotificationManager.createNotificationChannel(notificationChannel);

            notiBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("TRACKING LOCATION")
                    .setAutoCancel(false);
        } else {
            notiBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("TRACKING LOCATION")
                    .setPriority(Notification.PRIORITY_MIN)
                    .setAutoCancel(false);
        }

        Notification notification1 = notiBuilder.build();
        startForeground(NotificationConstants.ID_LOCATION_TRACKING, notification1);
    }

    private boolean needPermissionFineLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}
