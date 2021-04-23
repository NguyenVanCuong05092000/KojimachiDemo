package com.example.kojimachi.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import jp.co.kojimachi.R;
import jp.co.kojimachi.adapter.AdapterSchedule;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.constant.ExtraConstants;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntitySchedule;
import jp.co.kojimachi.listener.CallbackGetSchedule;
import jp.co.kojimachi.listener.ListenerClickSchedule;
import jp.co.kojimachi.utils.AppUtils;
import jp.co.kojimachi.view.CustomMonthLayout;

import static jp.co.kojimachi.constant.ApiConstants.STATUS_START_WORKING;
import static jp.co.kojimachi.constant.FragmentConstants.FRM_HOME;

public class FrmHome extends BaseFragment implements View.OnClickListener, ListenerClickSchedule, CallbackGetSchedule {

    public static FrmHome getInstance(Bundle dataSave) {
        FrmHome fragment = new FrmHome();
        if (dataSave != null) {
            Bundle data = new Bundle();
            data.putBundle(ExtraConstants.EXTRA_SAVE_INSTANCE_STATE, dataSave);
            fragment.setArguments(data);
        }
        return fragment;
    }

    public static FrmHome getInstanceWithBackStackData(HashMap<String, Object> mapData) {
        FrmHome fragment = new FrmHome();
        if (mapData != null && mapData.containsKey(ExtraConstants.EXTRA_POSITION)) {
            Bundle data = new Bundle();
            data.putInt(ExtraConstants.EXTRA_POSITION, (Integer) mapData.get(ExtraConstants.EXTRA_POSITION));
            data.putInt(ExtraConstants.EXTRA_OFFSET, (Integer) mapData.get(ExtraConstants.EXTRA_OFFSET));
            fragment.setArguments(data);
        }
        return fragment;
    }

    private TextView btnCommuting;
    private TextView btnLeaveWork;
    private RecyclerView rcSchedule;
    private TextView tvDepositPeriod;
    private TextView tvDepositAmount;

