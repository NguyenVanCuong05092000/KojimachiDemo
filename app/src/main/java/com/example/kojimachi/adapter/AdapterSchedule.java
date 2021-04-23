package com.example.kojimachi.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import jp.co.kojimachi.listener.ListenerClickSchedule;

public class AdapterSchedule extends RecyclerView.Adapter<AdapterSchedule.HolderScheduleAdapter> {
    private ArrayList<EntitySchedule> scheduleArrayList;
    private Context mContext;
    private ListenerClickSchedule listener;

    public AdapterSchedule(ArrayList<EntitySchedule> scheduleArrayList, Context mContext, ListenerClickSchedule listener) {
        this.scheduleArrayList = scheduleArrayList;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderScheduleAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_home, parent, false);
        return new HolderScheduleAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderScheduleAdapter holder, int position) {
        EntitySchedule entitySchedule = getItemAt(position);
        if (scheduleArrayList != null) {
            if (entitySchedule.status != 5)
                holder.bindData(entitySchedule);
        }
    }

    private EntitySchedule getItemAt(int position) {
        return scheduleArrayList != null && scheduleArrayList.size() > position ? scheduleArrayList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return scheduleArrayList == null ? 0 : scheduleArrayList.size();
    }

    public class HolderScheduleAdapter extends RecyclerView.ViewHolder {
        private TextView tvReservationTime;
        private TextView tvFacility;
        private TextView tvCourse;
        private View imgBtnPlay;
        private View clItemSchedule;

        public HolderScheduleAdapter(@NonNull View itemView) {
            super(itemView);
            tvReservationTime = itemView.findViewById(R.id.tvReservationTime);
            tvFacility = itemView.findViewById(R.id.tvFacility);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            imgBtnPlay = itemView.findViewById(R.id.imgBtnPlay);
            imgBtnPlay.getLayoutParams().height = getSizeWithScale(59);
            imgBtnPlay.getLayoutParams().width = getSizeWithScale(59);
            clItemSchedule = itemView.findViewById(R.id.clItemSchedule);
        }

        void bindData(EntitySchedule entitySchedule) {
            try {
                tvReservationTime.setText(entitySchedule.displayTimeStart);
                SpannableString content = new SpannableString(entitySchedule.facility);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tvFacility.setText(content);
                tvCourse.setText(entitySchedule.course);
                tvFacility.setOnClickListener(v -> {
                    try {
                        if (listener != null)
                            listener.onClickFacility(entitySchedule.locationShop);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
                clItemSchedule.setOnClickListener(v -> {
                    try {
                        if (listener != null)
                            listener.onClickStartBooking(entitySchedule.scheduleId);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                });
            } catch (Throwable e) {
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
