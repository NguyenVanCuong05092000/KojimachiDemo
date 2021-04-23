package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.AppConstants;

public class EntityDailyNewspaper {
    public String date;
    public int dateYear;
    public int dateMonth;
    public int dateDay;
    public String timeStart;
    public int timeStartHour;
    public int timeStartMinute;
    public String displayDateTime;
    public String shopName;
    public String courseName;
    public int treatmentTime;
    public String customerName;
    public JSONObject report;
    public int status;
    public String content;

    public EntityDailyNewspaper() {
    }

    public EntityDailyNewspaper(JSONObject json, Calendar tmpCalendar) {
        date = json.optString(ApiConstants.PARAM_DATE);
        if (TextUtils.isEmpty(date) || date.equals("null"))
            date = "";
        int[] value = getDateValues(date, getDateFormat(), tmpCalendar);
        dateYear = value[0];
        dateMonth = value[1];
        dateDay = value[2];

        timeStart = json.optString(ApiConstants.PARAM_TIME_START);
        if (TextUtils.isEmpty(timeStart) || timeStart.equals("null"))
            timeStart = "";
        int[] value1 = getTimeValues(timeStart, getTimeFormat(), tmpCalendar);
        timeStartHour = value1[0];
        timeStartMinute = value1[1];

        displayDateTime = getShortDateTime();

        shopName = json.optString(ApiConstants.PARAM_FACILITY);
        if (TextUtils.isEmpty(shopName) || shopName.equals("null"))
            shopName = "";

        courseName = json.optString(ApiConstants.PARAM_COURSE_NAME);
        if (TextUtils.isEmpty(courseName) || courseName.equals("null"))
            courseName = "";

        treatmentTime = json.optInt(ApiConstants.PARAM_TREATMENT_TIME);

        customerName = json.optString(ApiConstants.PARAM_CUSTOMER_NAME);
        if (TextUtils.isEmpty(customerName) || customerName.equals("null"))
            customerName = "";

        try {
            report = json.optJSONObject(ApiConstants.PARAM_REPORT);
            if (report != null) {
                status = report.optInt(ApiConstants.PARAM_STATUS);
                if (status == ApiConstants.STATUS_PUT_FORWARD){
                    content = report.optString(ApiConstants.PARAM_CONTENT);
                }
                else{
                    content = report.optString(ApiConstants.PARAM_CONTENT_TEMPORARY);
                }
                if (TextUtils.isEmpty(content) || content.equals("null"))
                    content = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] getDateValues(String dateTimeData, SimpleDateFormat dateFormat, Calendar tmpCalendar) {
        try {
            if (!TextUtils.isEmpty(dateTimeData)) {
                tmpCalendar.setTime(dateFormat.parse(dateTimeData));
                return new int[]{tmpCalendar.get(Calendar.YEAR), tmpCalendar.get(Calendar.MONTH) + 1,
                        tmpCalendar.get(Calendar.DAY_OF_MONTH),};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new int[]{0, 0, 0};
    }

    private int[] getTimeValues(String dateTimeData, SimpleDateFormat dateFormat, Calendar tmpCalendar) {
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

    private SimpleDateFormat dateFormat;

    public SimpleDateFormat getDateFormat() {
        dateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT);
        return dateFormat;
    }

    public SimpleDateFormat getTimeFormat() {
        dateFormat = new SimpleDateFormat(AppConstants.TIME_FORMAT);
        return dateFormat;
    }

    private String getShortDateTimeText(int y, int M, int d, int h, int m) {
        if (y < 1000)
            return "";
        return y
                + "/" + (M > 9 ? M : "0" + M)
                + "/" + (d > 9 ? d : "0" + d)
                + " " + (h > 9 ? h : "0" + h)
                + ":" + (m > 9 ? m : "0" + m);
    }

    public String getShortDateTime() {
        return getShortDateTimeText(dateYear, dateMonth, dateDay,
                timeStartHour, timeStartMinute);
    }
}
