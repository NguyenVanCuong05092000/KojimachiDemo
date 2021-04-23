package com.example.kojimachi.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.kojimachi.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.constant.EnumUrl;
import jp.co.kojimachi.constant.IntentActionConstant;
import jp.co.kojimachi.constant.PrefConstants;
import jp.co.kojimachi.constant.RequestConstants;
import jp.co.kojimachi.data.PrefManager;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityBookingConfirmation;
import jp.co.kojimachi.entity.EntityCar;
import jp.co.kojimachi.entity.EntityDailyActuarial;
import jp.co.kojimachi.entity.EntityDailyNewspaper;
import jp.co.kojimachi.entity.EntitySchedule;
import jp.co.kojimachi.entity.EntityShiftTable;
import jp.co.kojimachi.entity.EntityTherapistSchedule;
import jp.co.kojimachi.entity.EntityUpdateOfLinen;
import jp.co.kojimachi.entity.EntityUpdateOption;
import jp.co.kojimachi.entity.api.LoginResponseEntity;
import jp.co.kojimachi.entity.prefentity.PrefBoolean;
import jp.co.kojimachi.entity.prefentity.PrefInt;
import jp.co.kojimachi.entity.prefentity.PrefString;
import jp.co.kojimachi.fragment.BaseFragment;
import jp.co.kojimachi.fragment.FrmBookingConfirmation;
import jp.co.kojimachi.fragment.FrmDailyActuarial;
import jp.co.kojimachi.fragment.FrmDailyNewspaper;
import jp.co.kojimachi.fragment.FrmHome;
import jp.co.kojimachi.fragment.FrmHomeDriver;
import jp.co.kojimachi.fragment.FrmLogin;
import jp.co.kojimachi.fragment.FrmMap;
import jp.co.kojimachi.fragment.FrmNumberOfLinen;
import jp.co.kojimachi.fragment.FrmSalesHistory;
import jp.co.kojimachi.fragment.FrmShiftTable;
import jp.co.kojimachi.fragment.FrmVehicleSettings;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.listener.CallbackGetDetailActuarial;
import jp.co.kojimachi.listener.CallbackGetDetailNewspaper;
import jp.co.kojimachi.listener.CallbackGetDetailSchedule;
import jp.co.kojimachi.listener.CallbackGetListCar;
import jp.co.kojimachi.listener.CallbackGetSchedule;
import jp.co.kojimachi.listener.CallbackGetTherapistSchedule;
import jp.co.kojimachi.listener.ListenerCheckGPS;
import jp.co.kojimachi.listener.ListenerGPSChecking;
import jp.co.kojimachi.listener.ListenerHandleResult;
import jp.co.kojimachi.listener.ListenerUpdateStatusShift;
import jp.co.kojimachi.location.TrackingLocationManager;
import jp.co.kojimachi.network.APIHelper;
import jp.co.kojimachi.services.ServiceTrackingLocation;
import me.leolin.shortcutbadger.ShortcutBadger;

import static jp.co.kojimachi.constant.AppConstants.ID_DRIVER;
import static jp.co.kojimachi.constant.AppConstants.ID_TECHNICIAN;
import static jp.co.kojimachi.constant.IntentActionConstant.ACTION_PUSH_NEWS;
import static jp.co.kojimachi.constant.NotificationConstants.NOTIFICATION_DATA_TRANSFER;
import static jp.co.kojimachi.constant.PrefConstants.CAR_NUMBER;
import static jp.co.kojimachi.constant.PrefConstants.CREATE_AT;
import static jp.co.kojimachi.constant.PrefConstants.ID_CAR;
import static jp.co.kojimachi.constant.PrefConstants.IS_WORK_TODAY;
import static jp.co.kojimachi.constant.PrefConstants.NAME;
import static jp.co.kojimachi.constant.PrefConstants.PHONE;
import static jp.co.kojimachi.constant.PrefConstants.ROLE;
import static jp.co.kojimachi.constant.PrefConstants.SKIP_TUT_LOCATION;
import static jp.co.kojimachi.constant.PrefConstants.TOKEN;
import static jp.co.kojimachi.constant.PrefConstants.USER_ID;

public class ActMain extends BaseActivity {

    private final String TAG = "ActMain";
    private ActMain _activity;
    private int currentFragment;
    private int user_id = 0;

    public void setCurrentScreen(int currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void addFragmentToMain(Fragment f) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameMenuContainer);
            if (currentFragment != null) {
                fragmentManager.beginTransaction()
                        .remove(currentFragment)
                        .commitAllowingStateLoss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        addOrReplaceFragment(R.id.frameParent, f);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        clearBadgeNotification("onCreate");
        View imgHeader = findViewById(R.id.imgHeader);
        imgHeader.getLayoutParams().width = getSizeWithScale(375);
        imgHeader.getLayoutParams().height = getSizeWithScale(177);
        registerReceiver();
        navigationApp();
    }

