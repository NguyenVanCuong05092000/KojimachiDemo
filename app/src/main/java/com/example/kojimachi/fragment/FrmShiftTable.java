package com.example.kojimachi.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.example.kojimachi.R;
import com.example.kojimachi.adapter.AdapterShiftTable;
import com.example.kojimachi.entity.ApiResult;
import com.example.kojimachi.entity.BackStackData;
import com.example.kojimachi.entity.EntityArea;
import com.example.kojimachi.entity.EntityArrayAdapter;
import com.example.kojimachi.entity.EntityShiftTable;
import com.example.kojimachi.entity.EntityTherapistSchedule;
import com.example.kojimachi.listener.CallbackApi;
import com.example.kojimachi.listener.CallbackGetTherapistSchedule;
import com.example.kojimachi.utils.AppUtils;
import com.example.kojimachi.view.CustomMonthLayout;
import com.example.kojimachi.view.CustomSpinner;

import static com.example.kojimachi.constant.FragmentConstants.FRM_SHIFT_TABLE;

public class FrmShiftTable extends BaseFragment implements View.OnClickListener, CallbackGetTherapistSchedule, AdapterShiftTable.OnClickItemListener, CallbackApi {
    private TextView tvStartAttendanceTime;
    private TextView tvEndAttendanceTime;
    private TextView tvScheduledTime;
    private CustomSpinner tvWaitingPlace;
    private CustomMonthLayout clMonthLayout;
    private RecyclerView rcShiftTable;
    private AdapterShiftTable adapterShiftTable;
    private ArrayList<EntityShiftTable> shiftTableArrayList;
    private int hour;
    private int mMinute;
    private String timePicker;
    private int year;
    private int month;
    private String startTime;
    private String endTime;
    private ArrayList<EntityArrayAdapter> list = new ArrayList<>();
    private String waitingPlace;
    private int positionArea;
    private ArrayList<EntityArea> entityAreas;

    public static FrmShiftTable getInstance() {
        return new FrmShiftTable();
    }

    public static FrmShiftTable getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmShiftTable fragment = new FrmShiftTable();
        Bundle data = new Bundle();
        if (arrayList != null)
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_shift_table;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_SHIFT_TABLE;
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

        tvStartAttendanceTime = view.findViewById(R.id.tvStartAttendanceTime);
        tvStartAttendanceTime.getLayoutParams().width = activity.getSizeWithScale(97);
        tvStartAttendanceTime.getLayoutParams().height = activity.getSizeWithScale(34);

        tvEndAttendanceTime = view.findViewById(R.id.tvEndAttendanceTime);
        tvEndAttendanceTime.getLayoutParams().width = activity.getSizeWithScale(97);
        tvEndAttendanceTime.getLayoutParams().height = activity.getSizeWithScale(34);

        clMonthLayout = view.findViewById(R.id.clMonthAndYear);

        View clTable = view.findViewById(R.id.clTable);
        clTable.getLayoutParams().width = activity.getSizeWithScale(338);

        View clTop = view.findViewById(R.id.clTop);
        clTop.getLayoutParams().width = activity.getSizeWithScale(338);

        TextView btnPutForward = view.findViewById(R.id.btnPutForward);
        btnPutForward.getLayoutParams().width = activity.getSizeWithScale(146);
        btnPutForward.getLayoutParams().height = activity.getSizeWithScale(46);

        TextView btnBack = view.findViewById(R.id.btnBack);
        btnBack.getLayoutParams().width = activity.getSizeWithScale(146);
        btnBack.getLayoutParams().height = activity.getSizeWithScale(46);

        rcShiftTable = view.findViewById(R.id.rcShiftTable);

        View clHeaderTable = view.findViewById(R.id.clHeaderTable);
        clHeaderTable.getLayoutParams().height = activity.getSizeWithScale(33);
        tvScheduledTime = view.findViewById(R.id.tvScheduledTime);

        tvWaitingPlace = view.findViewById(R.id.tvWaitingPlace);
        tvWaitingPlace.getLayoutParams().height = activity.getSizeWithScale(34);

