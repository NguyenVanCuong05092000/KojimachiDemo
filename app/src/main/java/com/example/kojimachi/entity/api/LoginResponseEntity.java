package com.example.kojimachi.entity.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginResponseEntity {
    public int id;
    public String loginId;
    public int userId;
    public int role;
    public int employmentStatus;
    public String modified;
    public String accessToken;
    public String userName;
    public String phone;
    public String createAt;
    public JSONObject dataShifts;
    public JSONArray listCars;

    public LoginResponseEntity(int id, String loginId, int userId, int role, int employmentStatus, String modified, String accessToken, String userName, String phone, String createAt, JSONObject dataShifts, JSONArray listCars) {
        this.id = id;
        this.loginId = loginId;
        this.userId = userId;
        this.role = role;
        this.employmentStatus = employmentStatus;
        this.modified = modified;
        this.accessToken = accessToken;
        this.userName = userName;
        this.phone = phone;
        this.createAt = createAt;
        this.dataShifts = dataShifts;
        this.listCars = listCars;
    }
}