    private void clearBadgeNotification(String from) {
        try {
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "FirebaseMessaging.clearBadge.from >>> " + from);
            prefWriteInt(PrefManager.NOTIFICATION_COUNT, 0);
            ShortcutBadger.removeCount(this);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void navigationApp() {
        currentToken = prefGetString(PrefConstants.CURRENT_TOKEN);
        int currentRole = prefGetInt(PrefConstants.ROLE);
        int currentUserId = prefGetInt(USER_ID);

        if (TextUtils.isEmpty(currentToken) || currentUserId == 0 || currentRole == 0) {
            addFragmentToMain(new FrmLogin());
        } else {
            Bundle bundle = getPushData(getIntent(), "onLoginSuccess");
            if (bundle != null)
                handlePushData(bundle);
            else {
                if (getIdSchedule() > 0) {
                    HashMap<String, Object> mapData = new HashMap<>();
                    mapData.put(EXTRA_ID, getIdSchedule());
                    addFragmentToMain(FrmBookingConfirmation.getInstance(new ArrayList<>(), mapData));
                } else if (currentRole == ID_TECHNICIAN)
                    addFragmentToMain(FrmHome.getInstance(null));
                else if (currentRole == ID_DRIVER) {
                    addFragmentToMain(FrmHomeDriver.getInstance(null));
                }
            }
//            registerFibTokenPush(_userId, corporatesId);
        }

    }

    public Bundle getPushData(Intent intent, String from) {
        if (intent != null && intent.hasExtra(NOTIFICATION_DATA_TRANSFER)) {
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "hasPushData." + from);
            Bundle bundle = intent.getBundleExtra(NOTIFICATION_DATA_TRANSFER);
            intent.removeExtra(NOTIFICATION_DATA_TRANSFER);
            return bundle;
        }
        return null;
    }

