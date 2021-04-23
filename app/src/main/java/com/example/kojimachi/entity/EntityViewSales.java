package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.AppConstants;

public class EntityViewSales {
    public int dateYear;
    public int dateMonth;
    public int dateDay;
    public String workDate;
    public String displayDateTime;
    public String totalCommission;
    public ArrayList<EntityViewSalesSub>  viewSalesSubArrayList;

    public EntityViewSales(JSONObject jsonObject, Calendar tmpCalendar) {
        workDate = jsonObject.optString(ApiConstants.PARAM_DATE);
        if (TextUtils.isEmpty(workDate) || workDate.equals("null"))
            workDate = "";
        int[] value = getDateValues(workDate, getDateFormat(), tmpCalendar);
        dateYear = value[0];
        dateMonth = value[1];
        dateDay = value[2];
        displayDateTime = getShortDateTime();
        totalCommission = jsonObject.optString(ApiConstants.PARAM_TOTAL_COMMISSION);
        if (TextUtils.isEmpty(totalCommission) || totalCommission.equals("null"))
            totalCommission = "";

        totalCommission = jsonObject.optString(ApiConstants.PARAM_TOTAL_COMMISSION);
        if (TextUtils.isEmpty(totalCommission) || totalCommission.equals("null"))
            totalCommission = "";

        try {
            JSONArray jsonArray = new JSONArray(jsonObject.optString(ApiConstants.PARAM_SALES));
            if (jsonArray.length() > 0) {
                viewSalesSubArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityViewSalesSub  entityViewSalesSub = new EntityViewSalesSub(jsonArray.getJSONObject(i));
                    if (i == 0)
                        entityViewSalesSub.isStart = true;
                    if (i == jsonArray.length() - 1)
                        entityViewSalesSub.isEnd = true;
                    viewSalesSubArrayList.add(entityViewSalesSub);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private SimpleDateFormat dateFormat;
    public SimpleDateFormat getDateFormat() {
        dateFormat = new SimpleDateFormat(AppConstants.DATE_FORMAT);
        return dateFormat;
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
    private String getShortDateTimeText(int y, int M, int d) {
        if (y < 1000)
            return "";
        return y
                + "/" + (M > 9 ? M : "0" + M)
                + "/" + (d > 9 ? d : "0" + d);
    }

    public String getShortDateTime() {
        return getShortDateTimeText(dateYear, dateMonth, dateDay);
    }
}
