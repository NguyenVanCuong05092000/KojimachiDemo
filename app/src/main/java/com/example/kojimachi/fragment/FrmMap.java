package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.kojimachi.R;
import com.example.kojimachi.entity.BackStackData;

import static com.example.kojimachi.constant.FragmentConstants.FRM_MAP;

public class FrmMap extends BaseFragment implements View.OnClickListener {
    private double latitude;
    private double longitude;

    public static FrmMap getInstance() {
        return new FrmMap();
    }

    public static FrmMap getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmMap fragment = new FrmMap();
        Bundle data = new Bundle();
        if (mapData != null) {
            data.putDouble(EXTRA_LATITUDE, (double) mapData.get(EXTRA_LATITUDE));
            data.putDouble(EXTRA_LONGITUDE, (double) mapData.get(EXTRA_LONGITUDE));
        }
        if (arrayList != null) {
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        }
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_map;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_MAP;
    }

    @Override
    protected void finish() {
        activity.backToPreviousFromBackStack(getArguments());
    }

    @Override
    public boolean isBackPreviousEnable() {
        return true;
    }

    @Override
    public void backToPrevious() {
        finish();
    }

    @Override
    protected void loadControlsAndResize(View view) {
        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.getLayoutParams().width = activity.getSizeWithScale(302);
        btnBack.getLayoutParams().height = activity.getSizeWithScale(46);
        View clBtnBack = view.findViewById(R.id.clBtnBack);
        clBtnBack.getLayoutParams().height = activity.getSizeWithScale(150);

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(EXTRA_LATITUDE);
            longitude = getArguments().getDouble(EXTRA_LONGITUDE);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng location = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}