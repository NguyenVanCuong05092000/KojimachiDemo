package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import com.example.kojimachi.constant.ApiConstants;

public class EntityReceiptRequired {
    public String name;

    public EntityReceiptRequired(JSONObject jsonObject) {
        name = jsonObject.optString(ApiConstants.PARAM_NAME);
        if (TextUtils.isEmpty(name) || name.equals("null"))
            name = "";
    }
}
