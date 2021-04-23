package com.example.kojimachi.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.kojimachi.R;
import com.example.kojimachi.constant.AppConstants;
import com.example.kojimachi.listener.ListenerCheckGPS;
import com.example.kojimachi.listener.ListenerGPSChecking;
import com.example.kojimachi.services.ServiceTrackingLocation;
import com.example.kojimachi.utils.AppUtils;
import com.example.kojimachi.utils.DebugHelper;

import static com.example.kojimachi.constant.RequestConstants.RC_ENABLE_GPS;
import static com.example.kojimachi.constant.RequestConstants.RC_LOCATION_SETTINGS;
import static com.example.kojimachi.constant.RequestConstants.RC_WIRELESS_SETTINGS;

public class TrackingLocationManager {
    private static final String TAG = "TrackingLocationManager";
    private Activity mContext;
    private static com.example.kojimachi.location.TrackingLocationManager _instance;

    public static com.example.kojimachi.location.TrackingLocationManager getInstance(Activity context) {
        if (_instance == null) {
            _instance = new com.example.kojimachi.location.TrackingLocationManager(context);
        }
        return _instance;
    }

    private TrackingLocationManager(Activity mContext) {
        this.mContext = mContext;
    }

    //Start Service
    public void startTrackingLocation(ListenerCheckGPS gpsListener) {
        checkGpsService(new ListenerGPSChecking() {
            @Override
            public void onCanRequestEnableLocation(ApiException e) {
                DebugHelper.Log(TAG, "onCanRequestEnableLocation: ");
                requestEnableLocation(e);
            }

            @Override
            public void onNeedGotoSettingToEnableLocation() {
                DebugHelper.Log(TAG, "onNeedGotoSettingToEnableLocation: ");
                showDialogRequestEnableGps(gpsListener);
            }

            @Override
            public void onNeedNetwork() {
                DebugHelper.Log(TAG, "onNeedNetwork: ");
                showDialogRequestNetwork();
            }

            @Override
            public void onReadyToRequestLocation() {
                DebugHelper.Log(TAG, "onReadyToRequestLocation: ");
                startServiceTrackingLocation();
                gpsListener.onSuccess();
            }
        });
    }

    private boolean isTrackingLocation = false;

    public void startServiceTrackingLocation() {
        try {
            Intent intent = new Intent(mContext, ServiceTrackingLocation.class);
            intent.setAction(ServiceTrackingLocation.ACTION_START);
            mContext.startService(intent);
            isTrackingLocation = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToBackgroundMode() {
        try {
            if (!isTrackingLocation)
                return;
            Intent intent = new Intent(mContext, ServiceTrackingLocation.class);
            intent.setAction(ServiceTrackingLocation.ACTION_BACKGROUND_MODE);
            mContext.startService(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //Get Last Location
    public void getCurrentLocation() {
        try {
            Intent intent = new Intent(mContext, ServiceTrackingLocation.class);
            intent.setAction(ServiceTrackingLocation.ACTION_GET_CURRENT_LOCATION);
            mContext.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    public void checkGpsService(ListenerGPSChecking listener) {
        if (!AppUtils.isNetworkAvailable(mContext)) {
            listener.onNeedNetwork();
            return;
        }

        getSettingsClient().checkLocationSettings(getLocationSettingsRequest())
                .addOnSuccessListener(mContext, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        if (listener != null)
                            listener.onReadyToRequestLocation();
                    }
                })
                .addOnFailureListener(mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                if (AppConstants.LOG_DEBUG)
                                    Log.d(TAG, "onFailure>>>RESOLUTION_REQUIRED");
                                if (listener != null)
                                    listener.onCanRequestEnableLocation((ApiException) e);
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            default:
                                if (AppConstants.LOG_DEBUG)
                                    Log.d(TAG, "onFailure>>>SETTINGS_CHANGE_UNAVAILABLE");
                                if (listener != null)
                                    listener.onNeedGotoSettingToEnableLocation();
                        }
                    }
                });
    }

    //Setting
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    private SettingsClient getSettingsClient() {
        if (mSettingsClient == null)
            mSettingsClient = LocationServices.getSettingsClient(mContext);
        return mSettingsClient;
    }

    private LocationSettingsRequest getLocationSettingsRequest() {
        if (mLocationSettingsRequest == null) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(getLocationRequest());
            mLocationSettingsRequest = builder.build();
        }

        return mLocationSettingsRequest;
    }

    private LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        return mLocationRequest;
    }

    //Dialog
    private void showDialogRequestEnableGps(ListenerCheckGPS gpsListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(false);
            builder.setMessage(mContext.getResources().getString(R.string.lblRequestEnableGps));
            builder.setPositiveButton(mContext.getResources().getString(R.string.lblOK), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestEnableGPS();
                }
            });
            builder.setNegativeButton(mContext.getResources().getString(R.string.lblCancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gpsListener.onFailed();
                }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogRequestNetwork() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(false);
            builder.setMessage(mContext.getResources().getString(R.string.msgErrorNetwork));
            builder.setPositiveButton(mContext.getResources().getString(R.string.lblOK), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mContext.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), RC_WIRELESS_SETTINGS);
                }
            });
            builder.create();
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestEnableLocation(ApiException e) {
        try {
            ResolvableApiException rae = (ResolvableApiException) e;
            rae.startResolutionForResult(mContext, RC_ENABLE_GPS);
        } catch (IntentSender.SendIntentException sie) {
            if (AppConstants.LOG_DEBUG)
                Log.i("Tracking Location", "PendingIntent unable to execute request.");
        }
    }

    private void requestEnableGPS() {
        mContext.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), RC_LOCATION_SETTINGS);
    }
}
