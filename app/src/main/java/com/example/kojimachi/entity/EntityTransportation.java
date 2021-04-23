package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import com.example.kojimachi.constant.ApiConstants;

public class EntityTransportation {
    public String transportType;
    public int transportFee;

    public EntityTransportation(JSONObject json) {

        transportType = json.optString(ApiConstants.PARAM_TRANSPORT_TYPE);
        if (TextUtils.isEmpty(transportType) || transportType.equals("null"))
            transportType = "";

        transportFee = json.optInt(ApiConstants.PARAM_TRANSPORT_FEE);
    }
}
