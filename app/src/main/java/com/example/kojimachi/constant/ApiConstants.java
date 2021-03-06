package com.example.kojimachi.constant;

public interface ApiConstants {
    String API_KEY = AppConstants.TEST_MODE ? "z95NTnNlSzHYV0VJsCWPdzPS2uw+gqLgtR7Fn3/tII5Iw+a5H1C4AuL5QP+Dh7y3" : "n4oJCHEpHXeACEJMtWTHLyeB1/921+HjpxbezGHpM4BSRoeNZ8+4ZegBLPn2bI1T";
    String API_URL_VERSION = "v1/";
    String API_URL = (AppConstants.TEST_MODE ? "http://kojimachi.brite.vn/api/" : "http://kojimachi.brite.vn/api/") + API_URL_VERSION;
    String API_USER = "users/";
    String API_LOGIN = API_URL + API_USER + "login";
    String API_LOGOUT = API_URL + API_USER + "logout";
    String API_GET_LIST_SCHEDULE = API_URL + "top";
    String API_GET_DETAIL_SCHEDULE = API_URL + "reserve";
    String API_UPDATE_OPTION = API_URL + "reserve/update-bonus";
    String API_UPDATE_STATUS = API_URL + "reserve/update-status";
    String API_GET_DAILY_ACTUARIAL_GET = API_URL + "therapist/payments";
    String API_GET_DAILY_ACTUARIAL_POST = API_URL + "therapist/update-payments";
    String API_GET_DAILY_NEWSPAPER_GET = API_URL + "therapist/reports";
    String API_GET_DAILY_NEWSPAPER_POST = API_URL + "therapist/post-reports";
    String API_UPDATE_STATUS_SHIFT = API_URL + "shift/update-status";
    String API_GET_OPTION_LINEN = API_URL + "list-option-item";
    String API_GET_THERAPIST_SCHEDULE = API_URL + "therapist/schedule";
    String API_POST_THERAPIST_SCHEDULE = API_URL + "therapist/post-schedule";
    String API_GET_LIST_AREA = API_URL + "list-area";
    String API_VIEW_SALES = API_URL + "therapist/view-sales";
    String API_UPDATE_NUMBER_OF_LINEN = API_URL + "therapist/update-numbers-item";
    String API_REG_FiB_TOKEN_PUSH = API_URL + "user/register_token_push/%s";
    String API_TRACKING_LOCATION = API_URL + "location/register_location/%s";
    String API_GET_LIST_CAR = API_URL + "list-cars";
    String API_VEHICLE_SETTINGS = API_URL + "vehicle-settings";
    String API_UPDATE_STATUS_CAR = API_URL + "update-status-car";

    //api version
    int API_VERSION = 1;
    //status response
    int STATUS_OK = 200;
    int STATUS_ERROR_TOKEN = 402;
    int STATUS_ERROR_PASS = 422;
    int STATUS_ERROR = 0;
    int STATUS_ERROR_DATA = 404;
    int STATUS_ERROR_INTERNET = 1;
    //device type
    int ANDROID = 2;

    int STATUS_BOOKING_CONFIRM = 1;
    int STATUS_START_MOVING = 2;
    int STATUS_START_OF_TREATMENT = 3;
    int STATUS_END_OF_TREATMENT = 4;
    int STATUS_CHANGE = 5;
    int STATUS_CREATED = 0;
    int STATUS_EXTENSION_OF_ADD_OPTIONS = 6;

    int MAX_RETRY = 2;

    int STATUS_START_WORKING = 2;
    int STATUS_END_WORKING = 1;
    int STATUS_TEMPORARILY_SAVED = 2;
    int STATUS_PUT_FORWARD = 1;
    int STATUS_START_MOVING_SHIFT = 3;
    int STATUS_WAIT = 6;

