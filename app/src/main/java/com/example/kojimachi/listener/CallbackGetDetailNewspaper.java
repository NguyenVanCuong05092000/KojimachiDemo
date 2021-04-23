package com.example.kojimachi.listener;

import com.example.kojimachi.entity.EntityDailyNewspaper;

public interface CallbackGetDetailNewspaper {
    void onSuccessGetDetailNewspaper(EntityDailyNewspaper entityDailyNewspaper);

    void onFailedGetDetailNewspaper();
}
