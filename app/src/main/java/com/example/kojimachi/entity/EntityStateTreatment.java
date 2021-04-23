package com.example.kojimachi.entity;

public class EntityStateTreatment {
    public String key;
    public boolean state;
    public String values;

    public EntityStateTreatment(String key, boolean state) {
        this.key = key;
        this.state = state;
    }

    public EntityStateTreatment(String key, String values) {
        this.key = key;
        this.values = values;
    }
}
