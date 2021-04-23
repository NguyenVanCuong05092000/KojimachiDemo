package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import jp.co.kojimachi.constant.ApiConstants;

public class EntityOption {
    public int idOption;
    public String nameOption;
    public int timeOption;
    public int number;
    public String namePaymentType;
    public String payment;

    public EntityOption(JSONObject json) {
        idOption = json.optInt(ApiConstants.PARAM_ID);

        nameOption = json.optString(ApiConstants.PARAM_OPTION_NAME);
        if (TextUtils.isEmpty(nameOption) || nameOption.equals("null"))
            nameOption = "";

        timeOption = json.optInt(ApiConstants.PARAM_TIME_OPTION);

        number = json.optInt(ApiConstants.PARAM_NUMBER);

        namePaymentType = json.optString(ApiConstants.PARAM_NAME_PAYMENT_TYPE);
        if (TextUtils.isEmpty(namePaymentType) || namePaymentType.equals("null"))
            nameOption = "";

        payment = json.optString(ApiConstants.PARAM_PAYMENT);
        if (TextUtils.isEmpty(payment) || payment.equals("null"))
            payment = "";
    }
}
