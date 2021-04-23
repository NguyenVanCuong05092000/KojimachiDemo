package com.example.kojimachi.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.example.kojimachi.R;
import com.example.kojimachi.adapter.AdapterSalesHistory;
import com.example.kojimachi.constant.AppConstants;
import com.example.kojimachi.constant.ExtraConstants;
import com.example.kojimachi.entity.BackStackData;
import com.example.kojimachi.entity.EntityViewSales;
import com.example.kojimachi.listener.ListenerHandleResult;
import com.example.kojimachi.utils.AppUtils;
import com.example.kojimachi.view.CustomMonthLayout;

import static com.example.kojimachi.constant.FragmentConstants.FRM_VIEW_SALES;

public class FrmSalesHistory extends BaseFragment implements View.OnClickListener {
    private RecyclerView rcViewSales;
    private AdapterSalesHistory adapterSalesHistory;
    private ArrayList<EntityViewSales> viewSalesArrayList;
    private CustomMonthLayout clMonthLayout;
    private int month;
    private int year;
    private TextView tvMessage;

    public static FrmSalesHistory getInstance() {
        return new FrmSalesHistory();
    }

    public static FrmSalesHistory getInstance(ArrayList<BackStackData> arrayList, HashMap<String, Object> mapData) {
        FrmSalesHistory fragment = new FrmSalesHistory();
        Bundle data = new Bundle();
        if (mapData != null && mapData.containsKey(ExtraConstants.EXTRA_POSITION)) {
            data.putInt(ExtraConstants.EXTRA_POSITION, (Integer) mapData.get(ExtraConstants.EXTRA_POSITION));
            data.putInt(ExtraConstants.EXTRA_OFFSET, (Integer) mapData.get(ExtraConstants.EXTRA_OFFSET));
            fragment.setArguments(data);
        }
        if (arrayList != null)
            data.putSerializable(EXTRA_BACK_STACK, arrayList);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_sales_history;
    }

    @Override
    protected int getCurrentFragment() {
        return FRM_VIEW_SALES;
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
        ImageView imgArrowLeft = view.findViewById(R.id.imgArrowLeft);
        imgArrowLeft.getLayoutParams().height = activity.getSizeWithScale(25);
        imgArrowLeft.getLayoutParams().width = activity.getSizeWithScale(25);

        ImageView imgArrowRight = view.findViewById(R.id.imgArrowRight);
        imgArrowRight.getLayoutParams().height = activity.getSizeWithScale(25);
        imgArrowRight.getLayoutParams().width = activity.getSizeWithScale(25);

        rcViewSales = view.findViewById(R.id.rcViewSales);
        rcViewSales.getLayoutParams().width = activity.getSizeWithScale(338);
        clMonthLayout = view.findViewById(R.id.clMonthAndYear);
        tvMessage = view.findViewById(R.id.tvMessage);

        View clViewSales = view.findViewById(R.id.clViewSales);
        clViewSales.getLayoutParams().width = activity.getSizeWithScale(338);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.getLayoutParams().width = activity.getSizeWithScale(302);
        btnBack.getLayoutParams().height = activity.getSizeWithScale(46);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restoreDataState();
        prepareListHistory();
        setupDateController();
    }

    int pos = 0;
    int offset = 0;
    private void restoreDataState() {
        try {
            if (getArguments() != null && getArguments().containsKey(EXTRA_POSITION)) {
                pos = getArguments().getInt(EXTRA_POSITION);
                offset = getArguments().getInt(EXTRA_OFFSET);
                getArguments().remove(EXTRA_POSITION);
                getArguments().remove(EXTRA_OFFSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareListHistory() {
        viewSalesArrayList = new ArrayList<>();
        adapterSalesHistory = new AdapterSalesHistory(getContext(), viewSalesArrayList, activity.getSizeWithScale(11.5), new AdapterSalesHistory.OnClickItemListener() {
            @Override
            public void onClickItem(int position, int id) {
                showFrmDailyNewspaper(position, id);
            }
        });
        rcViewSales.setLayoutManager(new LinearLayoutManager(getContext()));
        rcViewSales.setAdapter(adapterSalesHistory);
    }

    private void setupDateController() {
        try {
            //Setup Max Date
            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.set(Calendar.MONTH, maxCalendar.get(Calendar.MONTH) - 1);
            clMonthLayout.setMaxCalendar(maxCalendar);

            //Setup Min Date If Have CreateAt
            if (!activity.getCurrentCreateAt().equals("null")) {
                Calendar minCalendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_FORMAT);
                minCalendar.setTime(formatter.parse(activity.getCurrentCreateAt()));
                minCalendar.set(Calendar.MONTH, minCalendar.get(Calendar.MONTH) + 1);
                clMonthLayout.setMinCalendar(minCalendar);
            }
            //
            month = clMonthLayout.getTmpCalendar().get(Calendar.MONTH) + 1;
            year = clMonthLayout.getTmpCalendar().get(Calendar.YEAR);
            getSalesFromServer(String.valueOf(month), String.valueOf(year));
            clMonthLayout.setOnArrowClickListener(new CustomMonthLayout.OnArrowClickListener() {
                @Override
                public void onClickPreMonth(Calendar calendar) {
                    refreshSalesFromSerVer(calendar);
                }

                @Override
                public void onClickNextMonth(Calendar calendar) {
                    refreshSalesFromSerVer(calendar);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSalesFromSerVer(Calendar calendar) {
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        getSalesFromServer(String.valueOf(month), String.valueOf(year));
    }

    private void getSalesFromServer(String month, String year) {
        activity.getDataViewSales(month, year, new ListenerHandleResult() {
            @Override
            public void onSuccess(Object response) {
                JSONArray jsonArray = (JSONArray) response;
                if (viewSalesArrayList != null) {
                    viewSalesArrayList.clear();
                    if (jsonArray.length() > 0){
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EntityViewSales entityViewSales = new EntityViewSales(jsonArray.optJSONObject(i), clMonthLayout.getTmpCalendar());
                            viewSalesArrayList.add(entityViewSales);
                        }
                        tvMessage.setVisibility(View.GONE);
                    } else tvMessage.setVisibility(View.VISIBLE);
                    if (adapterSalesHistory != null) adapterSalesHistory.notifyDataSetChanged();
                    if (pos > 0 || offset != 0)
                        ((LinearLayoutManager) rcViewSales.getLayoutManager())
                                .scrollToPositionWithOffset(pos, offset);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        if (v.getId() == R.id.btnBack) {
            finish();
        }
    }

    private HashMap<String, Object> getDataSaveState() {
        int itemPosition = 0;
        int offset = 0;
        try {
            View view = rcViewSales.getChildAt(0);
            offset = view == null ? 0 : view.getTop() - rcViewSales.getPaddingTop();
            itemPosition = ((LinearLayoutManager) rcViewSales.getLayoutManager()).findFirstVisibleItemPosition();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        HashMap<String, Object> mapIndex = new HashMap<>();
        mapIndex.put(EXTRA_OFFSET, offset);
        mapIndex.put(EXTRA_POSITION, itemPosition);
        return mapIndex;
    }

    private void showFrmDailyNewspaper(int position, int scheduleId) {
        try {
            HashMap<String, Object> data = new HashMap<>();
            data.put(EXTRA_POSITION, position);
            data.put(EXTRA_ID, scheduleId);
            ArrayList<BackStackData> arrayList = new ArrayList<>();
            arrayList.add(new BackStackData(getCurrentFragment(), getDataSaveState()));
            activity.showDailyNewspaper(arrayList, data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}