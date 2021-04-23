package com.example.kojimachi.listener;

import com.example.kojimachi.entity.EntityDailyActuarial;

public interface CallbackGetDetailActuarial {
    void onSuccessGetDetailActuarial(EntityDailyActuarial entityDailyActuarial);

    void onFailedGetDetailActuarial();
}
