package com.example.kojimachi.listener;

import java.util.ArrayList;

import com.example.kojimachi.entity.EntityCar;

public interface CallbackGetListCar {
    void onGetListCarSuccess(ArrayList<EntityCar> entityCars);
}
