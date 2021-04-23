package com.example.kojimachi.fragment;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.kojimachi.R;
import jp.co.kojimachi.adapter.AdapterListOptionCharge;
import jp.co.kojimachi.constant.PrefConstants;
import jp.co.kojimachi.data.PrefManager;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityBookingConfirmation;
import jp.co.kojimachi.entity.EntityUpdateOption;
import jp.co.kojimachi.listener.CallbackGetDetailSchedule;
import jp.co.kojimachi.listener.ListenerHandleResult;
import jp.co.kojimachi.listener.ListenerUpdateStatusShift;
import jp.co.kojimachi.utils.AppUtils;

import static jp.co.kojimachi.constant.ApiConstants.STATUS_BOOKING_CONFIRM;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_CHANGE;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_CREATED;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_END_OF_TREATMENT;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_EXTENSION_OF_ADD_OPTIONS;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_START_MOVING;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_START_MOVING_SHIFT;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_START_OF_TREATMENT;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_WAIT;
import static jp.co.kojimachi.constant.FragmentConstants.FRM_BOOKING_CONFIRMATION;

public class FrmBookingConfirmation extends BaseFragment implements View.OnClickListener, CallbackGetDetailSchedule {

    private View clBookingConfirm;
    private TextView tvTime;
    private TextView tvInfoHotel;
    private TextView tvInfoTechnicians;
    private View clInfo;
    private TextView tvInfo;
    private TextView tvTitleHotel;
    private TextView btnBookingConfirm;
    private TextView btnStartMoving;
    private TextView btnStartOfTreatment;
    private TextView btnEndOfTreatment;
    private View clFee;
    private TextView tvServiceCharge;
    private TextView tvBusinessTripFee;
    private TextView tvFee;
    private TextView btnExtensionOrAdditionalOptions;
    private View clAddExtend;
    private TextView tvInfoExtend;
    private RecyclerView rcOption;
    private View clEndingTreatment;
    private TextView tvFeeTaxIncluded;
    private TextView tvCashReceive;
    private TextView tvFeePlacement;
    private TextView tvTakeOutCash;
    private TextView tvTitleFeePlacement;
    private View btnWait;
    private CheckBox cbEndingTreatment;
    private ImageView imgTabDailyReport;
    private ImageView imgTabNumberOfLinen;
    private ImageView imgTabCancel;
    private TextView tvTimeExtended;
    private TextView tvNameIOldCourse;
    private TextView tvTimeEnd;
    private EntityBookingConfirmation entityBookingConfirmation;
    private AdapterListOptionCharge adapterListOptionCharge;
    private NestedScrollView scBooking;
    private int scheduleId;

    public static FrmBookingConfirmation getInstance() {
        return new FrmBookingConfirmation();
    }

