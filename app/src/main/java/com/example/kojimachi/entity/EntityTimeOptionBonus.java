package com.example.kojimachi.entity;

import org.json.JSONObject;

import jp.co.kojimachi.constant.ApiConstants;

public class EntityTimeOptionBonus {
    public int timeBonus;

    public EntityTimeOptionBonus(JSONObject json) {
        timeBonus = json.optInt(ApiConstants.PARAM_TIME_BONUS);
    }
}
