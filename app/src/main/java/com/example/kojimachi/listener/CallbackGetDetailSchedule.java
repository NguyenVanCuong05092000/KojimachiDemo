package com.example.kojimachi.listener;

import com.example.kojimachi.entity.EntityBookingConfirmation;

public interface CallbackGetDetailSchedule {
    void onSuccessGetDetailSchedule(EntityBookingConfirmation entityBookingConfirmation);
    void onFailedGetSchedule();
}