    //params
    String PARAM_API_VERSION = "api_version";
    String PARAM_API = "api_key";
    String PARAM_TOKEN = "token";
    String PARAM_TOKEN_TYPE = "token_type";
    String PARAM_DEVICE_UUID = "device_uuid";
    String PARAM_DEVICE_TYPE = "device_type";
    String PARAM_NAME = "name";
    String PARAM_LOGIN_ID = "login_id";
    String PARAM_USER_ID = "user_id";
    String PARAM_EMPLOYMENT_STATUS = "employment_status";
    String PARAM_ROLE = "role";
    String PARAM_MODIFIED = "modified";
    String PARAM_EXPIRES_IN = "expires_in";
    String PARAM_LIST = "list";
    String PARAM_DATE = "date";
    String PARAM_START_TIME = "start_time";
    String PARAM_END_TIME = "end_time";
    String PARAM_LOCATION = "location";
    String PARAM_ID_DATA_COURSE = "id_data_course";
    String PARAM_DATA = "data";
    String PARAM_TOTAL_COMMISSION = "total_comission";
    String PARAM_SALES = "sales";
    String PARAM_LIST_OPTION = "list_option";
    String PARAM_NUMBER = "number";
    String PARAM_PHONE = "phone";
    String PARAM_CAR_NUMBER = "car_number";
    String PARAM_ID_CAR = "id_car";
    String PARAM_CREATE_AT = "create_at";
    String PARAM_TOTAL_SALES = "total_sales";
    String PARAM_FACILITY_DEPOSIT = "facility_deposit";
    String PARAM_NAME_JAPAN_FACILITY_DEPOSIT = "name_japan_facility_deposit";
    String PARAM_FEE_PLACEMENT = "fee_placement";
    String PARAM_NAME_JAPAN_FEE_PLACEMENT = "name_japan_fee_placement";
    String PARAM_CASH_TAKE_OUT = "cash_take_out";
    String PARAM_NAME_JAPAN_CASH_TAKE_OUT = "name_japan_cash_take_out";
    String PARAM_CASH_DEPOSIT_SLIP = "cash_deposit_slip";
    String PARAM_NAME_JAPAN_CASH_DEPOSIT_SLIP = "name_japan_cash_deposit_slip";
    String PARAM_ROOM_ATTACHMENT = "room_attachment";
    String PARAM_NAME_JAPAN_ROOM_ATTACHMENT = "name_japan_room_attachment";
    String PARAM_ROOM_SLIP = "room_slip";
    String PARAM_NAME_JAPAN_ROOM_SLIP = "name_japan_room_slip";
    String PARAM_TAKE_OUT_CASH_TOTAL = "take_out_money";
    String PARAM_INSUFFICIENT_CASH = "actual_money";
    String PARAM_ACTUAL_CASH_ON_HAND = "excess_money";
    String PARAM_DATA_SHIFTS = "data_shifts";
    String PARAM_LIST_CARS = "list_cars";

