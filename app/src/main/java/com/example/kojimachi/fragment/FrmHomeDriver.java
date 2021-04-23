package com.example.kojimachi.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.kojimachi.R;
import jp.co.kojimachi.adapter.AdapterScheduleDriver;
import jp.co.kojimachi.constant.ExtraConstants;
import jp.co.kojimachi.constant.PrefConstants;
import jp.co.kojimachi.dialog.DialogHomeDriver;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityCar;
import jp.co.kojimachi.entity.EntitySchedule;
import jp.co.kojimachi.entity.EntityVehicleSettings;
import jp.co.kojimachi.entity.prefentity.PrefInt;
import jp.co.kojimachi.entity.prefentity.PrefString;
import jp.co.kojimachi.listener.CallbackActionDriver;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.listener.CallbackGetSchedule;
import jp.co.kojimachi.listener.ListenerOptionHomeDriverClick;
import jp.co.kojimachi.listener.ListenerUpdateStatusShift;
import jp.co.kojimachi.utils.AppUtils;

import static jp.co.kojimachi.constant.FragmentConstants.FRM_HOME_DRIVER;
import static jp.co.kojimachi.constant.PrefConstants.CAR_NUMBER;
import static jp.co.kojimachi.constant.PrefConstants.ID_CAR;
import static jp.co.kojimachi.constant.PrefConstants.NAME;
import static jp.co.kojimachi.constant.PrefConstants.PHONE;

public class FrmHomeDriver extends BaseFragment implements View.OnClickListener, ListenerOptionHomeDriverClick, CallbackGetSchedule {

    public static FrmHomeDriver getInstance(Bundle dataSave) {
        FrmHomeDriver fragment = new FrmHomeDriver();
        if (dataSave != null) {
            Bundle data = new Bundle();
            data.putBundle(ExtraConstants.EXTRA_SAVE_INSTANCE_STATE, dataSave);
            fragment.setArguments(data);
        }
        return fragment;
    }

