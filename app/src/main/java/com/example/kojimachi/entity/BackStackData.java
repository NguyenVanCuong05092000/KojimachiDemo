package com.example.kojimachi.entity;

import java.io.Serializable;
import java.util.HashMap;

public class BackStackData implements Serializable {
    private static final long serialVersionUID = -3723697355263419146L;
    public int fromFragment;
    public HashMap<String, Object> mapData;

    public BackStackData(int from) {
        fromFragment = from;
    }

    public BackStackData(int from, HashMap<String, Object> mapData) {
        fromFragment = from;
        this.mapData = mapData;
    }
}