        tvEndAttendanceTime.setOnClickListener(this);
        tvStartAttendanceTime.setOnClickListener(this);
        btnPutForward.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startTime = tvStartAttendanceTime.getText().toString();
        endTime = tvEndAttendanceTime.getText().toString();
        clickNextAndPreMonth();
        activity.getListArea(this);
        callApi();
    }

    private void callApi() {
        activity.getTherapistSchedule(clMonthLayout.getTmpCalendar().get(Calendar.YEAR), clMonthLayout.getTmpCalendar().get(Calendar.MONTH) + 1, true, this);
        setDataTable();
    }

    private void setDataTable() {
        try {
            month = clMonthLayout.getTmpCalendar().get(Calendar.MONTH);
            year = clMonthLayout.getTmpCalendar().get(Calendar.YEAR);
            setDataList();
            rcShiftTable.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterShiftTable = new AdapterShiftTable(shiftTableArrayList, getContext(), this);
            rcShiftTable.setAdapter(adapterShiftTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickNextAndPreMonth() {
        try {
            Calendar minCalendar = Calendar.getInstance();
            clMonthLayout.setMinCalendar(minCalendar);

            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.set(Calendar.MONTH, maxCalendar.get(Calendar.MONTH) + 15);
            clMonthLayout.setMaxCalendar(maxCalendar);

            clMonthLayout.setOnArrowClickListener(new CustomMonthLayout.OnArrowClickListener() {
                @Override
                public void onClickPreMonth(Calendar calendar) {
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);
                    callApi();
                    adapterShiftTable.notifyDataSetChanged();
                }

                @Override
                public void onClickNextMonth(Calendar calendar) {
                    month = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);
                    callApi();
                    adapterShiftTable.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataList() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, 1);
            int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (shiftTableArrayList != null) shiftTableArrayList.clear();
            if (shiftTableArrayList == null) shiftTableArrayList = new ArrayList<>();
            for (int i = 1; i <= maxDay; i++) {
                shiftTableArrayList.add(new EntityShiftTable(i, "", "", "", "", 0, 0, "", month, year));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeDataList(ArrayList<EntityTherapistSchedule> entityTherapistSchedules) {
        try {
            if (entityTherapistSchedules != null) {
                for (int i = 0; i < shiftTableArrayList.size(); i++) {
                    for (EntityTherapistSchedule entityTherapistSchedule : entityTherapistSchedules) {
                        if (AppUtils.parseInt(entityTherapistSchedule.day) == shiftTableArrayList.get(i).date) {
                            if (!(TextUtils.isEmpty(entityTherapistSchedule.workStart) || entityTherapistSchedule.workStart.equals("null"))
                                    && !(TextUtils.isEmpty(entityTherapistSchedule.workEnd) || entityTherapistSchedule.workEnd.equals("null"))
                                    && !(TextUtils.isEmpty(entityTherapistSchedule.areaName) || entityTherapistSchedule.areaName.equals("null"))) {
                                shiftTableArrayList.remove(i);
                                shiftTableArrayList.add(i, new EntityShiftTable(i + 1, "出勤",
                                        entityTherapistSchedule.workStart, entityTherapistSchedule.workEnd,
                                        entityTherapistSchedule.areaName, entityTherapistSchedule.paidHoliday,
                                        entityTherapistSchedule.paidHoliday, entityTherapistSchedule.idArea, month, year));
                            } else if (entityTherapistSchedule.paidHoliday == 1) {
                                shiftTableArrayList.remove(i);
                                shiftTableArrayList.add(i, new EntityShiftTable(i + 1, "",
                                        entityTherapistSchedule.workStart, entityTherapistSchedule.workEnd,
                                        entityTherapistSchedule.areaName, entityTherapistSchedule.paidHoliday,
                                        entityTherapistSchedule.paidHoliday, entityTherapistSchedule.idArea, month, year));
                            }
                        }
                    }
                }
                adapterShiftTable.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTimePicker(TextView tvTime) {
        try {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            timePicker = hour + ":" + mMinute;
            new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    try {
                        if (hourOfDay != hour
                                || minute != mMinute) {
                            hour = hourOfDay;
                            mMinute = minute;
                            if (hour < 10) {
                                if (mMinute < 10) {
                                    timePicker = "0" + hour + ":" + "0" + mMinute;
                                } else {
                                    timePicker = "0" + hour + ":" + mMinute;
                                }
                            } else {
                                if (mMinute < 10) {
                                    timePicker = hour + ":" + "0" + mMinute;
                                } else {
                                    timePicker = hour + ":" + mMinute;
                                }
                            }
                            tvTime.setText(timePicker);
                            startTime = tvStartAttendanceTime.getText().toString();
                            endTime = tvEndAttendanceTime.getText().toString();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }, hour, mMinute, true).show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void registeredToWork() {
        try {
            activity.registeredToWork(shiftTableArrayList, true, new CallbackApi() {
                @Override
                public void onFinished(ApiResult result) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.tvStartAttendanceTime:
                showTimePicker(tvStartAttendanceTime);
                break;
            case R.id.tvEndAttendanceTime:
                showTimePicker(tvEndAttendanceTime);
                break;
            case R.id.btnPutForward:
                registeredToWork();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private boolean isHaveWork;

    @Override
    public void onSuccessGetTherapistSchedule(ArrayList<EntityTherapistSchedule> entityTherapistSchedules) {
        isHaveWork = false;
        changeDataList(entityTherapistSchedules);
        if (entityTherapistSchedules.size() > 0) {
            for (EntityTherapistSchedule entityTherapistSchedule : entityTherapistSchedules) {
                if (!TextUtils.isEmpty(entityTherapistSchedule.workStart) && !TextUtils.isEmpty(entityTherapistSchedule.workEnd) && !TextUtils.isEmpty(entityTherapistSchedule.areaName)) {
                    isHaveWork = true;
                }
            }
        } else {
            isHaveWork = false;
        }
    }

    @Override
    public void onFailedGetTherapistSchedule() {
        finish();
    }

    @Override
    public void onClickItem(EntityShiftTable entityShiftTable) {
        Calendar calendar = Calendar.getInstance();
        waitingPlace = tvWaitingPlace.getText().toString();
        if (calendar.get(Calendar.DAY_OF_MONTH) > 15) {
            if (clMonthLayout.getTmpCalendar().get(Calendar.MONTH) + 1 <= calendar.get(Calendar.MONTH) + 2) {
                activity.showNotice(R.string.msg_unedit_shift_table, false, null);
            } else {
                if (shiftTableArrayList != null) {
                    if (isHaveWork) {
                        activity.showNotice(R.string.msg_unedit_shift_table, false, null);
                    } else {
                        if (entityShiftTable.getAttendanceStatus().isEmpty()) {
                            if (TextUtils.isEmpty(tvWaitingPlace.getText())) {
                                activity.showNotice(R.string.lblPleaseSelectTheWaitingAreaFirst, false, null);
                            } else {
                                entityShiftTable.setAttendanceStatus("出勤");
                                entityShiftTable.setAttendanceTime(startTime);
                                entityShiftTable.setLeaveTime(endTime);
                                entityShiftTable.setStandby(waitingPlace);
                                for (int i = 0; i < entityAreas.size(); i++) {
                                    if (i == positionArea)
                                        entityShiftTable.idArea = String.valueOf(entityAreas.get(i).id);
                                }
                            }
                        } else {
                            entityShiftTable.setAttendanceStatus("");
                            entityShiftTable.setAttendanceTime("");
                            entityShiftTable.setLeaveTime("");
                            entityShiftTable.setStandby("");
                            entityShiftTable.idArea = "";
                        }
                        adapterShiftTable.notifyDataSetChanged();
                    }
                }
            }
        } else {
            if (clMonthLayout.getTmpCalendar().get(Calendar.MONTH) + 1 <= calendar.get(Calendar.MONTH) + 1) {
                activity.showNotice(R.string.msg_unedit_shift_table, false, null);
            } else {
                if (shiftTableArrayList != null) {
                    if (isHaveWork) {
                        activity.showNotice(R.string.msg_unedit_shift_table, false, null);
                    } else {
                        if (entityShiftTable.getAttendanceStatus().isEmpty()) {
                            if (TextUtils.isEmpty(tvWaitingPlace.getText())) {
                                activity.showNotice(R.string.lblPleaseSelectTheWaitingAreaFirst, false, null);
                            } else {
                                entityShiftTable.setAttendanceStatus("出勤");
                                entityShiftTable.setAttendanceTime(startTime);
                                entityShiftTable.setLeaveTime(endTime);
                                entityShiftTable.setStandby(waitingPlace);
                                for (int i = 0; i < entityAreas.size(); i++) {
                                    if (i == positionArea)
                                        entityShiftTable.idArea = String.valueOf(entityAreas.get(i).id);
                                }
                            }
                        } else {
                            entityShiftTable.setAttendanceStatus("");
                            entityShiftTable.setAttendanceTime("");
                            entityShiftTable.setLeaveTime("");
                            entityShiftTable.setStandby("");
                            entityShiftTable.idArea = "";
                        }
                        adapterShiftTable.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void onFinished(ApiResult result) {
        if (result.response instanceof JSONArray) {
            try {
                JSONArray jsonArray = (JSONArray) result.response;
                entityAreas = new ArrayList<>();
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        EntityArea entityArea = new EntityArea(jsonArray.getJSONObject(i));
                        entityAreas.add(entityArea);
                    }
                    list.clear();

                    for (EntityArea area : entityAreas)
                        list.add(new EntityArrayAdapter(area.name, false));

                    tvWaitingPlace.changListData(list);
                    tvWaitingPlace.setOnSpinnerClickListener(positionSelect -> {
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}