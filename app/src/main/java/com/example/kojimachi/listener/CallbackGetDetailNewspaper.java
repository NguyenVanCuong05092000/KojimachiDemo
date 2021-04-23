package com.example.kojimachi.listener;

import jp.co.kojimachi.entity.EntityDailyNewspaper;

public interface CallbackGetDetailNewspaper {
    void onSuccessGetDetailNewspaper(EntityDailyNewspaper entityDailyNewspaper);

    void onFailedGetDetailNewspaper();
}
