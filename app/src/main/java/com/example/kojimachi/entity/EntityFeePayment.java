package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import com.example.kojimachi.constant.ApiConstants;

public class EntityFeePayment {
    public String feePaymentType;
    public String payment;

    public EntityFeePayment(JSONObject json) {

        feePaymentType = json.optString(ApiConstants.PARAM_FEE_PAYMENT_TYPE);
        if (TextUtils.isEmpty(feePaymentType) || feePaymentType.equals("null"))
            feePaymentType = "";
        payment = json.optString(ApiConstants.PARAM_PAYMENT);
        if (TextUtils.isEmpty(payment) || payment.equals("null"))
            payment = "0";
    }
}