    public static FrmHomeDriver getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmHomeDriver fragment = new FrmHomeDriver();
        Bundle data = new Bundle();
        if (mapData != null && mapData.containsKey(EXTRA_LIST_CARS)) {
            data.putSerializable(EXTRA_LIST_CARS, (ArrayList<EntityCar>) mapData.get(EXTRA_LIST_CARS));
        }
        if (arrayList != null)
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_home_driver;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_HOME_DRIVER;
    }

    @Override
    protected void finish() {

    }

    private static final int STATUS_START_WORKING = 1;
    private static final int STATUS_END_WORKING = 2;
    private TextView btnCommuting;
    private TextView btnLeaveWork;
    private TextView tvMessage;
    private RecyclerView rcSchedule;
    private DialogHomeDriver dialogHomeDriver;

    private ArrayList<String> list;
    private ArrayList<EntityCar> entityCars;
    private boolean isWorking;
    private AdapterScheduleDriver adapterScheduleDriver;

    @Override
    protected void loadControlsAndResize(View view) {
        btnCommuting = view.findViewById(R.id.btnCommuting);
        btnCommuting.getLayoutParams().width = activity.getSizeWithScale(146);
        btnCommuting.getLayoutParams().height = activity.getSizeWithScale(50);

        btnLeaveWork = view.findViewById(R.id.btnLeaveWork);
        btnLeaveWork.getLayoutParams().width = activity.getSizeWithScale(146);
        btnLeaveWork.getLayoutParams().height = activity.getSizeWithScale(50);

        rcSchedule = view.findViewById(R.id.rcSchedule);
        tvMessage = view.findViewById(R.id.tvMessage);

        View imgTabShiftTable = view.findViewById(R.id.imgTabShiftTable);
        imgTabShiftTable.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabShiftTable.getLayoutParams().width = activity.getSizeWithScale(90);

        View imgTabNumberOfLinen = view.findViewById(R.id.imgTabNumberOfLinen);
        imgTabNumberOfLinen.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabNumberOfLinen.getLayoutParams().width = activity.getSizeWithScale(90);

        View imgTabDriver = view.findViewById(R.id.imgTabDriver);
        imgTabDriver.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabDriver.getLayoutParams().width = activity.getSizeWithScale(90);

        btnCommuting.setOnClickListener(this);
        btnLeaveWork.setOnClickListener(this);
        imgTabShiftTable.setOnClickListener(this);
        imgTabNumberOfLinen.setOnClickListener(this);
        imgTabDriver.setOnClickListener(this);

    }

    private boolean isWorkToday() {
        return activity.prefGetBoolean(PrefConstants.IS_WORK_TODAY);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(EXTRA_LIST_CARS)) {
            try {
                if (list == null)
                    list = new ArrayList<>();
                if (entityCars != null)
                    entityCars = new ArrayList<>();
                entityCars = (ArrayList<EntityCar>) getArguments().get(EXTRA_LIST_CARS);
                for (EntityCar car : entityCars)
                    list.add(car.carNumber);
                showDialogHomeDriver("", activity.getCurrentUserName(), activity.getCurrentPhone(), new CallbackActionDriver() {
                    @Override
                    public void onActionFinished(EntityVehicleSettings entityVehicleSettings) {
                        if (entityVehicleSettings.positionCar < 0) {
                            activity.showNotice("乗車車両を選択してください", false, null);
                        } else {
                            dialogHomeDriver.dismiss();
                            activity.vehicleSettings(entityCars.get(entityVehicleSettings.positionCar).id,
                                    entityVehicleSettings.nameDriver, entityVehicleSettings.phoneNumber, true, new CallbackApi() {
                                        @Override
                                        public void onFinished(ApiResult result) {
                                            activity.prefWriteArr(
                                                    new PrefString(CAR_NUMBER, entityVehicleSettings.boardingVehicle),
                                                    new PrefString(NAME, entityVehicleSettings.nameDriver),
                                                    new PrefInt(ID_CAR, entityCars.get(entityVehicleSettings.positionCar).id),
                                                    new PrefString(PHONE, entityVehicleSettings.phoneNumber));
                                            getListSchedule();
                                        }
                                    });
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (isWorkToday()) {
                if (isWorkingDay()) {
                    startWorkingDay();
                }
                getListSchedule();
            } else {
                startWorkingDay();
                tvMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getListSchedule() {
        activity.getListScheduleDriver(true, this);
    }

    public void showDialogHomeDriver(String boardingVehicle, String nameDriver, String phoneNumber, CallbackActionDriver callback) {
        try {
            if (dialogHomeDriver == null) {
                dialogHomeDriver = new DialogHomeDriver(activity);
            }
            dialogHomeDriver.changListData(list);
            dialogHomeDriver.show(boardingVehicle, nameDriver, phoneNumber, callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWorkingDay() {
        return activity.prefGetBoolean(PrefConstants.IS_WORKING + "_" + activity.getCurrentUserId());
    }

    private boolean haveCarNumber() {
        return !TextUtils.isEmpty(activity.prefGetString(CAR_NUMBER));
    }

    private void dataListScheduleDriver(ArrayList<EntitySchedule> entitySchedules) {
        adapterScheduleDriver = new AdapterScheduleDriver(entitySchedules, getContext(), this, isWorkingDay());
        rcSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rcSchedule.setAdapter(adapterScheduleDriver);
    }

    private HashMap<String, Object> getDataSaveState() {
        HashMap<String, Object> mapIndex = new HashMap<>();
        return mapIndex;
    }

    private void showFrmShiftTable() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            activity.showShiftTable(arrayList, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showNumberOfLinen() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_ID, 0);
            activity.showNumberOfLinen(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showVehicleSettings() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            activity.showVehicleSettings(arrayList, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void startWorkingDay() {
        btnCommuting.setEnabled(false);
        btnCommuting.setTextColor(Color.parseColor("#848484"));
        btnCommuting.setBackgroundResource(R.drawable.bg_button_selected);
        btnLeaveWork.setEnabled(true);
        btnLeaveWork.setTextColor(Color.parseColor("#FFFFFF"));
        btnLeaveWork.setBackgroundResource(R.drawable.bg_button_normal);
    }

    private void stopWorkingDay() {
        try {
            activity.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.btnCommuting:
                startWork();
                break;
            case R.id.btnLeaveWork:
                stopWorkingDay();
                break;
            case R.id.imgTabShiftTable:
                showFrmShiftTable();
                break;
            case R.id.imgTabNumberOfLinen:
                showNumberOfLinen();
                break;
            case R.id.imgTabDriver:
                if (!isWorking)
                    showVehicleSettings();
                break;
        }
    }

    private void startWork() {
        activity.updateStatusShift(STATUS_START_WORKING, new ListenerUpdateStatusShift() {
            @Override
            public void onUpdateStatusShift() {
                startWorkingDay();
                activity.prefWriteBoolean(PrefConstants.IS_WORKING + "_" + activity.getCurrentUserId(), true);
                adapterScheduleDriver.changeStatusWorking(isWorkingDay());
            }
        });
    }

    @Override
    public void onConfirmationClick(String idTranfers) {
        isWorking = true;
        activity.updateStatusCar(idTranfers, STATUS_START_WORKING, new CallbackApi() {
            @Override
            public void onFinished(ApiResult result) {

            }
        });
    }

    @Override
    public void onDoneClick(String idTranfers) {
        isWorking = false;
        activity.updateStatusCar(idTranfers, STATUS_END_WORKING, new CallbackApi() {
            @Override
            public void onFinished(ApiResult result) {

            }
        });
    }

    @Override
    public void onSuccessGetListSchedule(ArrayList<EntitySchedule> entitySchedules, String totalPrice, int therapistStatus) {
        if (entitySchedules == null) {
            startWorkingDay();
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
            dataListScheduleDriver(entitySchedules);
        }
    }

    @Override
    public void onFailedGetSchedule() {
        startWorkingDay();
        tvMessage.setVisibility(View.VISIBLE);
    }
}