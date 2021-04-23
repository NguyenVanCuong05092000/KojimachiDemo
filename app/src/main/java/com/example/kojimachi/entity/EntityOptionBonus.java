package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import com.example.kojimachi.constant.ApiConstants;

public class EntityOptionBonus {
    public int id;
    public String nameOption;
    public boolean isCheck = false;
    public int timeOption;
    public int numberOption;
    public int kindCharge;
    public boolean isCheckKindCharge = true;

    public EntityOptionBonus(JSONObject json) {

        id = json.optInt(ApiConstants.PARAM_ID);

        nameOption = json.optString(ApiConstants.PARAM_OPTION_NAME);
        if (TextUtils.isEmpty(nameOption) || nameOption.equals("null"))
            nameOption = "";

        numberOption = json.optInt(ApiConstants.PARAM_NUMBER);
        timeOption = json.optInt(ApiConstants.PARAM_TIME_OPTION);
        kindCharge = json.optInt(ApiConstants.PARAM_KIND_CHARGE);
    }
}
