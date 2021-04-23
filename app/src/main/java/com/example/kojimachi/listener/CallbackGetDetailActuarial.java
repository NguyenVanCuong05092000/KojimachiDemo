package com.example.kojimachi.listener;

import jp.co.kojimachi.entity.EntityDailyActuarial;

public interface CallbackGetDetailActuarial {
    void onSuccessGetDetailActuarial(EntityDailyActuarial entityDailyActuarial);

    void onFailedGetDetailActuarial();
}