    public void handlePushData(Bundle bundle) {
        try {

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (AppConstants.LOG_DEBUG) Log.e(TAG, "onNewIntent");
        clearBadgeNotification("onNewIntent");
        if (currentFragment != FRM_LOGIN) {
            Bundle bundle = getPushData(intent, "onNewIntent");
            if (bundle != null)
                handlePushData(bundle);
        } else
            setIntent(intent);
    }

    public boolean isCallingApi = false;

    private String apiKey;

    private String getApiKey() {
        if (TextUtils.isEmpty(apiKey))
            apiKey = getTextDecrypt(API_KEY, "API_KEY");
        return apiKey;
    }

    private HashMap<String, String> getRequestParam() {
        HashMap<String, String> params = new HashMap<>();
//        params.put(PARAM_API, getApiKey());
        params.put(PARAM_API_VERSION, API_VERSION + "");
//        params.put(PARAM_DEVICE_UUID, AppUtils.getAndroidId(ActMain.this));
//        params.put(PARAM_DEVICE_TYPE, ANDROID);
        return params;
    }

    private HashMap<String, String> getRequestParamWithToken() {
        HashMap<String, String> params = getRequestParam();
        if (!TextUtils.isEmpty(getCurrentToken()))
            params.put(PARAM_TOKEN, currentToken);
        return params;
    }

    private void handleApiResult(ApiResult result, String tag, ListenerHandleResult callback) {
        if (AppConstants.LOG_DEBUG) Log.e(tag, "result->" + result.toString());
        hideProgress();
        isCallingApi = false;
        if (result.statusCode == STATUS_OK) {
            if (callback != null) {
                callback.onSuccess(result.response);
                return;
            }
        }
        showError(result);
        if (callback != null)
            callback.onFailed();
    }

    private void showError(ApiResult result) {
        if (result.statusCode == STATUS_ERROR_TOKEN) {
            showNotice(result.message, false, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    releaseDataAndShowLogin();
                }
            });
        } else if (result.statusCode == STATUS_ERROR_INTERNET)
            showNotice(R.string.msg_error_internet, true, null);
        else if (!TextUtils.isEmpty(result.message))
            showNotice(result.message, true, null);
        else
            showNotice(R.string.msg_error_server, true, null);
    }

    //======================================
    public int getCurrentUserId() {
        return prefGetInt(USER_ID, 0);
    }

    public int getCurrentRole() {
        return prefGetInt(ROLE, 0);
    }

    private String currentName = null;
    private String currentPassword = null;
    private String currentToken = null;
    private String currentUserName = null;
    private String currentPhone = null;
    private String currentCreateAt = null;

    public String getName() {
        if (TextUtils.isEmpty(currentName))
            currentName = prefGetString(PrefConstants.EMAIL);
        return currentName;
    }

    public void updateName(String name) {
        currentName = name;
        prefWriteString(PrefConstants.EMAIL, name);
    }

    public String getPassword() {
        if (TextUtils.isEmpty(currentPassword))
            currentPassword = prefGetString(PrefConstants.PASSWORD);
        return currentPassword;
    }

    public void updatePassword(String password) {
        currentPassword = password;
        prefWriteString(PrefConstants.PASSWORD, password);
    }

    public String getCurrentToken() {
        if (TextUtils.isEmpty(currentToken))
            currentToken = prefGetString(PrefConstants.CURRENT_TOKEN);
        return currentToken;
    }

    public void updateToken(String token, String from) {
        if (AppConstants.LOG_DEBUG) Log.e("updateToken." + from, currentToken + "->" + token);
        currentToken = token;
        prefWriteString(PrefConstants.CURRENT_TOKEN, token);
    }

    public String getCurrentUserName() {
        if (TextUtils.isEmpty(currentUserName))
            currentUserName = prefGetString(NAME);
        return currentUserName;
    }

    public String getCurrentPhone() {
        if (TextUtils.isEmpty(currentPhone))
            currentPhone = prefGetString(PrefConstants.PHONE);
        return currentPhone;
    }

    public String getCurrentCreateAt() {
        try {
            if (TextUtils.isEmpty(currentCreateAt)) currentCreateAt = prefGetString(CREATE_AT);
            return currentCreateAt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO Api

    public void login(String name, String password) {
        HashMap<String, String> params = getRequestParam();
        params.put(PARAM_LOGIN_ID, name);
        params.put(PARAM_PASSWORD, password);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_LOGIN, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response instanceof JSONObject) {
                            JSONObject jsResponse = (JSONObject) response;
                            onLoginSuccess(new LoginResponseEntity(
                                    jsResponse.optInt(ApiConstants.PARAM_ID),
                                    jsResponse.optString(ApiConstants.PARAM_LOGIN_ID),
                                    jsResponse.optInt(ApiConstants.PARAM_USER_ID),
                                    jsResponse.optInt(ApiConstants.PARAM_ROLE),
                                    jsResponse.optInt(ApiConstants.PARAM_EMPLOYMENT_STATUS),
                                    jsResponse.optString(ApiConstants.PARAM_MODIFIED),
                                    result.token,
                                    jsResponse.optString(ApiConstants.PARAM_NAME),
                                    jsResponse.optString(ApiConstants.PARAM_PHONE),
                                    jsResponse.optString(ApiConstants.PARAM_CREATE_AT),
                                    jsResponse.optJSONObject(ApiConstants.PARAM_DATA_SHIFTS),
                                    jsResponse.optJSONArray(ApiConstants.PARAM_LIST_CARS)));
                        }
                    }
                });
            }
        });
    }

    public void onLoginSuccess(LoginResponseEntity result) { // only call this function from login screen, if reuse in other case need add more check exception first same as login screen
        prefWriteArr(
                new PrefString(PrefConstants.CURRENT_TOKEN, result.accessToken),
                new PrefString(PrefConstants.LOGIN_ID, result.loginId),
                new PrefInt(USER_ID, result.userId),
                new PrefInt(PrefConstants.ROLE, result.role),
                new PrefString(NAME, result.userName),
                new PrefString(PHONE, result.phone),
                new PrefString(CREATE_AT, result.createAt),
                new PrefBoolean(IS_WORK_TODAY, result.dataShifts != null));
        switch (result.role) {
            case ID_TECHNICIAN:
                backToHome();
                break;
            case ID_DRIVER:
                HashMap<String, Object> mapData = new HashMap<>();
                ArrayList<EntityCar> entityCars = new ArrayList<>();
                if (result.listCars != null) {
                    try {
                        JSONArray jsonArray = result.listCars;
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                EntityCar entityCar = new EntityCar(jsonArray.getJSONObject(i));
                                entityCars.add(entityCar);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mapData.put(EXTRA_LIST_CARS, entityCars);
                showHomeDriver(new ArrayList<>(), mapData);
                break;
        }
    }

    public void logout() {
        isCallingApi = true;
        showProgress(false);
        HashMap<String, String> params = getRequestParamWithToken();
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_LOGOUT, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (AppConstants.LOG_DEBUG) Log.e("API_LOGOUT", "result->" + result.toString());
                handleApiResult(result, "logout", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        releaseDataAndShowLogin();
                    }
                });
            }
        });
    }

    private boolean isCallingApiGetListSchedule = false;

    public void getListSchedule(String day, boolean showProgress, CallbackGetSchedule listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_DAY, day);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_LIST_SCHEDULE, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                if (isCallingApiGetListSchedule)
                    return;
                isCallingApiGetListSchedule = true;
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                isCallingApiGetListSchedule = false;
                handleApiResult(result, "API_GET_LIST_SCHEDULE", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (response instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) response;
                                if (!jsonObject.isNull(PARAM_LIST_RESERVE)) {
                                    JSONArray listReserve = new JSONArray(jsonObject.optString(ApiConstants.PARAM_LIST_RESERVE));
                                    String totalPrice = jsonObject.optString(ApiConstants.PARAM_TOTAL_PRICE);
                                    if (TextUtils.isEmpty(totalPrice) || totalPrice.equals("null"))
                                        totalPrice = "";
                                    int therapistStatus = jsonObject.optInt(ApiConstants.PARAM_THERAPIST_STATUS);
                                    ArrayList<EntitySchedule> entitySchedules = new ArrayList<>();
                                    if (listReserve.length() > 0) {
                                        for (int i = 0; i < listReserve.length(); i++) {
                                            EntitySchedule entitySchedule = new EntitySchedule(listReserve.getJSONObject(i), getTimeFormat(), getTmpCalendar());
                                            entitySchedules.add(entitySchedule);
                                        }
                                    }
                                    if (listener != null) {
                                        listener.onSuccessGetListSchedule(entitySchedules, totalPrice, therapistStatus);
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.onSuccessGetListSchedule(null, "", 0);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        if (listener != null)
                            listener.onFailedGetSchedule();
                    }
                });
            }
        });
    }

    public void getListScheduleDriver(boolean showProgress, CallbackGetSchedule listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_LIST_SCHEDULE, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (isCallingApiGetListSchedule)
                    return;
                isCallingApiGetListSchedule = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                isCallingApiGetListSchedule = false;
                handleApiResult(result, "API_GET_LIST_SCHEDULE", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (response instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) response;
                                ArrayList<EntitySchedule> entitySchedules = new ArrayList<>();
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        EntitySchedule entitySchedule = new EntitySchedule(jsonArray.getJSONObject(i), getTimeFormat(), getTmpCalendar());
                                        entitySchedules.add(entitySchedule);
                                    }
                                }
                                if (listener != null) {
                                    listener.onSuccessGetListSchedule(entitySchedules, "", 0);
                                    hideProgress();
                                }
                            } else {
                                if (listener != null) {
                                    listener.onSuccessGetListSchedule(null, "", 0);
                                    hideProgress();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getDetailSchedule(int idSchedule, boolean showProgress, CallbackGetDetailSchedule listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_SCHEDULE, idSchedule + "");
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_DETAIL_SCHEDULE, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_DETAIL_SCHEDULE", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (response instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) response;
                                EntityBookingConfirmation entity = new EntityBookingConfirmation(jsonObject, getTmpCalendar());
                                entity.scheduleId = idSchedule;
                                if (listener != null) {
                                    listener.onSuccessGetDetailSchedule(entity);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        if (listener != null) {
                            listener.onFailedGetSchedule();
                        }
                    }
                });

            }
        });
    }

    public void updateOption(int id, ArrayList<EntityUpdateOption> arr_option, ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_SCHEDULE, id + "");
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < arr_option.size(); i++) {
                EntityUpdateOption entityUpdateOption = arr_option.get(i);
                if (entityUpdateOption.number != 0 && entityUpdateOption.id != 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(PARAM_ID, entityUpdateOption.id);
                    jsonObject.put(PARAM_NUMBER, entityUpdateOption.number);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(PARAM_ARR_OPTION, String.valueOf(jsonArray));
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_UPDATE_OPTION, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) {
                    handleApiResult(result, "API_UPDATE_OPTION", listener);
                }
            }
        });
    }

    public void updateStatusDetail(int id, int status, ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_SCHEDULE, id + "");
        params.put(PARAM_STATUS, status + "");
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_UPDATE_STATUS, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) handleApiResult(result, "API_UPDATE_STATUS", listener);
            }
        });
    }

    private void updateStatusShift(int status, ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_THERAPIST_STATUS, status + "");
        APIHelper.getInstance().callAPI(MAX_RETRY, EnumUrl.ENUM_API_UPDATE_STATUS_SHIFT, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) {
                    handleApiResult(result, "API_UPDATE_STATUS_SHIFT", listener);
                }
            }
        });
    }

    public void updateStatusShift(int status, ListenerUpdateStatusShift updateStatusShift) {
        updateStatusShift(status, new ListenerHandleResult() {
            @Override
            public void onSuccess(Object response) {
                if (updateStatusShift != null) {
                    updateStatusShift.onUpdateStatusShift();
                }
            }
        });
    }

    public void updateStatusCar(String idTranfers, int status, CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_TRANFERS, idTranfers);
        params.put(PARAM_STATUS, status + "");
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_UPDATE_STATUS_CAR, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_UPDATE_STATUS_CAR", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        if (listener != null)
                            listener.onFinished(result);
                    }
                });
            }
        });
    }

    public void getListOptionLinen(ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_OPTION_LINEN, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) {
                    handleApiResult(result, "API_GET_OPTION_LINEN", listener);
                }
            }
        });
    }

    public void updateNumberOfLinen(int idReserveCourse, ArrayList<EntityUpdateOfLinen> updateOfLinenArrayList, ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        if (getCurrentRole() == ID_DRIVER) {
            params.put(PARAM_ID_DATA_COURSE, "null");
        } else params.put(PARAM_ID_DATA_COURSE, idReserveCourse + "");

        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < updateOfLinenArrayList.size(); i++) {
                EntityUpdateOfLinen entityUpdateOfLinen = updateOfLinenArrayList.get(i);
                if (entityUpdateOfLinen.number != 0 && entityUpdateOfLinen.idOption != 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(PARAM_OPTION, entityUpdateOfLinen.idOption);
                    jsonObject.put(PARAM_NUMBER, entityUpdateOfLinen.number);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(PARAM_DATA, String.valueOf(jsonArray));
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_UPDATE_NUMBER_OF_LINEN, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) {
                    handleApiResult(result, "updateNumberOfLinen", listener);
                }
            }
        });
    }

    public void getTherapistSchedule(int year, int month, boolean showProgress, CallbackGetTherapistSchedule listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_YEAR, year + "");
        params.put(PARAM_MONTH, month + "");
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_THERAPIST_SCHEDULE, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_THERAPIST_SCHEDULE", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        if (response instanceof JSONArray) {
                            try {
                                JSONArray jsonArray = (JSONArray) response;
                                ArrayList<EntityTherapistSchedule> entityTherapistSchedules = new ArrayList<>();
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        EntityTherapistSchedule entityTherapistSchedule = new EntityTherapistSchedule(jsonArray.getJSONObject(i), getDateFormat(), getTmpCalendar());
                                        entityTherapistSchedules.add(entityTherapistSchedule);
                                    }
                                }
                                if (listener != null) {
                                    listener.onSuccessGetTherapistSchedule(entityTherapistSchedules);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailed() {
                        if (listener != null) {
                            listener.onFailedGetTherapistSchedule();
                        }
                    }
                });
            }
        });
    }

    public void getListArea(CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_LIST_AREA, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (AppConstants.LOG_DEBUG)
                    Log.e("API_GET_LIST_AREA", "result->" + result.toString());
                try {
                    if (listener != null) {
                        listener.onFinished(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void vehicleSettings(int idCar, String name, String phoneNumber, boolean showProgress, CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_ID_CAR, idCar + "");
        params.put(PARAM_NAME, name);
        params.put(PARAM_PHONE, phoneNumber);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_VEHICLE_SETTINGS, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_VEHICLE_SETTINGS", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (listener != null) {
                                listener.onFinished(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        try {
                            if (listener != null) {
                                listener.onFinished(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getListCar(boolean showProgress, CallbackGetListCar listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_LIST_CAR, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_LIST_CAR", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            ArrayList<EntityCar> entityCars = new ArrayList<>();
                            if (result.response instanceof JSONArray) {
                                try {
                                    JSONArray jsonArray = (JSONArray) result.response;
                                    if (jsonArray.length() > 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            EntityCar entityCar = new EntityCar(jsonArray.getJSONObject(i));
                                            entityCars.add(entityCar);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (listener != null) {
                                listener.onGetListCarSuccess(entityCars);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void parseUserDataAfterLogin(String responseData) {
        if (TextUtils.isEmpty(responseData)) return;
        //
        try {
            JSONObject jsonObjUserData = new JSONObject(responseData);
            user_id = jsonObjUserData.optInt(PARAM_USER_ID, 0);
            String corporatesId = jsonObjUserData.optString(PARAM_CORPORATES_ID, "");
            prefWriteArr(new PrefInt(USER_ID, user_id),
                    new PrefString(PrefConstants.CORPORATES_ID, corporatesId));
            if (user_id != 0) {
                // register firebase token for current user
                registerFibTokenPush(user_id, corporatesId);
                prefWriteInt(USER_ID, user_id);
            }
            // more handle when cannot get user-id here if need
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerFibTokenPush(int id, String corporatesId) { // valid userId before call this function
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            try {
                if (!task.isSuccessful()) {
                    // more handle for firebase-token-invalid here if need
                    if (AppConstants.LOG_DEBUG)
                        Log.d(getClassName(), "registerFibTokenPush >>> getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult().getToken();
                if (AppConstants.LOG_DEBUG)
                    Log.d(getClassName(), "registerFibTokenPush >>> currentFibToken : " + token);
                HashMap<String, String> params = getRequestParam();
                params.put(PARAM_FIREBASE_TOKEN, token);
                params.put(PARAM_CORPORATES_ID, corporatesId);
                APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_LIST_CAR, params, new APIHelper.OnStartCallAPIListener() {
                    @Override
                    public void onStart() {
                        isCallingApi = true;
                        showProgress(false);
                    }
                }, new APIHelper.OnFinishCallAPIListener() {
                    @Override
                    public void onFinished(ApiResult result) {
                        if (AppConstants.LOG_DEBUG)
                            Log.e(getClassName(), "registerFibTokenPush >>> result : " + result.toString());
                        if (result.statusCode == ApiConstants.STATUS_OK) {
                            prefWriteString(TOKEN, token);
                            // more handle when register firebase-token success if need
                            return;
                        }
                        // more handle when register firebase-token failed if need
                    }
                });
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void registeredToWork(ArrayList<EntityShiftTable> shiftTableArrayList, boolean showProgress, CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        JSONArray jsonArray = new JSONArray();
        try {
            for (EntityShiftTable entityShiftTable : shiftTableArrayList) {
                if ((entityShiftTable.attendanceStatus.equals("出勤")
                        && !TextUtils.isEmpty(entityShiftTable.leaveTime)
                        && !TextUtils.isEmpty(entityShiftTable.attendanceTime)
                        && !TextUtils.isEmpty(entityShiftTable.standby))
                        || entityShiftTable.paidHoliday == 1) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(PARAM_USER_ID, getCurrentUserId());
                    jsonObject.put(PARAM_YEAR, entityShiftTable.year);
                    jsonObject.put(PARAM_MONTH, (entityShiftTable.month + 1));
                    jsonObject.put(PARAM_DAY, entityShiftTable.date);
                    jsonObject.put(PARAM_ID_AREA, entityShiftTable.idArea);
                    jsonObject.put(PARAM_WORK_START, entityShiftTable.attendanceTime);
                    jsonObject.put(PARAM_WORK_END, entityShiftTable.leaveTime);
                    if (entityShiftTable.paidHoliday == 1)
                        jsonObject.put(PARAM_PAID_HOLIDAY, entityShiftTable.paidHoliday);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(PARAM_DATA, String.valueOf(jsonArray));
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_POST_THERAPIST_SCHEDULE, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress) {
                    showProgress(false);
                }
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_POST_THERAPIST_SCHEDULE", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (listener != null) {
                                listener.onFinished(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getDailyNewspaper(String scheduleId, boolean showProgress, CallbackGetDetailNewspaper listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_SCHEDULE, scheduleId);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_DAILY_NEWSPAPER_GET, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress)
                    showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_DAILY_NEWSPAPER_GET", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (result.response != null) {
                                JSONObject jsResponse = (JSONObject) result.response;
                                EntityDailyNewspaper entityDailyNewspaper = new EntityDailyNewspaper(jsResponse, getTmpCalendar());
                                if (listener != null) {
                                    listener.onSuccessGetDetailNewspaper(entityDailyNewspaper);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onFailedGetDetailNewspaper();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        if (listener != null) {
                            listener.onFailedGetDetailNewspaper();
                        }
                    }
                });
            }
        });
    }

    public void updateDailyNewspaper(String scheduleId, int status, String content, boolean showProgress, CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_ID_SCHEDULE, scheduleId);
        params.put(PARAM_STATUS, status + "");
        params.put(PARAM_CONTENT, content);

        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_DAILY_NEWSPAPER_POST, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress)
                    showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_DAILY_NEWSPAPER_POST", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (listener != null) {
                                listener.onFinished(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getDailyActuarial(String date, boolean showProgress, CallbackGetDetailActuarial listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_DAY, date);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_DAILY_ACTUARIAL_GET, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress)
                    showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                handleApiResult(result, "API_GET_DAILY_ACTUARIAL_GET", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (result.response != null) {
                                JSONObject jsResponse = (JSONObject) result.response;
                                EntityDailyActuarial entityDailyActuarial = new EntityDailyActuarial(jsResponse);
                                if (listener != null) {
                                    listener.onSuccessGetDetailActuarial(entityDailyActuarial);
                                }
                            } else {
                                if (listener != null) {
                                    listener.onFailedGetDetailActuarial();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed() {
                        if (listener != null) {
                            listener.onFailedGetDetailActuarial();
                        }
                    }
                });
            }
        });
    }

    public void updateDailyActuarial(EntityDailyActuarial entityDailyActuarial, boolean showProgress, CallbackApi listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_USER_ID, getCurrentUserId() + "");
        params.put(PARAM_INSUFFICIENT_CASH, entityDailyActuarial.actualMoney);
        params.put(PARAM_ACTUAL_CASH_ON_HAND, entityDailyActuarial.excessMoney);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_GET_DAILY_ACTUARIAL_POST, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                if (showProgress)
                    showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {

                handleApiResult(result, "API_GET_DAILY_ACTUARIAL_POST", new ListenerHandleResult() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            if (listener != null) {
                                listener.onFinished(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getDataViewSales(String month, String year, ListenerHandleResult listener) {
        HashMap<String, String> params = getRequestParamWithToken();
        params.put(PARAM_MONTH, month);
        params.put(PARAM_YEAR, year);
        APIHelper.getInstance().callAPI(EnumUrl.ENUM_API_VIEW_SALES, params, new APIHelper.OnStartCallAPIListener() {
            @Override
            public void onStart() {
                isCallingApi = true;
                showProgress(false);
            }
        }, new APIHelper.OnFinishCallAPIListener() {
            @Override
            public void onFinished(ApiResult result) {
                if (listener != null) {
                    handleApiResult(result, "API_VIEW_SALES", listener);
                }
            }
        });
    }

    //TODO Data Manager
    public void releaseDataAndShowLogin() {
        releaseAllData();
        addFragmentToMain(FrmLogin.getInstance());
    }

    public void releaseAllData() {
        closeAllDialog();
        currentName = null;
        currentToken = null;
        currentUserName = null;
        currentPhone = null;
        currentCreateAt = null;
        user_id = 0;
        prefWriteArr(
                new PrefString(PrefConstants.CURRENT_TOKEN, currentToken),
                new PrefString(PrefConstants.LOGIN_ID, currentName),
                new PrefInt(USER_ID, user_id),
                new PrefBoolean(PrefConstants.IS_WORKING + "_" + getCurrentUserId(), false),
                new PrefString(CAR_NUMBER, ""),
                new PrefString(NAME, currentUserName),
                new PrefInt(ID_CAR, 0),
                new PrefString(PHONE, currentPhone));
    }

    public int getIdSchedule() {
        return PrefManager.getInstance(this).getInt(PrefConstants.ID_SCHEDULE + "_" + getCurrentUserId());
    }

    //TODO handle back press
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment menuFragment = fragmentManager.findFragmentById(R.id.frameMenuContainer);
                    if (menuFragment != null && menuFragment instanceof BaseFragment) {
                        if (((BaseFragment) menuFragment).isBackPreviousEnable()) {
                            ((BaseFragment) menuFragment).backToPrevious();
                            return true;
                        }
                    } else {
                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameParent);
                        if (currentFragment != null && currentFragment instanceof BaseFragment) {
                            if (((BaseFragment) currentFragment).isBackPreviousEnable()) {
                                ((BaseFragment) currentFragment).backToPrevious();
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                onBackPressed();
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        return false;
    }

//TODO show fragment

    public void showShiftTable(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmShiftTable.getInstance(arrayList, mapData));
    }

    public void showDailyActuarial(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmDailyActuarial.getInstance(arrayList, mapData));
    }

    public void showViewSales(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmSalesHistory.getInstance(arrayList, mapData));
    }

    public void showDailyNewspaper(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmDailyNewspaper.getInstance(arrayList, mapData));
    }

    public void showBookingConfirmation(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmBookingConfirmation.getInstance(arrayList, mapData));
    }

    public void showNumberOfLinen(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmNumberOfLinen.getInstance(arrayList, mapData));
    }

    public void showVehicleSettings(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmVehicleSettings.getInstance(arrayList, mapData));
    }

    public void showHomeDriver(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmHomeDriver.getInstance(arrayList, mapData));
    }

    public void showMap(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        addFragmentToMain(FrmMap.getInstance(arrayList, mapData));
    }

    public void backToHome() {
        addFragmentToMain(FrmHome.getInstance(null));
    }

    public void backToHomeDriver() {
        addFragmentToMain(FrmHomeDriver.getInstance(null));
    }

    public void backToPreviousFromBackStack(Bundle arguments) {
        try {
            if (arguments != null && arguments.containsKey(EXTRA_BACK_STACK)) {
                backToPreviousFromBackStack((ArrayList<BackStackData>) arguments.getSerializable(EXTRA_BACK_STACK));
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        addFragmentToMain(FrmHome.getInstance(null));
    }

    public void backToPreviousFromBackStack(ArrayList<BackStackData> arrayList) {
        try {
            if (arrayList != null && !arrayList.isEmpty()) {
                BackStackData data = arrayList.remove(arrayList.size() - 1);
                BaseFragment fragment;
                switch (data.fromFragment) {
                    case FRM_BOOKING_CONFIRMATION:
                        fragment = FrmBookingConfirmation.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_SHIFT_TABLE:
                        fragment = FrmShiftTable.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_DAILY_ACTUARIAL:
                        fragment = FrmDailyActuarial.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_VIEW_SALES:
                        fragment = FrmSalesHistory.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_DAILY_NEWSPAPER:
                        fragment = FrmDailyNewspaper.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_NUMBER_OF_LINE:
                        fragment = FrmNumberOfLinen.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_VEHICLE_SETTINGS:
                        fragment = FrmVehicleSettings.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_HOME_DRIVER:
                        fragment = FrmHomeDriver.getInstance(arrayList, data.mapData);
                        break;
                    case FRM_MAP:
                        fragment = FrmMap.getInstance(arrayList, data.mapData);
                        break;
                    default:
                        fragment = FrmHome.getInstanceWithBackStackData(data.mapData);
                        break;
                }
                addFragmentToMain(fragment);
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        addFragmentToMain(FrmHome.getInstance(null));
    }

//TODO check permissions

    @Override
    protected void onResume() {
        super.onResume();
        _activity = this;
    }

    private boolean isRequestingPermission = false;
    private boolean isRequestingPermissionLocation = false;

    public void requestPermissions(String from) {
        if (AppConstants.LOG_DEBUG)
            Log.e("requestPermissions", from + "-isRequestingPermission:" + isRequestingPermission);
        if (isRequestingPermission)
            return;
        isRequestingPermission = true;
        ActivityCompat.requestPermissions(this, listRequest, RequestConstants.RC_PERMISSIONS);
    }

    public void requestPermissionsLocation(String from) {
        if (AppConstants.LOG_DEBUG)
            Log.e("requestPermissions", from + "-isRequestingPermissionLocation:" + isRequestingPermissionLocation);
        prefWriteBoolean(SKIP_TUT_LOCATION + "_" + getCurrentUserId(), true);
        if (isRequestingPermissionLocation)
            return;
        isRequestingPermissionLocation = true;
        ActivityCompat.requestPermissions(this, requestLocation, RequestConstants.RC_PERMISSIONS_LOCATION);
    }

    private String[] listRequest = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String[] requestLocation = {
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    public boolean isNeedPermissions() {
        return Build.VERSION.SDK_INT >= 23
                && (ActivityCompat.checkSelfPermission(this, listRequest[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, listRequest[1]) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean isNeedPermissionsLocation() {
        return Build.VERSION.SDK_INT >= 23
                && (ActivityCompat.checkSelfPermission(this, requestLocation[0]) != PackageManager.PERMISSION_GRANTED);
    }

    public boolean hasPermissionLocation() {
        return Build.VERSION.SDK_INT < 23 || ActivityCompat.checkSelfPermission(this, requestLocation[0]) == PackageManager.PERMISSION_GRANTED;
    }

    private void onPermissionGranted() {
        try {
            isRequestingPermission = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameParent);
            if (currentFragment != null)
                ((BaseFragment) currentFragment).onPermissionsGranted();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void onPermissionDenied() {
        try {
            isRequestingPermission = false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.frameParent);
            if (currentFragment != null)
                ((BaseFragment) currentFragment).onPermissionsDenied();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestConstants.RC_PERMISSIONS)
            checkPermissionsResult();
    }

    private void checkPermissionsResult() {
        try {
            String msg = "";
            if (ActivityCompat.checkSelfPermission(this, listRequest[0]) != PackageManager.PERMISSION_GRANTED)
                msg = getString(R.string.msg_request_permission_camera);
            if (ActivityCompat.checkSelfPermission(this, listRequest[1]) != PackageManager.PERMISSION_GRANTED) {
                if (!TextUtils.isEmpty(msg))
                    msg += "\n";
                msg += getString(R.string.msg_request_permission_storage);
            }

            if (TextUtils.isEmpty(msg)) {
                onPermissionGranted();
                return;
            }
            showNotice(msg, false, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    gotoAppSetting();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void gotoAppSetting() {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, RequestConstants.RC_APP_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
            isRequestingPermission = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstants.LOG_DEBUG) Log.d("TAG", "onActivityResult: " + requestCode);
        if (requestCode == RequestConstants.RC_APP_SETTINGS) {
            if (!isNeedPermissions()) {
                onPermissionGranted();
            } else {
                onPermissionDenied();
            }
            isRequestingPermission = false;
        } else if (requestCode == RequestConstants.RC_ENABLE_GPS
                || requestCode == RequestConstants.RC_WIRELESS_SETTINGS
                || requestCode == RequestConstants.RC_LOCATION_SETTINGS) {
            if (resultCode == RESULT_OK && !isNeedPermissions()) {
                onPermissionGranted();
                return;
            }
            onPermissionDenied();
        }
    }


    //TODO size manager
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private float screenDensity = 0;

    public float getScreenDensity() {
        if (screenDensity == 0)
            screenDensity = getDisplayMetrics().density;
        return screenDensity;
    }

    private int screenWidth = 0;

    public int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private int screenHeight = 0;

    public int getScreenHeight() {
        if (screenHeight == 0) {
            int statusBarHeight = 0;
            try {
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            screenHeight = getDisplayMetrics().heightPixels - statusBarHeight;
        }
        return screenHeight;
    }

    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / AppConstants.SCREEN_WIDTH_DESIGN;
        return scaleValue;
    }

    public int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

    //TODO date time format
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat shortDateFormat;
    private Calendar tmpCalendar;

    public Calendar getTmpCalendar() {
        if (tmpCalendar == null)
            tmpCalendar = Calendar.getInstance();
        return tmpCalendar;
    }

    public SimpleDateFormat getDateFormat() {
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat(AppConstants.DATE_TIME_FORMAT);
        return dateFormat;
    }

    public SimpleDateFormat getTimeFormat() {
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat(AppConstants.TIME_FORMAT);
        return dateFormat;
    }

    public SimpleDateFormat getShortDateFormat() {
        if (shortDateFormat == null)
            shortDateFormat = new SimpleDateFormat(AppConstants.SHORT_DATE_TIME_FORMAT);
        return shortDateFormat;
    }

    //TODO Tracking Location
    public void startTrackingLocation() {
        TrackingLocationManager.getInstance(this).startServiceTrackingLocation();
    }

    public void checkGpsAndRequestLocation() {
        TrackingLocationManager.getInstance(this).startTrackingLocation(new ListenerCheckGPS() {
            @Override
            public void onFailed() {
                onPermissionDenied();
            }

            @Override
            public void onSuccess() {
                TrackingLocationManager.getInstance(ActMain.this).getCurrentLocation();
            }
        });
    }

    public void checkGpsService(ListenerGPSChecking listener) {
        TrackingLocationManager.getInstance(this).checkGpsService(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        clearBadgeNotification("onPause");
        try {
            TrackingLocationManager.getInstance(this).switchToBackgroundMode();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        stopService(new Intent(ActMain.this, ServiceTrackingLocation.class));
    }

    //
    private Location mCurrentLocation;

    private void checkInitCurrentLocation() {
        if (mCurrentLocation == null) {
            try {
                float lat = prefGetFloat(PrefManager.LAST_KNOWN_LAT, 1);
                float lng = prefGetFloat(PrefManager.LAST_KNOWN_LNG, 1);
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(lat);
                mCurrentLocation.setLongitude(lng);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentLat() {
        checkInitCurrentLocation();
        return mCurrentLocation != null ? mCurrentLocation.getLatitude() + "" : "1";
    }

    public String getCurrentLng() {
        checkInitCurrentLocation();
        return mCurrentLocation != null ? mCurrentLocation.getLongitude() + "" : "";
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null && intent.getAction() != null) {
                    if (intent.getAction().equals(ACTION_PUSH_NEWS)) {

                    } else if (intent.getAction().equals(IntentActionConstant.ACTION_UPDATE_LOCATION)) {
                        mCurrentLocation = intent.getParcelableExtra(EXTRA_LOCATION);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    private void registerReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(IntentActionConstant.ACTION_UPDATE_LOCATION);
            filter.addAction(ACTION_PUSH_NEWS);

            registerReceiver(broadcastReceiver, filter);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void unregisterReceiver() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
