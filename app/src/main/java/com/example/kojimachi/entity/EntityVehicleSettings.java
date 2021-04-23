package com.example.kojimachi.entity;

public class EntityVehicleSettings {
    public String boardingVehicle;
    public String nameDriver;
    public String phoneNumber;
    public int positionCar;

    public EntityVehicleSettings(String boardingVehicle, String nameDriver, String phoneNumber, int positionCar) {
        this.boardingVehicle = boardingVehicle;
        this.nameDriver = nameDriver;
        this.phoneNumber = phoneNumber;
        this.positionCar = positionCar;
    }
}
