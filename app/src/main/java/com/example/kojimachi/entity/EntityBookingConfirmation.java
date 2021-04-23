package com.example.kojimachi.entity;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.utils.AppUtils;

public class EntityBookingConfirmation implements Serializable {
    public int scheduleId;
    public String serviceCharge;
    public String infoExtend;
    public String feeTaxIncluded;
    public String titleFeePayment;
    public String cashReceive;
    public String feePlacement;
    public String takeOutCash;
    public int state;
    public String dateReserve;
    public int dateReserveYear;
    public int dateReserveMonth;
    public int dateReserveDay;
    public String timeReserve;
    public int timeReserveHour;
    public int timeReserveMinute;
    public String displayDateTimeReserve;
    public String facility;
    public String placeRoom;
    public String titleHotel;
    public String timeEnd;
    public int minutesExtra = 0;
    public String courseType;
    public String courseName;
    public String courseTime;
    public String timeBonus;
    public ArrayList<EntityOption> listOptions = new ArrayList<>();
    public String option;
    public String courseTypeGender;
    public String customerName;
    public ArrayList<EntityTransportation> entityTransportations;
    public String transportation = "";
    public int transportationFee;
    public ArrayList<String> receiptRequired;
    public ArrayList<Integer> timeOptionBonus;
    public ArrayList<EntityFeePayment> entityFeePayments = new ArrayList<>();
    public ArrayList<EntityOptionBonus> optionBonus = new ArrayList<>();
    public String handForce;
    public String otherRemarks;
    public String courseDataChargeFee;
    public int status;
    public int scrolledY = 0;

    public boolean isStarted() {
        return state != 0;
    }

