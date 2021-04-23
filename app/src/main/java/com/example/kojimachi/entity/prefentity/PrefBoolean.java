package com.example.kojimachi.entity.prefentity;

public class PrefBoolean extends PrefValue {
    public boolean value;

    public PrefBoolean(String key, boolean value) {
        this.key = key;
        this.value = value;
    }
}
