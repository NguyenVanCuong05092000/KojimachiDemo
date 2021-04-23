package com.example.kojimachi.entity;

public class EntityScheduleDriver {
    public String arrivalTime;
    public String facility;
    public String gettingOn;
    public boolean status;

    public EntityScheduleDriver(String arrivalTime, String facility, String gettingOn, boolean status) {
        this.arrivalTime = arrivalTime;
        this.facility = facility;
        this.gettingOn = gettingOn;
        this.status = status;
    }
}
