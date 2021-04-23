package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

import jp.co.kojimachi.constant.ApiConstants;

public class EntityDailyActuarial implements Serializable {

    public String totalSales;
    public String facilityDeposit;
    public String titleFacilityDeposit;
    public String feePlacement;
    public String titleFeePlacement;
    public String cashTakeOut;
    public String titleCashTakeOut;
    public String cashDepositSlip;
    public String titleCashDepositSlip;
    public String roomAttachment;
    public String titleRoomAttachment;
    public String roomSlip;
    public String titleRoomSlip;
    public String takeOutMoney;
    public String actualMoney;
    public String excessMoney;

    public EntityDailyActuarial() {
    }

    public EntityDailyActuarial(JSONObject jsonObject) {
        totalSales = jsonObject.optString(ApiConstants.PARAM_TOTAL_SALES);
        if (TextUtils.isEmpty(totalSales) || totalSales.equals("null"))
            totalSales = "0";

        facilityDeposit = jsonObject.optString(ApiConstants.PARAM_FACILITY_DEPOSIT);
        if (TextUtils.isEmpty(facilityDeposit) || facilityDeposit.equals("null"))
            facilityDeposit = "0";

        titleFacilityDeposit = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_FACILITY_DEPOSIT);
        if (TextUtils.isEmpty(titleFacilityDeposit) || titleFacilityDeposit.equals("null"))
            titleFacilityDeposit = "";

        feePlacement = jsonObject.optString(ApiConstants.PARAM_FEE_PLACEMENT);
        if (TextUtils.isEmpty(feePlacement) || feePlacement.equals("null"))
            feePlacement = "0";

        titleFeePlacement = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_FEE_PLACEMENT);
        if (TextUtils.isEmpty(titleFeePlacement) || titleFeePlacement.equals("null"))
            titleFeePlacement = "";

        cashTakeOut = jsonObject.optString(ApiConstants.PARAM_CASH_TAKE_OUT);
        if (TextUtils.isEmpty(cashTakeOut) || cashTakeOut.equals("null"))
            cashTakeOut = "0";

        titleCashTakeOut = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_CASH_TAKE_OUT);
        if (TextUtils.isEmpty(titleCashTakeOut) || titleCashTakeOut.equals("null"))
            titleCashTakeOut = "";

        cashDepositSlip = jsonObject.optString(ApiConstants.PARAM_CASH_DEPOSIT_SLIP);
        if (TextUtils.isEmpty(cashDepositSlip) || cashDepositSlip.equals("null"))
            cashDepositSlip = "0";

        titleCashDepositSlip = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_CASH_DEPOSIT_SLIP);
        if (TextUtils.isEmpty(titleCashDepositSlip) || titleCashDepositSlip.equals("null"))
            titleCashDepositSlip = "";

        roomAttachment = jsonObject.optString(ApiConstants.PARAM_ROOM_ATTACHMENT);
        if (TextUtils.isEmpty(roomAttachment) || roomAttachment.equals("null"))
            roomAttachment = "0";

        titleRoomAttachment = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_ROOM_ATTACHMENT);
        if (TextUtils.isEmpty(titleRoomAttachment) || titleRoomAttachment.equals("null"))
            titleRoomAttachment = "";

        roomSlip = jsonObject.optString(ApiConstants.PARAM_ROOM_SLIP);
        if (TextUtils.isEmpty(roomSlip) || roomSlip.equals("null"))
            roomSlip = "0";

        titleRoomSlip = jsonObject.optString(ApiConstants.PARAM_NAME_JAPAN_ROOM_SLIP);
        if (TextUtils.isEmpty(titleRoomSlip) || titleRoomSlip.equals("null"))
            titleRoomSlip = "";

        takeOutMoney = jsonObject.optString(ApiConstants.PARAM_TAKE_OUT_CASH_TOTAL);
        if (TextUtils.isEmpty(takeOutMoney) || takeOutMoney.equals("null"))
            takeOutMoney = "0";

        actualMoney = jsonObject.optString(ApiConstants.PARAM_INSUFFICIENT_CASH);
        if (TextUtils.isEmpty(actualMoney) || actualMoney.equals("null"))
            actualMoney = "0";

        excessMoney = jsonObject.optString(ApiConstants.PARAM_ACTUAL_CASH_ON_HAND);
        if (TextUtils.isEmpty(excessMoney) || excessMoney.equals("null"))
            excessMoney = "0";
    }
}
