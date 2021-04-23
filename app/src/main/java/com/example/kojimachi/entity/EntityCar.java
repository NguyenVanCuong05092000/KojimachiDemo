package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

import jp.co.kojimachi.constant.ApiConstants;

public class EntityCar implements Serializable {
    public int id;
    public String carNumber;

    public EntityCar(JSONObject json) {

        id = json.optInt(ApiConstants.PARAM_ID);

        carNumber = json.optString(ApiConstants.PARAM_CAR_NUMBER);
        if (TextUtils.isEmpty(carNumber) || carNumber.equals("null"))
            carNumber = "";

    }
}
