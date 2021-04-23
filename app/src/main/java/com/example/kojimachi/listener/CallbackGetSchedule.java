package com.example.kojimachi.listener;

import java.util.ArrayList;

import com.example.kojimachi.entity.EntitySchedule;

public interface CallbackGetSchedule {

    void onSuccessGetListSchedule(ArrayList<EntitySchedule> entitySchedules, String totalPrice, int therapistStatus);

    void onFailedGetSchedule();
}
