package com.example.kojimachi.entity;

public class EntityShiftTable {
    public int date;
    public String attendanceStatus;
    public String attendanceTime;
    public String leaveTime;
    public String standby;
    public int paidHoliday;
    public int holiday;
    public String idArea;
    public int month;
    public int year;

    public EntityShiftTable(int date, String attendanceStatus, String attendanceTime, String leaveTime, String standby, int paidHoliday, int holiday, String idArea, int month, int year) {
        this.date = date;
        this.attendanceStatus = attendanceStatus;
        this.attendanceTime = attendanceTime;
        this.leaveTime = leaveTime;
        this.standby = standby;
        this.paidHoliday = paidHoliday;
        this.holiday = holiday;
        this.idArea = idArea;
        this.month = month;
        this.year = year;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public void setStandby(String standby) {
        this.standby = standby;
    }
}
