package com.example.kojimachi.constant;

public enum EnumUrl implements ApiConstants{
    ENUM_API_LOGIN (API_URL + API_USER + "login",1),
    ENUM_API_LOGOUT (API_URL + API_USER + "logout",2),
    ENUM_API_GET_LIST_SCHEDULE (API_URL + "top",3),
    ENUM_API_GET_DETAIL_SCHEDULE (API_URL + "reserve",4),
    ENUM_API_UPDATE_OPTION (API_URL + "reserve/update-bonus",5),
    ENUM_API_UPDATE_STATUS (API_URL + "reserve/update-status",6),
    ENUM_API_GET_DAILY_ACTUARIAL_GET (API_URL + "therapist/payments",7),
    ENUM_API_GET_DAILY_ACTUARIAL_POST (API_URL + "therapist/update-payments",8),
    ENUM_API_GET_DAILY_NEWSPAPER_GET (API_URL + "therapist/reports",9),
    ENUM_API_GET_DAILY_NEWSPAPER_POST (API_URL + "therapist/post-reports",10),
    ENUM_API_UPDATE_STATUS_SHIFT (API_URL + "shift/update-status",11),
    ENUM_API_GET_OPTION_LINEN (API_URL + "list-option-item",12),
    ENUM_API_GET_THERAPIST_SCHEDULE (API_URL + "therapist/schedule",13),
    ENUM_API_POST_THERAPIST_SCHEDULE (API_URL + "therapist/post-schedule",14),
    ENUM_API_GET_LIST_AREA (API_URL + "list-area",15),
    ENUM_API_VIEW_SALES (API_URL + "therapist/view-sales",16),
    ENUM_API_UPDATE_NUMBER_OF_LINEN (API_URL + "therapist/update-numbers-item",17),
    ENUM_API_REG_FiB_TOKEN_PUSH (API_URL + "user/register_token_push/%s",18),
    ENUM_API_TRACKING_LOCATION (API_URL + "location/register_location/%s",19),
    ENUM_API_GET_LIST_CAR (API_URL + "list-cars",20),
    ENUM_API_VEHICLE_SETTINGS (API_URL + "vehicle-settings",21),
    ENUM_API_UPDATE_STATUS_CAR (API_URL + "update-status-car",22),
    ;

    String url;
    int type;
    EnumUrl(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }
}
