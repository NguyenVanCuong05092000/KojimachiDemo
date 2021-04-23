package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

import com.example.kojimachi.constant.ApiConstants;

public class EntityArea implements Serializable {
    public int id;
    public String name;

    public EntityArea(JSONObject json) {

        id = json.optInt(ApiConstants.PARAM_ID);

        name = json.optString(ApiConstants.PARAM_NAME);
        if (TextUtils.isEmpty(name) || name.equals("null"))
            name = "";

    }
}
