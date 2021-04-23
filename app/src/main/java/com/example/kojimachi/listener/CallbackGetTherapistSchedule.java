package com.example.kojimachi.listener;

import java.util.ArrayList;

import com.example.kojimachi.entity.EntityTherapistSchedule;

public interface CallbackGetTherapistSchedule {
    void onSuccessGetTherapistSchedule(ArrayList<EntityTherapistSchedule> entityTherapistSchedules);

    void onFailedGetTherapistSchedule();
}
