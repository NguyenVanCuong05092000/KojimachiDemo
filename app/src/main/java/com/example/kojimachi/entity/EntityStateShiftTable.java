package com.example.kojimachi.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class EntityStateShiftTable implements Serializable {
    public String monthAndYear;
    public String startTime;
    public String endTime;
    public String waitingPlace;
    public ArrayList<EntityShiftTable> shiftTableArrayList;

    public EntityStateShiftTable() {
    }
}
