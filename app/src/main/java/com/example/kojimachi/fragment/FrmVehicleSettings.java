package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.entity.BackStackData;
import jp.co.kojimachi.entity.EntityArrayAdapter;
import jp.co.kojimachi.entity.EntityCar;
import jp.co.kojimachi.entity.prefentity.PrefInt;
import jp.co.kojimachi.entity.prefentity.PrefString;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.listener.CallbackGetListCar;
import jp.co.kojimachi.utils.AppUtils;
import jp.co.kojimachi.utils.AutoAddTextWatcher;
import jp.co.kojimachi.view.CustomSpinner;

import static jp.co.kojimachi.constant.FragmentConstants.FRM_VEHICLE_SETTINGS;
import static jp.co.kojimachi.constant.PrefConstants.CAR_NUMBER;
import static jp.co.kojimachi.constant.PrefConstants.ID_CAR;
import static jp.co.kojimachi.constant.PrefConstants.NAME;
import static jp.co.kojimachi.constant.PrefConstants.PHONE;


public class FrmVehicleSettings extends BaseFragment implements View.OnClickListener, CallbackGetListCar {

    public static FrmVehicleSettings getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmVehicleSettings fragment = new FrmVehicleSettings();
        Bundle data = new Bundle();
        if (arrayList != null)
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_vehicle_settings;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_VEHICLE_SETTINGS;
    }

    @Override
    protected void finish() {
        activity.backToPreviousFromBackStack(getArguments());
    }

    @Override
    public void backToPrevious() {
        finish();
    }

    @Override
    public boolean isBackPreviousEnable() {
        return true;
    }

    private CustomSpinner tvSpinnerBoardingVehicle;
    private EditText tvDriver;
    private EditText tvPhoneNumber;
    private ArrayList<EntityArrayAdapter> list;
    private ArrayList<EntityCar> entityCars;

    @Override
    protected void loadControlsAndResize(View view) {

        tvSpinnerBoardingVehicle = view.findViewById(R.id.tvSpinnerBoardingVehicle);
        tvSpinnerBoardingVehicle.getLayoutParams().width = activity.getSizeWithScale(213);
        tvSpinnerBoardingVehicle.getLayoutParams().height = activity.getSizeWithScale(46);

        tvDriver = view.findViewById(R.id.tvDriver);
        tvDriver.getLayoutParams().width = activity.getSizeWithScale(213);
        tvDriver.getLayoutParams().height = activity.getSizeWithScale(46);

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.getLayoutParams().width = activity.getSizeWithScale(213);
        tvPhoneNumber.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnRegistration = view.findViewById(R.id.btnRegistration);
        btnRegistration.getLayoutParams().width = activity.getSizeWithScale(146);
        btnRegistration.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnGoBack = view.findViewById(R.id.btnGoBack);
        btnGoBack.getLayoutParams().width = activity.getSizeWithScale(146);
        btnGoBack.getLayoutParams().height = activity.getSizeWithScale(46);

        btnRegistration.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getListCar(true, this);
        setData();

        if (entityCars == null)
            entityCars = new ArrayList<>();

        tvPhoneNumber.addTextChangedListener(new AutoAddTextWatcher(tvPhoneNumber, "-", 3, 7));
    }

    private void setData() {
        idCar = getIDCar();
        setText(tvDriver, activity.prefGetString(NAME));
        setText(tvPhoneNumber, activity.prefGetString(PHONE));
        tvSpinnerBoardingVehicle.setText(activity.prefGetString(CAR_NUMBER));
    }

    int idCar = -1;

    private int getIDCar() {
        return activity.prefGetInt(ID_CAR);
    }

    private boolean isValidatePosition(int position) {
        return entityCars != null && list != null && entityCars.size() == list.size() && position < list.size();
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.btnRegistration:
                String name = tvDriver.getText().toString();
                String phoneNumber = tvPhoneNumber.getText().toString();
                activity.vehicleSettings(idCar, name, phoneNumber, true, new CallbackApi() {
                    @Override
                    public void onFinished(ApiResult result) {
                        activity.prefWriteArr(
                                new PrefString(CAR_NUMBER, tvSpinnerBoardingVehicle.getText()),
                                new PrefString(NAME, name),
                                new PrefInt(ID_CAR, idCar),
                                new PrefString(PHONE, phoneNumber));
                        finish();
                    }
                });
                break;
            case R.id.btnGoBack:
                finish();
                break;
        }
    }

    @Override
    public void onGetListCarSuccess(ArrayList<EntityCar> entityCars) {
        try {
            this.entityCars = entityCars;
            list = new ArrayList<>();
            for (EntityCar car : entityCars)
                list.add(new EntityArrayAdapter(car.carNumber, false));
            tvSpinnerBoardingVehicle.changListData(list);
            tvSpinnerBoardingVehicle.setOnSpinnerClickListener(positionSelect -> {
                if (isValidatePosition(positionSelect))
                    idCar = entityCars.get(positionSelect).id;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}