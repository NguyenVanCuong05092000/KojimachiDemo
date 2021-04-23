package com.example.kojimachi.entity;

import java.io.Serializable;

public class EntityFeeDailyActuarial implements Serializable {

    public String title;
    public String fee;

    public EntityFeeDailyActuarial(String title, String fee) {
        this.title = title;
        this.fee = fee;
    }
}
