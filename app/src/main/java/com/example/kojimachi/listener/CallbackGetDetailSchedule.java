package com.example.kojimachi.listener;

import jp.co.kojimachi.entity.EntityBookingConfirmation;

public interface CallbackGetDetailSchedule {
    void onSuccessGetDetailSchedule(EntityBookingConfirmation entityBookingConfirmation);
    void onFailedGetSchedule();
}
