package com.example.kojimachi.entity;

import androidx.annotation.NonNull;

public class ApiResult {
    public int statusCode;
    public String message;
    public String token;
    public Object response;
    public int total;

    public ApiResult(int statusCode) {
        this.statusCode = statusCode;
    }

    public ApiResult(int statusCode, String message, String token,
                     Object response, int total) {
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
        this.response = response;
        this.total = total;
    }

    @NonNull
    @Override
    public String toString() {
        return "statusCode:" + statusCode + " - token:" + token + " - message:" + message;
    }
}
