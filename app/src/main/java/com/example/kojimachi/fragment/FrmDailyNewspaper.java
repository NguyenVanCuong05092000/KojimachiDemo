package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.kojimachi.R;
import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityDailyNewspaper;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.listener.CallbackGetDetailNewspaper;
import jp.co.kojimachi.utils.AppUtils;

import static jp.co.kojimachi.constant.FragmentConstants.FRM_DAILY_NEWSPAPER;

public class FrmDailyNewspaper extends BaseFragment implements View.OnClickListener, CallbackGetDetailNewspaper, CallbackApi {
    private TextView tvDateAndTime;
    private TextView tvHotel;
    private TextView tvTitleCourse;
    private TextView tvCourse;
    private TextView tvCustomer;
    private EditText edtContentOfReport;
    private int scheduleId;

    public static FrmDailyNewspaper getInstance() {
        return new FrmDailyNewspaper();
    }

    public static FrmDailyNewspaper getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmDailyNewspaper fragment = new FrmDailyNewspaper();
        Bundle data = new Bundle();
        if (mapData != null) {
            if (mapData.containsKey(EXTRA_ID)) {
                data.putInt(EXTRA_ID, (Integer) mapData.get(EXTRA_ID));
            }
        }
        if (arrayList != null) {
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        }
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_daily_newspaper;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_DAILY_NEWSPAPER;
    }

    @Override
    protected void finish() {
        if (getArguments().containsKey(EXTRA_ID)) {
            scheduleId = getArguments().getInt(EXTRA_ID);
        }
        activity.backToPreviousFromBackStack(getArguments());
    }

    @Override
    public boolean isBackPreviousEnable() {
        return true;
    }

    @Override
    public void backToPrevious() {
        finish();
    }

    @Override
    protected void loadControlsAndResize(View view) {
        View clDailyNewPaper = view.findViewById(R.id.clDailyNewPaper);
        clDailyNewPaper.getLayoutParams().width = activity.getSizeWithScale(338);

        View imgIcTime = view.findViewById(R.id.imgIcTime);
        imgIcTime.getLayoutParams().width = activity.getSizeWithScale(26);
        imgIcTime.getLayoutParams().height = activity.getSizeWithScale(26);

        View imgIcHotel = view.findViewById(R.id.imgIcHotel);
        imgIcHotel.getLayoutParams().width = activity.getSizeWithScale(21.5);
        imgIcHotel.getLayoutParams().height = activity.getSizeWithScale(25.1);

        View imgIcCourse = view.findViewById(R.id.imgIcCourse);
        imgIcCourse.getLayoutParams().width = activity.getSizeWithScale(24);
        imgIcCourse.getLayoutParams().height = activity.getSizeWithScale(24);

        View imgIcCustomer = view.findViewById(R.id.imgIcCustomer);
        imgIcCustomer.getLayoutParams().width = activity.getSizeWithScale(28);
        imgIcCustomer.getLayoutParams().height = activity.getSizeWithScale(28);

        TextView btnTemporarilySave = view.findViewById(R.id.btnTemporarilySave);
        btnTemporarilySave.getLayoutParams().width = activity.getSizeWithScale(93);
        btnTemporarilySave.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnPutForward = view.findViewById(R.id.btnPutForward);
        btnPutForward.getLayoutParams().width = activity.getSizeWithScale(93);
        btnPutForward.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnBack = view.findViewById(R.id.btnBack);
        btnBack.getLayoutParams().width = activity.getSizeWithScale(93);
        btnBack.getLayoutParams().height = activity.getSizeWithScale(46);

        edtContentOfReport = view.findViewById(R.id.edtContentOfReport);
        tvDateAndTime = view.findViewById(R.id.tvDateAndTime);
        tvHotel = view.findViewById(R.id.tvHotel);
        tvTitleCourse = view.findViewById(R.id.tvTitleCourse);
        tvCourse = view.findViewById(R.id.tvCourse);
        tvCustomer = view.findViewById(R.id.tvCustomer);

        btnBack.setOnClickListener(this);
        btnTemporarilySave.setOnClickListener(this);
        btnPutForward.setOnClickListener(this);
        view.findViewById(R.id.clRoot).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        getDailyNewspaperFromServer();

    }

    private void initData() {
        if (getArguments() == null)
            finish();
        if (getArguments().containsKey(EXTRA_ID)) {
            scheduleId = getArguments().getInt(EXTRA_ID);
        }
    }

    private void getDailyNewspaperFromServer() {
        activity.getDailyNewspaper(String.valueOf(scheduleId), true, this);
    }

    private void setData(EntityDailyNewspaper entityDailyNewspaper) {
        try {
            if (entityDailyNewspaper != null) {
                setText(tvDateAndTime, entityDailyNewspaper.displayDateTime);
                setText(tvHotel, entityDailyNewspaper.shopName);
                setText(tvTitleCourse, entityDailyNewspaper.courseName);
                setText(tvCourse, entityDailyNewspaper.treatmentTime + "分");
                setText(tvCustomer, entityDailyNewspaper.customerName);
                setText(edtContentOfReport,
                        (TextUtils.isEmpty(entityDailyNewspaper.content) || entityDailyNewspaper.content.equals("null")) ? "" : entityDailyNewspaper.content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDailyNewspaper(int status) {
        try {
            String content = getText(edtContentOfReport);
            activity.updateDailyNewspaper(String.valueOf(scheduleId), status, content, true, this);
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
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnTemporarilySave:
                updateDailyNewspaper(ApiConstants.STATUS_TEMPORARILY_SAVED);
                break;
            case R.id.btnPutForward:
                updateDailyNewspaper(ApiConstants.STATUS_PUT_FORWARD);
                break;
            case R.id.clRoot:
                AppUtils.hideKeyboard(getView());
                break;
        }
    }

    @Override
    public void onSuccessGetDetailNewspaper(EntityDailyNewspaper entityDailyNewspaper) {
        setData(entityDailyNewspaper);
    }

    @Override
    public void onFailedGetDetailNewspaper() {

    }

    @Override
    public void onFinished(ApiResult result) {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}