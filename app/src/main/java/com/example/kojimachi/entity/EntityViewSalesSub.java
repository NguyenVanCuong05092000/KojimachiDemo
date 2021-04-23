package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import jp.co.kojimachi.constant.ApiConstants;

import static jp.co.kojimachi.constant.ApiConstants.PARAM_ID;

public class EntityViewSalesSub {
    public int id;
    public int treatmentTime;
    public String timeStart;
    public String timeEnd;
    public String nameDataCharge;
    public int idDataCharge;
    public int idShop;
    public String nameShop;
    public int totalPrice;
    public int priceCommission;
    public boolean isStart;
    public boolean isEnd;


    public EntityViewSalesSub(JSONObject jsonObject) {
        id = jsonObject.optInt(PARAM_ID);

        treatmentTime = jsonObject.optInt(ApiConstants.PARAM_TREATMENT_TIME);

        timeStart = jsonObject.optString(ApiConstants.PARAM_TIME_START);
        if (TextUtils.isEmpty(timeStart) || timeStart.equals("null"))
            timeStart = "";

        timeEnd = jsonObject.optString(ApiConstants.PARAM_TIME_END);
        if (TextUtils.isEmpty(timeEnd) || timeEnd.equals("null"))
            timeEnd = "";

        idShop = jsonObject.optInt(ApiConstants.PARAM_ID_SHOP);

        nameShop = jsonObject.optString(ApiConstants.PARAM_NAME_SHOP);
        if (TextUtils.isEmpty(nameShop) || nameShop.equals("null"))
            nameShop = "";

        idDataCharge = jsonObject.optInt(ApiConstants.PARAM_ID_DATA_CHARGE);

        nameDataCharge = jsonObject.optString(ApiConstants.PARAM_NAME_DATA_CHARGE);
        if (TextUtils.isEmpty(nameDataCharge) || nameDataCharge.equals("null"))
            nameDataCharge = "";

        totalPrice = jsonObject.optInt(ApiConstants.PARAM_TOTAL_PRICE);

        priceCommission = jsonObject.optInt(ApiConstants.PARAM_PRICE_COMMISSION);
    }


}