    public EntityBookingConfirmation(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public EntityBookingConfirmation(JSONObject json, Calendar tmpCalendar) {
        dateReserve = json.optString(ApiConstants.PARAM_DATE_RESERVE);
        if (TextUtils.isEmpty(dateReserve) || dateReserve.equals("null"))
            dateReserve = "";
        int[] value = getDateValues(dateReserve, getDateFormat(), tmpCalendar);
        dateReserveYear = value[0];
        dateReserveMonth = value[1];
        dateReserveDay = value[2];

        timeReserve = json.optString(ApiConstants.PARAM_TIME_RESERVE);
        if (TextUtils.isEmpty(timeReserve) || timeReserve.equals("null"))
            timeReserve = "";
        int[] value1 = getTimeValues(timeReserve, getTimeFormat(), tmpCalendar);
        timeReserveHour = value1[0];
        timeReserveMinute = value1[1];

        displayDateTimeReserve = getShortDateTimeReserve();

        titleHotel = json.optString(ApiConstants.PARAM_SHOP_KIND);
        if (TextUtils.isEmpty(titleHotel) || titleHotel.equals("null"))
            titleHotel = "";

        timeEnd = json.optString(ApiConstants.PARAM_TIME_END_COURSE);
        if (TextUtils.isEmpty(timeEnd) || timeEnd.equals("null"))
            timeEnd = "";

        takeOutCash = json.optString(ApiConstants.PARAM_TOTAL_TAKE_OUT_MONEY);
        if (TextUtils.isEmpty(takeOutCash) || takeOutCash.equals("null"))
            takeOutCash = "";

        facility = json.optString(ApiConstants.PARAM_FACILITY);
        if (TextUtils.isEmpty(facility) || facility.equals("null"))
            facility = "";

        placeRoom = json.optString(ApiConstants.PARAM_PLACE_ROOM);
        if (TextUtils.isEmpty(placeRoom) || placeRoom.equals("null"))
            placeRoom = "";

        courseType = json.optString(ApiConstants.PARAM_COURSE_TYPE);
        if (TextUtils.isEmpty(courseType) || courseType.equals("null"))
            courseType = "";

        courseName = json.optString(ApiConstants.PARAM_COURSE_NAME);
        if (TextUtils.isEmpty(courseName) || courseName.equals("null"))
            courseName = "";

        courseTime = json.optString(ApiConstants.PARAM_COURSE_TIME);
        if (TextUtils.isEmpty(courseTime) || courseTime.equals("null"))
            courseTime = "";

        cashReceive = json.optString(ApiConstants.PARAM_PRICE_ONTAX_RESERVE_COURSE);
        if (TextUtils.isEmpty(cashReceive) || cashReceive.equals("null"))
            cashReceive = "";

        timeBonus = json.optString(ApiConstants.PARAM_TIME_BONUS);
        if (TextUtils.isEmpty(timeBonus) || timeBonus.equals("null"))
            timeBonus = "";

        feeTaxIncluded = json.optString(ApiConstants.PARAM_PRICE_ONTAX_RESERVE_COURSE);
        if (TextUtils.isEmpty(feeTaxIncluded) || feeTaxIncluded.equals("null"))
            feeTaxIncluded = "";

        state = json.optInt(ApiConstants.PARAM_STATUS_RESERVE_COURSE);
        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_OPTION));
            if (jsonArray.length() > 0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityOption entityOption = new EntityOption(jsonArray.getJSONObject(i));
                    listOptions.add(entityOption);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        courseTypeGender = json.optString(ApiConstants.PARAM_COURSE_TYPE_GENDER);
        if (TextUtils.isEmpty(courseTypeGender) || courseTypeGender.equals("null"))
            courseTypeGender = "";

        serviceCharge = json.optString(ApiConstants.PARAM_PRICE_RESERVE_COURSE);
        if (TextUtils.isEmpty(serviceCharge) || serviceCharge.equals("null"))
            serviceCharge = "";

        customerName = json.optString(ApiConstants.PARAM_CUSTOMER_NAME);
        if (TextUtils.isEmpty(customerName) || customerName.equals("null"))
            customerName = "";

        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_TRANSPORTATION));
            if (jsonArray.length() > 0) {
                entityTransportations = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityTransportation entityTransportation = new EntityTransportation(jsonArray.getJSONObject(i));
                    entityTransportations.add(entityTransportation);
                }
                getTransportation(entityTransportations);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_FEE_PAYMENT));

            if (jsonArray.length() > 0){
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityFeePayment entityFeePayment = new EntityFeePayment(jsonArray.getJSONObject(i));
                    entityFeePayments.add(entityFeePayment);
                }
               feePayment();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_RECEIPT_REQUIRED));
            if (jsonArray.length() > 0) {
                receiptRequired = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityReceiptRequired  entityReceiptRequired = new EntityReceiptRequired(jsonArray.getJSONObject(i));
                    receiptRequired.add(entityReceiptRequired.name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        handForce = json.optString(ApiConstants.PARAM_HANDFORCE);
        if (TextUtils.isEmpty(handForce) || handForce.equals("null"))
            handForce = "";

        otherRemarks = json.optString(ApiConstants.PARAM_OTHER_REMARKS);
        if (TextUtils.isEmpty(otherRemarks) || otherRemarks.equals("null"))
            otherRemarks = "";

        courseDataChargeFee = json.optString(ApiConstants.PARAM_COURSE_DATA_CHARGE_FEE);
        if (TextUtils.isEmpty(courseDataChargeFee) || courseDataChargeFee.equals("null"))
            courseDataChargeFee = "";

        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_TIME_OPTION_BONUS_CHARGE));
            if (jsonArray.length() > 0) {
                timeOptionBonus = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityTimeOptionBonus entityTimeOptionBonus = new EntityTimeOptionBonus(jsonArray.getJSONObject(i));
                    timeOptionBonus.add(entityTimeOptionBonus.timeBonus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(json.optString(ApiConstants.PARAM_OPTION_BONUS_CHARGE));
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    EntityOptionBonus entityOptionBonus = new EntityOptionBonus(jsonArray.getJSONObject(i));
                    optionBonus.add(entityOptionBonus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        status = json.optInt(ApiConstants.PARAM_STATUS);

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

    public String getShortDateTimeReserve() {
        return getShortDateTimeText(dateReserveYear, dateReserveMonth, dateReserveDay,
                timeReserveHour, timeReserveMinute);
    }

    public String getDataInfoHotel() {
        try {
            return facility + "\n"
                    + placeRoom;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void feePayment(){
        try{
            if (entityFeePayments == null || entityFeePayments.size() == 0 ){
                titleFeePayment = null;
                feePlacement = null;
            }
            else {
                StringBuilder titleBuffer = new StringBuilder();
                StringBuilder totalBuffer = new StringBuilder();
                for (int i = 0; i < entityFeePayments.size(); i++) {
                    if (entityFeePayments.get(i) != entityFeePayments.get(0)){
                        titleBuffer.append("\n");
                        totalBuffer.append("\n");
                    }
                    titleBuffer.append(entityFeePayments.get(i).feePaymentType);
                    totalBuffer.append(AppUtils.formatPrice(entityFeePayments.get(i).payment)).append(" 円");
                }
                titleFeePayment = titleBuffer.toString();
                feePlacement = totalBuffer.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String listNameOption(){
        try {
            if (listOptions == null || listOptions.size() == 0) option = "オプションなし";
            else{
                StringBuffer nameOption = new StringBuffer();
                for (int i = 0; i < listOptions.size() ; i++) {
                    if (listOptions.get(i) != listOptions.get(0)) nameOption.append("\n");
                    nameOption.append(listOptions.get(i).nameOption);
                }
                option = nameOption.toString();
            }
            return option;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private String getAddOptionList(){
        try {
            StringBuilder textOption = new StringBuilder();
            for (int i = 0; i < optionBonus.size(); i++) {
                if (optionBonus.get(i).isCheck){
                    textOption.append("\n");
                    textOption.append(optionBonus.get(i).nameOption);
                }
            }
            if (textOption.toString().equals(""))return "\n" + listNameOption();
            return textOption.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getDataInfoTechnicians() {
        try {
            return courseType + "\n"
                    + courseName + "\n"
                    + getTimeTotal(courseTime, timeBonus, minutesExtra) + " 分"
                    + getAddOptionList() + "\n"
                    + courseTypeGender + "\n"
                    + customerName + "\n"
                    + transportation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDataInfo() {
        try {
            return getReceiptRequired() + "\n"
                    + handForce + "\n"
                    + otherRemarks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTimeTotal(String courseTime, String timeBonus, int minutesExtra) {
        try {
            if (minutesExtra == 0 ) {
                if (courseTime == null || TextUtils.isEmpty(courseTime)){
                    return String.valueOf(AppUtils.parseInt(timeBonus));
                } else if (timeBonus == null || TextUtils.isEmpty(timeBonus)){
                    return String.valueOf(AppUtils.parseInt(courseTime));
                }else return String.valueOf(AppUtils.parseInt(courseTime) + AppUtils.parseInt(timeBonus));
            }
            else {
               return String.valueOf(AppUtils.parseInt(courseTime) + minutesExtra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getReceiptRequired() {
        try {
            StringBuilder text = new StringBuilder();
            if (receiptRequired != null){
                for (String loop : receiptRequired) {
                    text.append(loop);
                }
            }
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getTransportation(ArrayList<EntityTransportation> entity) {
        try {
            for (int i = 0; i < entity.size(); i++) {
                transportation = transportation
                        + ((TextUtils.isEmpty(entity.get(i).transportType) || entity.get(i).transportType.equals("null")) ? "" : entity.get(i).transportType)
                        + "   "
                        + (entity.get(i).transportFee == 0 ? "" : entity.get(i).transportFee + "円\n");
                transportationFee += entity.get(i).transportFee;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