    private CustomMonthLayout clCustomDate;
    private View clDialog;
    private View clRetryGetListSchedule;
    private ImageView ivCloseNotice;
    private TextView tvInfo;
    private TextView tvContent;
    private TextView tvMessage;
    private SimpleDateFormat formatDate = new SimpleDateFormat(AppConstants.DATE_FORMAT, new Locale("ja"));
    private SimpleDateFormat formatDateDepositPeriod = new SimpleDateFormat(AppConstants.DATE_FORMAT_DEPOSIT_PERIOD, new Locale("ja"));
    private String day;

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_home;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_HOME;
    }

    @Override
    protected void finish() {

    }

    @Override
    protected void loadControlsAndResize(View view) {
        View clFooter = view.findViewById(R.id.clFooter);
        clFooter.getLayoutParams().height = activity.getSizeWithScale(93);

        btnCommuting = view.findViewById(R.id.btnCommuting);
        btnCommuting.getLayoutParams().width = activity.getSizeWithScale(146);
        btnCommuting.getLayoutParams().height = activity.getSizeWithScale(50);

        btnLeaveWork = view.findViewById(R.id.btnLeaveWork);
        btnLeaveWork.getLayoutParams().width = activity.getSizeWithScale(146);
        btnLeaveWork.getLayoutParams().height = activity.getSizeWithScale(50);

        rcSchedule = view.findViewById(R.id.rcSchedule);
        tvDepositPeriod = view.findViewById(R.id.tvDepositPeriod);
        tvDepositAmount = view.findViewById(R.id.tvDepositAmount);

        View btnPaymentContact = view.findViewById(R.id.btnPaymentContact);
        btnPaymentContact.getLayoutParams().width = activity.getSizeWithScale(146);
        btnPaymentContact.getLayoutParams().height = activity.getSizeWithScale(50);

        View btnRetryGetListSchedule = view.findViewById(R.id.btnRetryGetListSchedule);
        btnRetryGetListSchedule.getLayoutParams().width = activity.getSizeWithScale(145);
        btnRetryGetListSchedule.getLayoutParams().height = activity.getSizeWithScale(30);

        View imgTabShiftTable = view.findViewById(R.id.imgTabShiftTable);
        imgTabShiftTable.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabShiftTable.getLayoutParams().width = activity.getSizeWithScale(90);

        View imgTabDailyActuarial = view.findViewById(R.id.imgTabDailyActuarial);
        imgTabDailyActuarial.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabDailyActuarial.getLayoutParams().width = activity.getSizeWithScale(90);

        View imgTabViewSales = view.findViewById(R.id.imgTabViewSales);
        imgTabViewSales.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabViewSales.getLayoutParams().width = activity.getSizeWithScale(90);

        clCustomDate = view.findViewById(R.id.clCustomDate);
        clDialog = view.findViewById(R.id.clDialog);
        ivCloseNotice = view.findViewById(R.id.ivCloseNotice);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvContent = view.findViewById(R.id.tvContent);
        tvMessage = view.findViewById(R.id.tvMessage);
        clRetryGetListSchedule = view.findViewById(R.id.clRetryGetListSchedule);

        btnCommuting.setOnClickListener(this);
        btnLeaveWork.setOnClickListener(this);
        btnPaymentContact.setOnClickListener(this);
        imgTabShiftTable.setOnClickListener(this);
        imgTabDailyActuarial.setOnClickListener(this);
        imgTabViewSales.setOnClickListener(this);
        ivCloseNotice.setOnClickListener(this);
        btnRetryGetListSchedule.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLeaveWork.setEnabled(false);
        clickNextAndPreDay();
        day = formatDate.format(clCustomDate.getTmpCalendar().getTime());
        getListSchedule();
    }

    private void getListSchedule() {
        activity.getListSchedule(day, true, this);
    }

    private boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().compareTo(clCustomDate.getTmpCalendar().getTime()) > 0;
    }

    private void clickNextAndPreDay() {
        try {
            Calendar minCalendar = Calendar.getInstance();
            clCustomDate.setMinCalendar(minCalendar);

            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.set(Calendar.MONTH, maxCalendar.get(Calendar.MONTH) + 1);
            clCustomDate.setMaxCalendar(maxCalendar);
            clCustomDate.setOnArrowDayClickListener(new CustomMonthLayout.OnArrowDayClickListener() {
                @Override
                public void onClickPreDay(Calendar calendar) {
                    day = formatDate.format(calendar.getTime());
                    getListSchedule();
                }

                @Override
                public void onClickNextDay(Calendar calendar) {
                    day = formatDate.format(calendar.getTime());
                    getListSchedule();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dataListSchedule(ArrayList<EntitySchedule> entitySchedules) {
        try {
            AdapterSchedule adapterSchedule = new AdapterSchedule(entitySchedules, getContext(), this);
            rcSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
            rcSchedule.setAdapter(adapterSchedule);
        } catch (Exception e) {
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

    private void showDialog() {
        clDialog.setVisibility(View.VISIBLE);
        setText(tvInfo, "11月のシフトが確定しました。");
        setText(tvContent, "10/21 21:00 ○○コースの予約が入りました。");
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.btnCommuting:
                activity.updateStatusShift(STATUS_START_WORKING, null);
                startWorkingDay();
                break;
            case R.id.btnLeaveWork:
                stopWorkingDay();
                break;
            case R.id.imgTabShiftTable:
                if (checkNetwork())
                    showFrmShiftTable();
                break;
            case R.id.imgTabDailyActuarial:
                if (checkNetwork())
                    showDailyActuarial();
                break;
            case R.id.imgTabViewSales:
                if (checkNetwork())
                    showFrmViewSales();
                break;
            case R.id.ivCloseNotice:
                clDialog.setVisibility(View.GONE);
                break;
            case R.id.btnRetryGetListSchedule:
                clRetryGetListSchedule.setVisibility(View.GONE);
                getListSchedule();
                break;
        }
    }

    private boolean checkNetwork() {
        if (!AppUtils.isNetworkAvailable(activity)) {
            activity.showNotice(R.string.msg_error_internet, true, null);
            return false;
        }
        return true;
    }

    private HashMap<String, Object> getDataSaveState() {
        int itemPosition = 0;
        int offset = 0;
        try {
//            View view = recyclerView.getChildAt(0);
//            offset = view == null ? 0 : view.getTop() - recyclerView.getPaddingTop();
//            itemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        HashMap<String, Object> mapIndex = new HashMap<>();
        mapIndex.put(EXTRA_OFFSET, offset);
        mapIndex.put(EXTRA_POSITION, itemPosition);
        return mapIndex;
    }


    private void showBookingConfirmation(int scheduleId) {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_ID, scheduleId);
            activity.showBookingConfirmation(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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

    private void showDailyActuarial() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_DAY, day);
            activity.showDailyActuarial(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showFrmViewSales() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            activity.showViewSales(arrayList, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showMap(double latitude, double longitude) {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_LATITUDE, latitude);
            mapData.put(EXTRA_LONGITUDE, longitude);
            activity.showMap(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickStartBooking(int scheduleId) {
        if (checkNetwork())
            if (isToday())
                showBookingConfirmation(scheduleId);
    }

    @Override
    public void onClickFacility(String url) {
        if (checkNetwork())
            if (isToday())
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
    }

    @Override
    public void onSuccessGetListSchedule(ArrayList<EntitySchedule> entitySchedules, String totalPrice, int therapistStatus) {
        if (entitySchedules == null || entitySchedules.size() == 0) {
            if (isToday()) {
                startWorkingDay();
            }
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
            if (therapistStatus < 2 && isToday()) {
                btnCommuting.setEnabled(true);
                btnCommuting.setTextColor(Color.parseColor("#FFFFFF"));
                btnCommuting.setBackgroundResource(R.drawable.bg_button_normal);
            } else {
                startWorkingDay();
            }
        }
        dataListSchedule(entitySchedules);
        setText(tvDepositPeriod, formatDateDepositPeriod.format(clCustomDate.getTmpCalendar().getTime()));
        if (TextUtils.isEmpty(totalPrice) || totalPrice.equals("null")) {
            setText(tvDepositAmount, "0");
        } else {
            try {
                setText(tvDepositAmount, AppUtils.formatPrice(totalPrice));
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        if (therapistStatus == 4) {
            int id = 0;
            for (int i = 0; i < entitySchedules.size(); i++) {
                if (entitySchedules.get(i).status > 2) {
                    id = entitySchedules.get(i).scheduleId;
                }
            }
            if (id > 0) showBookingConfirmation(id);
        }
    }

    @Override
    public void onFailedGetSchedule() {
        clRetryGetListSchedule.setVisibility(View.VISIBLE);
        btnLeaveWork.setEnabled(true);
        btnLeaveWork.setTextColor(Color.parseColor("#FFFFFF"));
        btnLeaveWork.setBackgroundResource(R.drawable.bg_button_normal);
    }
}