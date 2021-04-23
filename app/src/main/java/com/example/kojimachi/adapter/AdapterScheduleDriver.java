package com.example.kojimachi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.co.kojimachi.R;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.entity.EntitySchedule;
import jp.co.kojimachi.listener.ListenerOptionHomeDriverClick;

public class AdapterScheduleDriver extends RecyclerView.Adapter<AdapterScheduleDriver.HolderScheduleAdapter> {
    private static final String TAG = "AdapterScheduleDriver";
    private ArrayList<EntitySchedule> entityScheduleDrivers;
    private Context mContext;
    private ListenerOptionHomeDriverClick listenerOptionHomeDriverClick;
    private boolean isWorking;

    public AdapterScheduleDriver(ArrayList<EntitySchedule> entityScheduleDrivers, Context mContext, ListenerOptionHomeDriverClick listenerOptionHomeDriverClick, boolean isWorking) {
        this.entityScheduleDrivers = entityScheduleDrivers;
        this.mContext = mContext;
        this.listenerOptionHomeDriverClick = listenerOptionHomeDriverClick;
        this.isWorking = isWorking;
    }

    @NonNull
    @Override
    public HolderScheduleAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_home_driver, parent, false);
        return new HolderScheduleAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderScheduleAdapter holder, int position) {
        EntitySchedule entity = getItemAt(position);
        if (entityScheduleDrivers != null) {
            holder.bindData(entity);
        }
    }

    @Override
    public int getItemCount() {
        return entityScheduleDrivers == null ? 0 : entityScheduleDrivers.size();
    }

    private EntitySchedule getItemAt(int position) {
        return entityScheduleDrivers != null && entityScheduleDrivers.size() > position ? entityScheduleDrivers.get(position) : null;
    }

    public void changeStatusWorking(boolean isWorkingDay) {
        isWorking = isWorkingDay;
        notifyDataSetChanged();
    }

    private void enableButton(TextView button) {
        button.setTextColor(Color.parseColor("#FFFFFF"));
        button.setBackgroundResource(R.drawable.bg_button_normal);
    }

    private void disableButton(TextView button) {
        button.setTextColor(Color.parseColor("#848484"));
        button.setBackgroundResource(R.drawable.bg_button_selected);
        button.setEnabled(false);
    }

    class HolderScheduleAdapter extends RecyclerView.ViewHolder {

        private TextView tvArrivalTime;
        private TextView tvFacility;
        private TextView tvGettingOn;
        private TextView btnConfirmation;
        private TextView btnDone;

        public HolderScheduleAdapter(@NonNull View itemView) {
            super(itemView);

            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvFacility = itemView.findViewById(R.id.tvFacility);
            tvGettingOn = itemView.findViewById(R.id.tvGettingOn);

            btnConfirmation = itemView.findViewById(R.id.btnConfirmation);
            btnConfirmation.getLayoutParams().width = getSizeWithScale(146);
            btnConfirmation.getLayoutParams().height = getSizeWithScale(50);
            btnDone = itemView.findViewById(R.id.btnDone);
            btnDone.getLayoutParams().width = getSizeWithScale(146);
            btnDone.getLayoutParams().height = getSizeWithScale(50);

        }

        void bindData(EntitySchedule entityScheduleDrivers) {
            try {
                tvArrivalTime.setText(entityScheduleDrivers.timeStart);
                tvFacility.setText(entityScheduleDrivers.shop);
                tvGettingOn.setText(entityScheduleDrivers.therapist);
                if (entityScheduleDrivers.status == 1) {
                    disableButton(btnConfirmation);
                    enableButton(btnDone);
                    btnDone.setEnabled(true);
                } else if (entityScheduleDrivers.status == 2) {
                    disableButton(btnConfirmation);
                    disableButton(btnDone);
                } else {
                    enableButton(btnConfirmation);
                    btnConfirmation.setEnabled(true);
                    enableButton(btnDone);
                    btnDone.setEnabled(false);
                }

                btnConfirmation.setOnClickListener(v -> {
                    try {
                        if (isWorking) {
                            disableButton(btnConfirmation);
                            btnDone.setEnabled(true);
                            if (listenerOptionHomeDriverClick != null)
                                listenerOptionHomeDriverClick.onConfirmationClick(entityScheduleDrivers.idTranfers);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });

                btnDone.setOnClickListener(v -> {
                    try {
                        if (isWorking) {
                            disableButton(btnDone);
                            if (listenerOptionHomeDriverClick != null)
                                listenerOptionHomeDriverClick.onDoneClick(entityScheduleDrivers.idTranfers);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //RESIZE
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private int screenWidth = 0;

    protected int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / AppConstants.SCREEN_WIDTH_DESIGN;
        return scaleValue;
    }

    protected int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

}