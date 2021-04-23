package com.example.kojimachi.entity;

import org.json.JSONObject;

import static jp.co.kojimachi.constant.ApiConstants.PARAM_ID;
import static jp.co.kojimachi.constant.ApiConstants.PARAM_NAME;

public class EntityOptionOfLinen {
    public int id;
    public String nameOption;

    public EntityOptionOfLinen(JSONObject jsonObject) {
        id = jsonObject.optInt(PARAM_ID);
        nameOption = jsonObject.optString(PARAM_NAME);
    }
}
