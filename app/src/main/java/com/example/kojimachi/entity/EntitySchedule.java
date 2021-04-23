package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.kojimachi.constant.ApiConstants;

public class EntitySchedule {

    public int scheduleId;
    public String timeStart;
    public int hourStart;
    public int minuteStart;
    public String displayTimeStart;
    public String facility;
    public String course;
    public int status;
    public String shop;
    public String therapist;
    public String idTranfers;
    public String locationShop;

    public EntitySchedule(JSONObject json, SimpleDateFormat dateFormat, Calendar tmpCalendar) {
        scheduleId = json.optInt(ApiConstants.PARAM_ID_SCHEDULE);

        facility = json.optString(ApiConstants.PARAM_FACILITY);
        if (TextUtils.isEmpty(facility) || facility.equals("null"))
            facility = "";

        timeStart = json.optString(ApiConstants.PARAM_TIME_START);
        if (TextUtils.isEmpty(timeStart) || timeStart.equals("null"))
            timeStart = "";

        int[] value = getDateTimeValues(timeStart, dateFormat, tmpCalendar);
        hourStart = value[0];
        minuteStart = value[1];
        displayTimeStart = getDisplayTimeStart();

        course = json.optString(ApiConstants.PARAM_COURSE);
        if (TextUtils.isEmpty(course) || course.equals("null"))
            course = "";

        status = json.optInt(ApiConstants.PARAM_STATUS_RESERVE_COURSE);

        shop = json.optString(ApiConstants.PARAM_SHOP);
        if (TextUtils.isEmpty(shop) || shop.equals("null"))
            shop = "";

        therapist = json.optString(ApiConstants.PARAM_THERAPIST);
        if (TextUtils.isEmpty(therapist) || therapist.equals("null"))
            therapist = "";

        idTranfers = json.optString(ApiConstants.PARAM_ID_TRANFERS);
        if (TextUtils.isEmpty(idTranfers) || idTranfers.equals("null"))
            idTranfers = "";

        locationShop = json.optString(ApiConstants.PARAM_LOCATION_SHOP);
        if (TextUtils.isEmpty(locationShop) || locationShop.equals("null"))
            locationShop = "";
    }

    private int[] getDateTimeValues(String dateTimeData, SimpleDateFormat dateFormat, Calendar tmpCalendar) {
        try {
            if (!TextUtils.isEmpty(dateTimeData)) {
                tmpCalendar.setTime(dateFormat.parse(dateTimeData));
                return new int[]{tmpCalendar.get(Calendar.HOUR_OF_DAY), tmpCalendar.get(Calendar.MINUTE)};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    private String getShortDateTimeText(int h, int m) {
        return (h > 9 ? h : "0" + h) + ":" + (m > 9 ? m : "0" + m);
    }

    public String getDisplayTimeStart() {
        return getShortDateTimeText(hourStart, minuteStart);
    }

}