    public static FrmBookingConfirmation getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmBookingConfirmation fragment = new FrmBookingConfirmation();
        Bundle data = new Bundle();
        if (mapData != null) {
            if (mapData.containsKey(EXTRA_DATA_BOOKING)) {
                data.putSerializable(EXTRA_DATA_BOOKING, (EntityBookingConfirmation) mapData.get(EXTRA_DATA_BOOKING));
            }
            if (mapData.containsKey(EXTRA_ID)) {
                data.putInt(EXTRA_ID, (Integer) mapData.get(EXTRA_ID));
            }
            if (mapData.containsKey(EXTRA_POSITION))
                data.putInt(EXTRA_POSITION, (int) mapData.get(EXTRA_POSITION));
        }
        if (arrayList != null) {
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
            fragment.setArguments(data);
        }
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_booking_confirmation;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_BOOKING_CONFIRMATION;
    }

    @Override
    protected void finish() {
        activity.backToPreviousFromBackStack(getArguments());
    }

    @Override
    public boolean isBackPreviousEnable() {
        return true;
    }

    @Override
    public void backToPrevious() {
        if (imgTabCancel.isSelected()) {
            finish();
        }
    }

    @Override
    protected void loadControlsAndResize(View view) {
        clBookingConfirm = view.findViewById(R.id.clBookingConfirm);
        tvTime = view.findViewById(R.id.tvTime);
        tvInfoHotel = view.findViewById(R.id.tvInfoHotel);
        tvInfoTechnicians = view.findViewById(R.id.tvInfoTechnicians);
        clInfo = view.findViewById(R.id.clInfo);
        tvInfo = view.findViewById(R.id.tvInfo);
        tvTitleHotel = view.findViewById(R.id.tvTitleHotel);

        btnBookingConfirm = view.findViewById(R.id.btnBookingConfirm);
        btnBookingConfirm.getLayoutParams().width = activity.getSizeWithScale(330);
        btnBookingConfirm.getLayoutParams().height = activity.getSizeWithScale(50);

        btnStartMoving = view.findViewById(R.id.btnStartMoving);
        btnStartMoving.getLayoutParams().width = activity.getSizeWithScale(330);
        btnStartMoving.getLayoutParams().height = activity.getSizeWithScale(50);

        btnStartOfTreatment = view.findViewById(R.id.btnStartOfTreatment);
        btnStartOfTreatment.getLayoutParams().width = activity.getSizeWithScale(330);
        btnStartOfTreatment.getLayoutParams().height = activity.getSizeWithScale(50);

        tvServiceCharge = view.findViewById(R.id.tvServiceCharge);
        clFee = view.findViewById(R.id.clFee);
        tvBusinessTripFee = view.findViewById(R.id.tvBusinessTripFee);
        tvFee = view.findViewById(R.id.tvFee);

        btnExtensionOrAdditionalOptions = view.findViewById(R.id.btnExtensionOrAdditionalOptions);
        btnExtensionOrAdditionalOptions.getLayoutParams().width = activity.getSizeWithScale(330);
        btnExtensionOrAdditionalOptions.getLayoutParams().height = activity.getSizeWithScale(50);

        clAddExtend = view.findViewById(R.id.clAddExtend);
        scBooking = view.findViewById(R.id.scBooking);

        tvTimeExtended = view.findViewById(R.id.tvTimeExtended);
        tvNameIOldCourse = view.findViewById(R.id.tvNameIOldCourse);
        tvTimeEnd = view.findViewById(R.id.tvTimeEnd);

        tvTitleFeePlacement = view.findViewById(R.id.tvTitleFeePlacement);

        rcOption = view.findViewById(R.id.rcOption);
        rcOption.getLayoutParams().height = activity.getSizeWithScale(173);

        View btnChange = view.findViewById(R.id.btnChange);
        btnChange.getLayoutParams().width = activity.getSizeWithScale(146);
        btnChange.getLayoutParams().height = activity.getSizeWithScale(46);

        View btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.getLayoutParams().width = activity.getSizeWithScale(146);
        btnCancel.getLayoutParams().height = activity.getSizeWithScale(46);

        btnEndOfTreatment = view.findViewById(R.id.btnEndOfTreatment);
        btnEndOfTreatment.getLayoutParams().width = activity.getSizeWithScale(330);
        btnEndOfTreatment.getLayoutParams().height = activity.getSizeWithScale(50);

        clEndingTreatment = view.findViewById(R.id.clEndingTreatment);
        tvFeeTaxIncluded = view.findViewById(R.id.tvFeeTaxIncluded);
        tvCashReceive = view.findViewById(R.id.tvCashReceive);
        tvFeePlacement = view.findViewById(R.id.tvFeePlacement);
        tvTakeOutCash = view.findViewById(R.id.tvTakeOutCash);
        cbEndingTreatment = view.findViewById(R.id.cbEndingTreatment);

        View btnStartMovingOn = view.findViewById(R.id.btnStartMovingOn);
        btnStartMovingOn.getLayoutParams().width = activity.getSizeWithScale(330);
        btnStartMovingOn.getLayoutParams().height = activity.getSizeWithScale(50);

        btnWait = view.findViewById(R.id.btnWait);
        btnWait.getLayoutParams().width = activity.getSizeWithScale(330);
        btnWait.getLayoutParams().height = activity.getSizeWithScale(50);

        imgTabDailyReport = view.findViewById(R.id.imgTabDailyReport);
        imgTabDailyReport.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabDailyReport.getLayoutParams().width = activity.getSizeWithScale(90);

        imgTabNumberOfLinen = view.findViewById(R.id.imgTabNumberOfLinen);
        imgTabNumberOfLinen.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabNumberOfLinen.getLayoutParams().width = activity.getSizeWithScale(90);

        imgTabCancel = view.findViewById(R.id.imgTabCancel);
        imgTabCancel.getLayoutParams().height = activity.getSizeWithScale(90);
        imgTabCancel.getLayoutParams().width = activity.getSizeWithScale(90);

        View btnInformationUpdate = view.findViewById(R.id.btnInformationUpdate);
        btnInformationUpdate.getLayoutParams().width = activity.getSizeWithScale(100);
        btnInformationUpdate.getLayoutParams().height = activity.getSizeWithScale(35);

        btnBookingConfirm.setOnClickListener(this);
        btnStartMoving.setOnClickListener(this);
        btnStartOfTreatment.setOnClickListener(this);
        btnExtensionOrAdditionalOptions.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnEndOfTreatment.setOnClickListener(this);
        btnStartMovingOn.setOnClickListener(this);
        btnWait.setOnClickListener(this);
        imgTabDailyReport.setOnClickListener(this);
        imgTabNumberOfLinen.setOnClickListener(this);
        imgTabCancel.setOnClickListener(this);
        imgTabCancel.setSelected(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() == null) {
            finish();
            return;
        }
        prepareDataBooking();
        setupAutoAnim(view);
    }

    private void prepareDataBooking() {
        if (getArguments().containsKey(EXTRA_ID)) {
            activity.getDetailSchedule(getArguments().getInt(EXTRA_ID), true, this);
            scheduleId = getArguments().getInt(EXTRA_ID);
        } else if (getArguments().containsKey(EXTRA_DATA_BOOKING)) {
            entityBookingConfirmation = (EntityBookingConfirmation) getArguments().getSerializable(EXTRA_DATA_BOOKING);
            if (entityBookingConfirmation == null) {
                finish();
                return;
            }
            showState(entityBookingConfirmation.state);
            initData();
            if (entityBookingConfirmation.scrolledY != 0) {
                scBooking.post(() -> {
                    scBooking.scrollTo(0, entityBookingConfirmation.scrolledY);
                    entityBookingConfirmation.scrolledY = 0;
                });
            }
        }
    }

    private void setupAutoAnim(View view) {
        ViewGroup clRoot = view.findViewById(R.id.clRoot);
        LayoutTransition layoutTransition = new LayoutTransition();
        clRoot.setLayoutTransition(layoutTransition);
        clRoot.getLayoutTransition().addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (view.getId() == clFee.getId())
                    scBooking.smoothScrollTo(0, btnExtensionOrAdditionalOptions.getBottom());
                if (view.getId() == btnEndOfTreatment.getId())
                    ObjectAnimator.ofInt(scBooking, "scrollY", btnEndOfTreatment.getBottom()).setDuration(1000).start();
                if (view.getId() == clEndingTreatment.getId())
                    scBooking.smoothScrollTo(0, clEndingTreatment.getBottom());
            }
        });
    }

    private void initData() {
        setText(tvTime, entityBookingConfirmation.displayDateTimeReserve);
        setText(tvTitleHotel, entityBookingConfirmation.titleHotel);
        setText(tvInfoHotel, entityBookingConfirmation.getDataInfoHotel());
        setText(tvInfoTechnicians, entityBookingConfirmation.getDataInfoTechnicians());
        setText(tvInfo, entityBookingConfirmation.getDataInfo());
        setText(tvServiceCharge, AppUtils.formatPrice(entityBookingConfirmation.serviceCharge) + " 円");
        setText(tvBusinessTripFee, AppUtils.formatPrice(entityBookingConfirmation.transportationFee + "") + " 円");
        setText(tvFee, entityBookingConfirmation.courseDataChargeFee + " 円など");
        setText(tvInfoExtend, entityBookingConfirmation.infoExtend);
        setText(tvFeeTaxIncluded, AppUtils.formatPrice(entityBookingConfirmation.feeTaxIncluded) + " 円");
        setText(tvCashReceive, AppUtils.formatPrice(entityBookingConfirmation.cashReceive) + " 円");
        setText(tvFeePlacement, entityBookingConfirmation.feePlacement);
        setText(tvTakeOutCash, AppUtils.formatPrice(entityBookingConfirmation.takeOutCash) + " 円");
        setText(tvTitleFeePlacement, entityBookingConfirmation.titleFeePayment);
        setText(tvTimeEnd, entityBookingConfirmation.timeEnd);
        setText(tvNameIOldCourse, entityBookingConfirmation.courseName);
        setText(tvTimeExtended, entityBookingConfirmation.minutesExtra + "分の延長が可能です。");
    }

    private void setMinutesExtra() {
        try {
            int minutes = 0;
            for (int i = 0; i < entityBookingConfirmation.optionBonus.size(); i++) {
                if (entityBookingConfirmation.optionBonus.get(i).isCheck) {
                    minutes = minutes + entityBookingConfirmation.optionBonus.get(i).numberOption * entityBookingConfirmation.optionBonus.get(i).timeOption;
                }
            }
            entityBookingConfirmation.minutesExtra = minutes;
            setText(tvTimeExtended, entityBookingConfirmation.minutesExtra + "分の延長が可能です。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareDataOptions() {
        try {
            if (adapterListOptionCharge == null) {
                if (entityBookingConfirmation.optionBonus != null || entityBookingConfirmation.optionBonus.size() > 0) {
                    for (int i = 0; i < entityBookingConfirmation.optionBonus.size(); i++) {
                        if (entityBookingConfirmation.optionBonus.get(i).numberOption == 0) {
                            entityBookingConfirmation.optionBonus.get(i).numberOption = 1;
                        }
                    }
                    adapterListOptionCharge = new AdapterListOptionCharge(entityBookingConfirmation.optionBonus, getContext(), new AdapterListOptionCharge.OnClickItemListener() {
                        @Override
                        public void onClickNumber() {
                            setMinutesExtra();
                        }

                        @Override
                        public void onClickNameOption(int position) {
                            for (int i = 0; i < entityBookingConfirmation.optionBonus.size(); i++) {
                                if (entityBookingConfirmation.optionBonus.get(position).kindCharge == 0) {
                                    if (entityBookingConfirmation.optionBonus.get(position).isCheck) {
                                        if (entityBookingConfirmation.optionBonus.get(i).kindCharge == 0) {
                                            entityBookingConfirmation.optionBonus.get(i).isCheckKindCharge = false;
                                            entityBookingConfirmation.optionBonus.get(i).isCheck = false;
                                        }
                                        entityBookingConfirmation.optionBonus.get(position).isCheckKindCharge = true;
                                        entityBookingConfirmation.optionBonus.get(position).isCheck = true;
                                        entityBookingConfirmation.optionBonus.get(i).numberOption = 1;
                                    } else {
                                        entityBookingConfirmation.optionBonus.get(i).isCheckKindCharge = true;
                                    }
                                }
                            }
                            adapterListOptionCharge.notifyDataSetChanged();
                            setMinutesExtra();
                        }
                    });
                }
                rcOption.setLayoutManager(new LinearLayoutManager(getContext()));
                rcOption.setAdapter(adapterListOptionCharge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOptionsToServer() {
        try {
            if (entityBookingConfirmation.optionBonus != null && entityBookingConfirmation.optionBonus.size() > 0) {
                ArrayList<EntityUpdateOption> entityUpdateOptions = new ArrayList<>();
                for (int i = 0; i < entityBookingConfirmation.optionBonus.size(); i++) {
                    if (entityBookingConfirmation.optionBonus.get(i).isCheck && entityBookingConfirmation.optionBonus.get(i).numberOption > 0) {
                        entityUpdateOptions.add(new EntityUpdateOption(entityBookingConfirmation.optionBonus.get(i).id, entityBookingConfirmation.optionBonus.get(i).numberOption));
                    }
                }
                    activity.updateOption(entityBookingConfirmation.scheduleId, entityUpdateOptions, new ListenerHandleResult() {
                        @Override
                        public void onSuccess(Object response) {
                            setText(tvInfoTechnicians, entityBookingConfirmation.getDataInfoTechnicians());
                            closeOptionsExtension();
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showState(int state) {
        switch (state) {
            case STATUS_CREATED:
                initStatusButton();
                break;
            case STATUS_BOOKING_CONFIRM:
                showBookingConfirm();
                break;
            case STATUS_START_MOVING:
                showStartMoving();
                break;
            case STATUS_START_OF_TREATMENT:
                showStartOfTreatment();
                break;
            case STATUS_EXTENSION_OF_ADD_OPTIONS:
                showOptionsExtension();
                break;
            case STATUS_CHANGE:
                closeOptionsExtension();
                break;
            case STATUS_END_OF_TREATMENT:
                showEndOfTreatment();
                break;
        }
    }

    private void initStatusButton() {
        btnStartMoving.setEnabled(false);
        btnStartOfTreatment.setEnabled(false);
        btnExtensionOrAdditionalOptions.setEnabled(false);
    }

    private HashMap<String, Object> getDataSaveState() {
        try {
            HashMap<String, Object> mapIndex = new HashMap<>();
            entityBookingConfirmation.scrolledY = scBooking.getScrollY();
            mapIndex.put(EXTRA_DATA_BOOKING, entityBookingConfirmation);
            return mapIndex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showFrmDailyNewspaper() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_ID, entityBookingConfirmation.scheduleId);
            activity.showDailyNewspaper(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showNumberOfLinen() {
        try {
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            HashMap<String, Object> mapData = new HashMap<>();
            mapData.put(EXTRA_ID, entityBookingConfirmation.scheduleId);
            activity.showNumberOfLinen(arrayList, mapData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void showBookingConfirm() {
        try {
            visibleView(clInfo);
            goneView(clFee);
            enableButton(btnStartMoving);
            enableButton(btnStartOfTreatment);
            disableButton(btnBookingConfirm);
            btnExtensionOrAdditionalOptions.setEnabled(false);
            entityBookingConfirmation.state = STATUS_BOOKING_CONFIRM;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStartMoving() {
        try {
            statusDoing(btnStartMoving);
            btnExtensionOrAdditionalOptions.setEnabled(false);
            visibleView(clInfo);
            goneView(clFee);
            disableButton(btnBookingConfirm);
            entityBookingConfirmation.state = STATUS_START_MOVING;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStartOfTreatment() {
        try {
            disableButton(btnBookingConfirm);
            disableButton(btnStartMoving);
            statusDoing(btnStartOfTreatment);
            visibleView(clInfo);
            goneView(clFee);
            enableButton(btnExtensionOrAdditionalOptions);
            visibleView(btnEndOfTreatment);
            entityBookingConfirmation.state = STATUS_START_OF_TREATMENT;
            imgTabCancel.setSelected(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOptionsExtension() {
        try {
            showStartOfTreatment();
            btnExtensionOrAdditionalOptions.setBackgroundResource(R.drawable.bg_button_extension_selected);
            btnExtensionOrAdditionalOptions.setEnabled(false);
            visibleView(clAddExtend);
            goneView(imgTabNumberOfLinen);
            entityBookingConfirmation.state = STATUS_EXTENSION_OF_ADD_OPTIONS;
            prepareDataOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeOptionsExtension() {
        try {
            disableButton(btnBookingConfirm);
            disableButton(btnStartMoving);
            statusDoing(btnStartOfTreatment);
            visibleView(clInfo);
            goneView(clFee);
            visibleView(btnEndOfTreatment);
            enableButton(btnExtensionOrAdditionalOptions);
            goneView(clAddExtend);
            visibleView(imgTabNumberOfLinen);
            entityBookingConfirmation.state = STATUS_CHANGE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEndOfTreatment() {
        try {
            disableButton(btnBookingConfirm);
            disableButton(btnExtensionOrAdditionalOptions);
            disableButton(btnStartOfTreatment);
            disableButton(btnStartMoving);
            goneView(btnEndOfTreatment);
            goneView(clAddExtend);
            visibleView(clEndingTreatment);
            imgTabCancel.setSelected(true);
            imgTabDailyReport.setVisibility(View.INVISIBLE);
            goneView(imgTabNumberOfLinen);
            entityBookingConfirmation.state = STATUS_END_OF_TREATMENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handelUpdateStatus(int status) {
        try {
            activity.updateStatusDetail(entityBookingConfirmation.scheduleId, status, new ListenerHandleResult() {
                @Override
                public void onSuccess(Object response) {
                    showState(status);
                }
            });
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
            case R.id.btnBookingConfirm:
                handelUpdateStatus(STATUS_BOOKING_CONFIRM);
                break;
            case R.id.btnStartMoving:
                handelUpdateStatus(STATUS_START_MOVING);
                break;
            case R.id.btnStartOfTreatment:
                handelUpdateStatus(STATUS_START_OF_TREATMENT);
                break;
            case R.id.btnExtensionOrAdditionalOptions:
                showOptionsExtension();
                break;
            case R.id.btnChange:
                updateOptionsToServer();
                break;
            case R.id.btnCancel:
                closeOptionsExtension();
                break;
            case R.id.btnEndOfTreatment:
                handelUpdateStatus(STATUS_END_OF_TREATMENT);
                break;
            case R.id.btnStartMovingOn:
                activity.updateStatusShift(STATUS_START_MOVING_SHIFT, new ListenerUpdateStatusShift() {
                    @Override
                    public void onUpdateStatusShift() {
                        scheduleId = 0;
                        PrefManager.getInstance(activity).writeInt(PrefConstants.ID_SCHEDULE + "_" + activity.getCurrentUserId(), scheduleId);
                        activity.backToHome();
                    }
                });
                break;
            case R.id.btnWait:
                activity.updateStatusShift(STATUS_WAIT, new ListenerUpdateStatusShift() {
                    @Override
                    public void onUpdateStatusShift() {
                        scheduleId = 0;
                        PrefManager.getInstance(activity).writeInt(PrefConstants.ID_SCHEDULE + "_" + activity.getCurrentUserId(), scheduleId);
                        activity.backToHome();
                    }
                });
                break;
            case R.id.imgTabDailyReport:
                showFrmDailyNewspaper();
                break;
            case R.id.imgTabNumberOfLinen:
                showNumberOfLinen();
                break;
            case R.id.imgTabCancel:
                scheduleId = 0;
                PrefManager.getInstance(activity).writeInt(PrefConstants.ID_SCHEDULE + "_" + activity.getCurrentUserId(), scheduleId);
                backToPrevious();
                break;
        }
    }

    @Override
    public void onSuccessGetDetailSchedule(EntityBookingConfirmation entity) {
        if (entity != null) {
            entityBookingConfirmation = entity;
            initData();
            showState(entityBookingConfirmation.state);
            prepareDataOptions();
        }
    }

    @Override
    public void onFailedGetSchedule() {
        finish();
    }

    //Utils
    private void enableButton(TextView button) {
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setBackgroundResource(R.drawable.bg_button_normal);
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(Color.parseColor("#848484"));
        button.setBackgroundResource(R.drawable.bg_button_selected);
        button.setEnabled(false);
    }

    private void statusDoing(TextView button) {
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setBackgroundResource(R.drawable.bg_button_cancel);
        button.setEnabled(false);
    }

    private void visibleView(View view) {
        if (!view.isShown()) view.setVisibility(View.VISIBLE);
    }

    private void goneView(View view) {
        if (view.isShown()) view.setVisibility(View.GONE);
    }

}