    String PARAM_PASSWORD = "password";
    String PARAM_STATUS = "status";
    String PARAM_THERAPIST_STATUS = "therapist_status";
    String PARAM_MESSAGE = "message";
    String PARAM_RESPONSE = "response";
    String PARAM_ID = "id";
    String PARAM_LAT = "lat";
    String PARAM_LNG = "lng";
    String PARAM_TOTAL = "total";
    String PARAM_ERRORS = "errors";
    String PARAM_FIREBASE_TOKEN = "firebase_token";
    String PARAM_CORPORATES_ID = "corporates_id";
    String PARAM_DAY = "day";
    String PARAM_ID_SCHEDULE = "id_reserve_course";
    String PARAM_LIST_RESERVE = "list_reserve";
    String PARAM_STR_TIME_START = "str_time_start";
    String PARAM_TIME_START = "time_start";
    String PARAM_TIME_END = "time_end";
    String PARAM_ID_SHOP = "id_shop";
    String PARAM_NAME_SHOP = "name_shop";
    String PARAM_ID_DATA_CHARGE = "id_data_charge";
    String PARAM_NAME_DATA_CHARGE = "name_data_charge";
    String PARAM_FACILITY = "shop_name";
    String PARAM_COURSE = "charge_name";
    String PARAM_TOTAL_PRICE = "total_price";
    String PARAM_PRICE_COMMISSION = "price_comission";
    String PARAM_YEAR = "year";
    String PARAM_MONTH = "month";
    String PARAM_WORK_START = "work_start";
    String PARAM_WORK_END = "work_end";
    String PARAM_WORK_OUT = "work_out";
    String PARAM_WORK_IN = "work_in";
    String PARAM_HOLIDAY = "holiday";
    String PARAM_PAID_HOLIDAY = "paid_holidays";
    String PARAM_ID_AREA = "id_area";
    String PARAM_AREA_NAME = "area_name";
    String PARAM_CONTENT = "content";
    String PARAM_CONTENT_TEMPORARY = "content_temporary";
    String PARAM_REPORT = "report";
    String PARAM_SHOP = "shop";
    String PARAM_THERAPIST = "therapist";
    String PARAM_ID_TRANFERS = "id_tranfers";
    String PARAM_LOCATION_SHOP = "location_shop";

    String PARAM_DATE_RESERVE = "date_reserve";
    String PARAM_TIME_RESERVE = "time_reserve";
    String PARAM_PLACE_ROOM = "place_room";
    String PARAM_COURSE_TYPE = "course_type";
    String PARAM_COURSE_NAME = "course_name";
    String PARAM_COURSE_TIME = "course_time";
    String PARAM_TIME_BONUS = "time_bonus";
    String PARAM_OPTION = "option";
    String PARAM_ID_OPTION = "id_option";
    String PARAM_GENDER = "gender";
    String PARAM_COURSE_TYPE_GENDER = "course_type_gender";
    String PARAM_PRICE_RESERVE_COURSE = "price_reserve_course";
    String PARAM_CUSTOMER_NAME = "customer_name";
    String PARAM_RECEIPT_REQUIRED = "receipt_required";
    String PARAM_HANDFORCE = "handforce";
    String PARAM_OTHER_REMARKS = "other_remarks";
    String PARAM_TIME_OPTION_BONUS_CHARGE = "time_option_bonus_charge";
    String PARAM_OPTION_BONUS_CHARGE = "option_bonus_charge";
    String PARAM_OPTION_NAME = "name_option";
    String PARAM_TIME_OPTION = "time_option";
    String PARAM_PAYMENT_TYPE = "payment_type";
    String PARAM_NAME_PAYMENT_TYPE = "name_payment_type";
    String PARAM_OPTION_TIME = "time";
    String PARAM_PAYMENT = "payment";
    String PARAM_OPTION_PRICE = "price";
    String PARAM_SHOP_KIND = "shop_kind";
    String PARAM_TRANSPORTATION = "transportation";
    String PARAM_TRANSPORT_TYPE = "transport_type";
    String PARAM_TRANSPORT_FEE = "transport_fee";
    String PARAM_COURSE_DATA_CHARGE_FEE = "course_data_charge_fee";
    String PARAM_TREATMENT_TIME = "treatment_time";
    String PARAM_STATUS_RESERVE_COURSE = "status_reserve_course";
    String PARAM_PRICE_ONTAX_RESERVE_COURSE = "price_ontax_reserve_course";
    String PARAM_TOTAL_TAKE_OUT_MONEY = "total_take_out_money";
    String PARAM_FEE_PAYMENT_TYPE = "fee_payment_type";
    String PARAM_FEE_TOTAL_CASH = "fee_total_cash_take_out";
    String PARAM_FEE_PAYMENT = "fee_payment";
    String PARAM_TIME_END_COURSE = "time_end_course_reserve";
    String PARAM_ARR_OPTION = "arr_option";
    String PARAM_KIND_CHARGE = "kind_charge";
}
