package com.example.kojimachi.entity;

import java.io.Serializable;

public class EntityArrayAdapter implements Serializable {
    public String name;
    public boolean isSelect;

    public EntityArrayAdapter(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }
}
