package com.example.kojimachi.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.EntityArrayAdapter;
import jp.co.kojimachi.entity.EntityVehicleSettings;
import jp.co.kojimachi.listener.CallbackActionDriver;
import jp.co.kojimachi.view.CustomSpinner;

public class DialogHomeDriver extends BaseDialog implements View.OnClickListener {
    private CustomSpinner tvSpinnerBoardingVehicle;
    private TextView tvNameDriver;
    private TextView tvPhoneNumber;
    private CallbackActionDriver callback;
    private ArrayList<EntityArrayAdapter> list = new ArrayList<>();
    private Context mContext;
    private int positionCar = -1;

    public DialogHomeDriver(Context context) {
        super(context, R.style.dialogWithoutBox);
        mContext = context;
        setContentView(R.layout.dialog_information_driver);
        tvSpinnerBoardingVehicle = findViewById(R.id.tvSpinnerBoardingVehicle);
        tvSpinnerBoardingVehicle.getLayoutParams().width = getSizeWithScale(200);
        tvSpinnerBoardingVehicle.getLayoutParams().height = getSizeWithScale(46);

        tvNameDriver = findViewById(R.id.tvNameDriver);
        tvNameDriver.getLayoutParams().width = getSizeWithScale(200);
        tvNameDriver.getLayoutParams().height = getSizeWithScale(46);

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvPhoneNumber.getLayoutParams().width = getSizeWithScale(200);
        tvPhoneNumber.getLayoutParams().height = getSizeWithScale(46);

        TextView btnRegistration = findViewById(R.id.btnRegistration);
        btnRegistration.getLayoutParams().width = getSizeWithScale(146);
        btnRegistration.getLayoutParams().height = getSizeWithScale(46);

        btnRegistration.setOnClickListener(this);
    }

    public void changListData(ArrayList<String> arrayList) {
        list.clear();
        for (String s : arrayList)
            list.add(new EntityArrayAdapter(s, false));
        tvSpinnerBoardingVehicle.changListData(list);
        tvSpinnerBoardingVehicle.setOnSpinnerClickListener(positionSelect -> positionCar = positionSelect);
    }

    public void show(String boardingVehicle, String nameDriver, String phoneNumber, CallbackActionDriver callback) {
        try {
            this.callback = callback;
            tvSpinnerBoardingVehicle.setText(boardingVehicle);
            tvNameDriver.setText(nameDriver);
            tvPhoneNumber.setText(phoneNumber);
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegistration) {
            try {
                EntityVehicleSettings entityVehicleSettings =
                        new EntityVehicleSettings(tvSpinnerBoardingVehicle.getText(),
                                tvNameDriver.getText().toString(),
                                tvPhoneNumber.getText().toString(),
                                positionCar);
                if (callback != null)
                    callback.onActionFinished(entityVehicleSettings);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
