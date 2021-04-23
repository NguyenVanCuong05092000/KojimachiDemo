package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.kojimachi.R;
import jp.co.kojimachi.adapter.AdapterDailyActuarial;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityDailyActuarial;
import jp.co.kojimachi.entity.EntityFeeDailyActuarial;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.listener.CallbackGetDetailActuarial;
import jp.co.kojimachi.utils.AppUtils;
import jp.co.kojimachi.view.CustomCurrencyEditText;

import static jp.co.kojimachi.constant.FragmentConstants.FRM_DAILY_ACTUARIAL;

public class FrmDailyActuarial extends BaseFragment implements View.OnClickListener, CallbackApi, CallbackGetDetailActuarial {

    private TextView tvTotalSales;
    private TextView tvTakeOutCashTotal;
    private TextView tvInsufficientCash;
    private TextView tvDate;
    private String date;
    private CustomCurrencyEditText tvActualCashOnHand;
    private RecyclerView rcDailyActuarial;

    private final TextView.OnEditorActionListener actionListener = (v, actionId, event) -> {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            updateInsufficientCash();
            if (v != null) {
                AppUtils.hideKeyboard(v);
                v.clearFocus();
            }
        }
        return false;
    };

    public static FrmDailyActuarial getInstance() {
        return new FrmDailyActuarial();
    }

    public static FrmDailyActuarial getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmDailyActuarial fragment = new FrmDailyActuarial();
        Bundle data = new Bundle();
        if (mapData != null && mapData.containsKey(EXTRA_DAY)) {
            data.putString(EXTRA_DAY, (String) mapData.get(EXTRA_DAY));
        }
        if (arrayList != null) data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_daily_actuarial;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_DAILY_ACTUARIAL;
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
        finish();
    }

    @Override
    protected void loadControlsAndResize(View view) {
        tvActualCashOnHand = view.findViewById(R.id.tvActualCashOnHand);
        tvActualCashOnHand.getLayoutParams().height = activity.getSizeWithScale(34);

        tvTotalSales = view.findViewById(R.id.tvTotalSales);
        tvTakeOutCashTotal = view.findViewById(R.id.tvTakeOutCashTotal);
        tvInsufficientCash = view.findViewById(R.id.tvInsufficientCash);
        tvDate = view.findViewById(R.id.tvDate);
        rcDailyActuarial = view.findViewById(R.id.rcDailyActuarial);

        TextView btnPayOff = view.findViewById(R.id.btnPayOff);
        btnPayOff.getLayoutParams().width = activity.getSizeWithScale(146);
        btnPayOff.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnGoBack = view.findViewById(R.id.btnGoBack);
        btnGoBack.getLayoutParams().width = activity.getSizeWithScale(146);
        btnGoBack.getLayoutParams().height = activity.getSizeWithScale(46);

        View imgDash = view.findViewById(R.id.imgDash);
        imgDash.getLayoutParams().width = activity.getSizeWithScale(340);
        imgDash.getLayoutParams().height = activity.getSizeWithScale(24);

        btnGoBack.setOnClickListener(this);
        btnPayOff.setOnClickListener(this);
        tvActualCashOnHand.setOnClickListener(this);
        tvActualCashOnHand.setOnTextChangedListener(this::updateInsufficientCash);
        tvActualCashOnHand.setOnEditorActionListener(actionListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        setDate();
        setupListFeeDailyActuarial();
        getDailyActuarialFromServer();
    }

    private void getDailyActuarialFromServer() {
        activity.getDailyActuarial(date, true, this);
    }

    private void setDate() {
        setText(tvDate, date == null ? "" + " 現在" : date.replace("-", "/") + "  現在");
    }

    private void initData() {
        if (getArguments() == null)
            finish();
        if (getArguments().containsKey(EXTRA_DAY))
            date = getArguments().getString(EXTRA_DAY);
    }

    private void setupListFeeDailyActuarial() {
        listFeeDailyActuarial = new ArrayList<>();
        if (adapterDailyActuarial == null) {
            adapterDailyActuarial = new AdapterDailyActuarial(listFeeDailyActuarial);
            rcDailyActuarial.setLayoutManager(new LinearLayoutManager(getContext()));
            rcDailyActuarial.setAdapter(adapterDailyActuarial);
        }
    }

    private ArrayList<EntityFeeDailyActuarial> listFeeDailyActuarial;
    private AdapterDailyActuarial adapterDailyActuarial;

    private void setDataRecyclerView(EntityDailyActuarial entityDailyActuarial) {
        if (entityDailyActuarial != null && listFeeDailyActuarial != null) {
            try {
                listFeeDailyActuarial.clear();
                //
                if (AppUtils.parseInt(entityDailyActuarial.facilityDeposit) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleFacilityDeposit, entityDailyActuarial.facilityDeposit));

                if (AppUtils.parseInt(entityDailyActuarial.feePlacement) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleFeePlacement, entityDailyActuarial.feePlacement));

                if (AppUtils.parseInt(entityDailyActuarial.cashTakeOut) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleCashTakeOut, entityDailyActuarial.cashTakeOut));

                if (AppUtils.parseInt(entityDailyActuarial.cashDepositSlip) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleCashDepositSlip, entityDailyActuarial.cashDepositSlip));

                if (AppUtils.parseInt(entityDailyActuarial.roomAttachment) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleRoomAttachment, entityDailyActuarial.roomAttachment));

                if (AppUtils.parseInt(entityDailyActuarial.roomSlip) > 0)
                    listFeeDailyActuarial.add(new EntityFeeDailyActuarial(entityDailyActuarial.titleRoomSlip, entityDailyActuarial.roomSlip));

                if (adapterDailyActuarial != null)
                    adapterDailyActuarial.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(EntityDailyActuarial entityDailyActuarial) {
        try {
            if (entityDailyActuarial != null) {
                setText(tvTotalSales, AppUtils.formatPrice(entityDailyActuarial.totalSales + ""));
                setText(tvTakeOutCashTotal, AppUtils.formatPrice(entityDailyActuarial.takeOutMoney + ""));
                setText(tvActualCashOnHand, AppUtils.formatPrice(entityDailyActuarial.actualMoney + ""));
                setText(tvInsufficientCash, AppUtils.formatPrice(entityDailyActuarial.excessMoney + ""));
                setDataRecyclerView(entityDailyActuarial);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateActualCashOnHand() {
        try {
            updateInsufficientCash();
            EntityDailyActuarial bodyDailyActuarial = new EntityDailyActuarial();
            bodyDailyActuarial.totalSales = getText(tvTotalSales);
            bodyDailyActuarial.takeOutMoney = getText(tvTakeOutCashTotal);
            bodyDailyActuarial.actualMoney = getText(tvActualCashOnHand);
            bodyDailyActuarial.excessMoney = getText(tvInsufficientCash);
            activity.updateDailyActuarial(bodyDailyActuarial, true, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateInsufficientCash() {
        try {
            setText(tvInsufficientCash, AppUtils.formatPrice((getValueFromTextView(tvTakeOutCashTotal) - getValueFromTextView(tvActualCashOnHand)) + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getValueFromTextView(TextView textView) {
        return TextUtils.isEmpty(getText(textView)) ? 0 : AppUtils.parseInt(getText(textView).replace(",", ""));
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        switch (v.getId()) {
            case R.id.btnGoBack:
                finish();
                break;
            case R.id.btnPayOff:
                updateActualCashOnHand();
                break;
        }
    }

    @Override
    public void onFinished(ApiResult result) {
        try {
            activity.showNotice(result.message, true, dialog -> {
                finish();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessGetDetailActuarial(EntityDailyActuarial entity) {
        AppUtils.showKeyboard(tvActualCashOnHand);
        setData(entity);
    }

    @Override
    public void onFailedGetDetailActuarial() {
        finish();
    }
}