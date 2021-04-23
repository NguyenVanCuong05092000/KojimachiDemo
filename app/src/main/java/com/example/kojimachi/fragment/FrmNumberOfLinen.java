package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.kojimachi.R;
import com.example.kojimachi.entity.BackStackData;
import com.example.kojimachi.entity.EntityArrayAdapter;
import com.example.kojimachi.entity.EntityOptionOfLinen;
import com.example.kojimachi.entity.EntityUpdateOfLinen;
import com.example.kojimachi.listener.ListenerHandleResult;
import com.example.kojimachi.utils.AppUtils;
import com.example.kojimachi.view.CustomNextNumber;
import com.example.kojimachi.view.CustomSpinner;

import static com.example.kojimachi.constant.ApiConstants.PARAM_LIST_OPTION;
import static com.example.kojimachi.constant.FragmentConstants.FRM_NUMBER_OF_LINE;

public class FrmNumberOfLinen extends BaseFragment implements View.OnClickListener {


    public static FrmNumberOfLinen getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmNumberOfLinen fragment = new FrmNumberOfLinen();
        Bundle data = new Bundle();
        if (mapData.containsKey(EXTRA_ID)) {
            data.putInt(EXTRA_ID, (Integer) mapData.get(EXTRA_ID));
        }
        if (arrayList != null)
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_number_of_linen;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_NUMBER_OF_LINE;
    }

    @Override
    public void backToPrevious() {
        finish();
    }

    @Override
    public boolean isBackPreviousEnable() {
        return true;
    }

    @Override
    protected void finish() {
        activity.backToPreviousFromBackStack(getArguments());
    }

    private TextView btnRegistration;
    private TextView btnGoBack;
    private CustomSpinner tvSpinnerOption;
    private ArrayList<EntityArrayAdapter> arrayList;
    private CustomNextNumber customNextNumber;
    private CustomNextNumber customNextNumber2;
    private ArrayList<EntityOptionOfLinen> entityOptionOfLinenArrayList;
    private ArrayList<EntityUpdateOfLinen> updateOfLinenArrayList;
    private int scheduleId;
    private int idOption1;
    private int idOption2;

    @Override
    protected void loadControlsAndResize(View view) {

        btnRegistration = view.findViewById(R.id.btnRegistration);
        btnRegistration.getLayoutParams().width = activity.getSizeWithScale(146);
        btnRegistration.getLayoutParams().height = activity.getSizeWithScale(46);

        btnGoBack = view.findViewById(R.id.btnGoBack);
        btnGoBack.getLayoutParams().width = activity.getSizeWithScale(146);
        btnGoBack.getLayoutParams().height = activity.getSizeWithScale(46);

        tvSpinnerOption = view.findViewById(R.id.tvSpinnerOption);
        tvSpinnerOption.getLayoutParams().width = activity.getSizeWithScale(302);
        tvSpinnerOption.getLayoutParams().height = activity.getSizeWithScale(46);

        customNextNumber = view.findViewById(R.id.clNextNumber);
        customNextNumber2 = view.findViewById(R.id.clNextNumber2);

        btnRegistration.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().containsKey(EXTRA_ID)) {
            scheduleId = getArguments().getInt(EXTRA_ID);
        }
        if (entityOptionOfLinenArrayList == null) entityOptionOfLinenArrayList = new ArrayList<>();
        getDataListOption();
        if (arrayList == null) arrayList = new ArrayList<>();
        customNextNumber.setMinNumberPieces(0);
        customNextNumber2.setMinNumberPieces(0);
        customNextNumber.setMaxNumberPieces(20);
        customNextNumber2.setMaxNumberPieces(20);
    }

    private void getDataListOption() {
        try {
            activity.getListOptionLinen(new ListenerHandleResult() {
                @Override
                public void onSuccess(Object response) {
                    JSONObject jsonObject = (JSONObject) response;
                    JSONArray jsonArray = jsonObject.optJSONArray(PARAM_LIST_OPTION);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        EntityOptionOfLinen entityOptionOfLinen = new EntityOptionOfLinen(jsonArray.optJSONObject(i));
                        entityOptionOfLinenArrayList.add(entityOptionOfLinen);
                    }
                    if (arrayList != null) arrayList.clear();
                    for (int i = 0; i < entityOptionOfLinenArrayList.size(); i++) {
                        arrayList.add(new EntityArrayAdapter(entityOptionOfLinenArrayList.get(i).nameOption, false));
                    }
                    tvSpinnerOption.changListData(arrayList);
                    tvSpinnerOption.setOnSpinnerClickListener(positionSelect -> {
                        tvSpinnerOption.setText(entityOptionOfLinenArrayList.get(positionSelect).nameOption);
                        idOption1 = entityOptionOfLinenArrayList.get(positionSelect).id;
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNumberOfLinen() {
        if (updateOfLinenArrayList == null) updateOfLinenArrayList = new ArrayList<>();
        updateOfLinenArrayList.clear();
        updateOfLinenArrayList.add(new EntityUpdateOfLinen(customNextNumber.getNumberPieces(), idOption1));
        updateOfLinenArrayList.add(new EntityUpdateOfLinen(customNextNumber2.getNumberPieces(), idOption2));
        activity.updateNumberOfLinen(scheduleId, updateOfLinenArrayList, new ListenerHandleResult() {
            @Override
            public void onSuccess(Object response) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.btnRegistration:
                updateNumberOfLinen();
                break;
            case R.id.btnGoBack:
                finish();
                break;
        }
    }
}