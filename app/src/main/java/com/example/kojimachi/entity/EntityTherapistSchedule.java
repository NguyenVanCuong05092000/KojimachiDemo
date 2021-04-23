package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.kojimachi.constant.ApiConstants;

public class EntityTherapistSchedule implements Serializable {
    public String year;
    public String month;
    public String day;
    public String workStart;
    public String workEnd;
    public int holiday;
    public int paidHoliday;
    public String areaName;
    public String idArea;

    public EntityTherapistSchedule(JSONObject json, SimpleDateFormat dateFormat, Calendar tmpCalendar) {

        year = json.optString(ApiConstants.PARAM_YEAR);
        if (TextUtils.isEmpty(year) || year.equals("null"))
            year = "";

        month = json.optString(ApiConstants.PARAM_MONTH);
        if (TextUtils.isEmpty(month) || month.equals("null"))
            month = "";

        day = json.optString(ApiConstants.PARAM_DAY);
        if (TextUtils.isEmpty(day) || day.equals("null"))
            day = "";

        workStart = json.optString(ApiConstants.PARAM_WORK_START);
        if (TextUtils.isEmpty(workStart) || workStart.equals("null") || workStart.equals(":"))
            workStart = "";

        workEnd = json.optString(ApiConstants.PARAM_WORK_END);
        if (TextUtils.isEmpty(workEnd) || workEnd.equals("null") || workStart.equals(":"))
            workEnd = "";

//        workIn = json.optString(ApiConstants.PARAM_WORK_IN);
//        if (TextUtils.isEmpty(workIn) || workIn.equals("null"))
//            workIn = "";
//        int[] value = getDateTimeValues(workIn, dateFormat, tmpCalendar);
//        workInYear = value[0];
//        workInMonth = value[1];
//        workInDay = value[2];
//        workInHour = value[3];
//        workInMinute = value[4];
//        workInSecond = value[5];
//        displayWorkIn = getDisplayWorkIn();
//
//        workOut = json.optString(ApiConstants.PARAM_WORK_OUT);
//        if (TextUtils.isEmpty(workOut) || workOut.equals("null"))
//            workOut = "";
//        int[] value1 = getDateTimeValues(workOut, dateFormat, tmpCalendar);
//        workOutYear = value1[0];
//        workOutMonth = value1[1];
//        workOutDay = value1[2];
//        workOutHour = value1[3];
//        workOutMinute = value1[4];
//        workOutSecond = value1[5];
//        displayWorkOut = getDisplayWorkOut();

        holiday = json.optInt(ApiConstants.PARAM_HOLIDAY);

        paidHoliday = json.optInt(ApiConstants.PARAM_PAID_HOLIDAY);

        idArea = json.optString(ApiConstants.PARAM_ID_AREA);
        if (TextUtils.isEmpty(idArea) || idArea.equals("null"))
            idArea = "";

        areaName = json.optString(ApiConstants.PARAM_AREA_NAME);
        if (TextUtils.isEmpty(areaName) || areaName.equals("null"))
            areaName = "";
    }

    private int[] getDateTimeValues(String dateTimeData, SimpleDateFormat dateFormat, Calendar tmpCalendar) {
        try {
            if (!TextUtils.isEmpty(dateTimeData)) {
                tmpCalendar.setTime(dateFormat.parse(dateTimeData));
                return new int[]{tmpCalendar.get(Calendar.YEAR), tmpCalendar.get(Calendar.MONTH) + 1,
                        tmpCalendar.get(Calendar.DAY_OF_MONTH), tmpCalendar.get(Calendar.HOUR_OF_DAY),
                        tmpCalendar.get(Calendar.MINUTE), tmpCalendar.get(Calendar.SECOND)};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new int[]{0, 0, 0, 0, 0, 0};
    }

    private String getShortDateTimeText(int y, int M, int d, int h, int m, int s) {
        if (y < 1000)
            return "";
        return y
                + "-" + (M > 9 ? M : "0" + M)
                + "-" + (d > 9 ? d : "0" + d)
                + " " + (h > 9 ? h : "0" + h)
                + ":" + (m > 9 ? m : "0" + m)
                + ":" + (s > 9 ? s : "0" + s);
    }

//    public String getDisplayWorkIn() {
//        return getShortDateTimeText(workInYear, workInMonth, workInDay,
//                workInHour, workInMinute, workInSecond);
//    }
//
//    public String getDisplayWorkOut() {
//        return getShortDateTimeText(workOutYear, workOutMonth, workOutDay,
//                workOutHour, workOutMinute, workOutSecond);
//    }

}
