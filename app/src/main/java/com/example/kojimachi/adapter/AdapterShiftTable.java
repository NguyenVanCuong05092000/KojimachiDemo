package com.example.kojimachi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.kojimachi.R;
import com.example.kojimachi.constant.AppConstants;
import com.example.kojimachi.entity.EntityShiftTable;

public class AdapterShiftTable extends RecyclerView.Adapter<AdapterShiftTable.HolderShiftTableAdapter> {
    private ArrayList<EntityShiftTable> shiftTableArrayList;
    private Context mContext;

    public interface OnClickItemListener {
        void onClickItem(EntityShiftTable entityShiftTable);
    }

    private OnClickItemListener onClickItemListener;

    public AdapterShiftTable(ArrayList<EntityShiftTable> shiftTableArrayList, Context mContext, OnClickItemListener onClickItemListener) {
        this.shiftTableArrayList = shiftTableArrayList;
        this.mContext = mContext;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public HolderShiftTableAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_shift, parent, false);
        return new HolderShiftTableAdapter(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderShiftTableAdapter holder, int position) {
        EntityShiftTable entityShiftTable = shiftTableArrayList.get(position);
        if (shiftTableArrayList != null)
            holder.bindData(entityShiftTable);
    }

    @Override
    public int getItemCount() {
        return shiftTableArrayList == null ? 0 : shiftTableArrayList.size();
    }

    public class HolderShiftTableAdapter extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private ImageView imgAttendanceStatus;
        private TextView tvAttendanceStatus;
        private TextView tvAttendanceTime;
        private TextView tvLeaveTime;
        private TextView tvStandby;
        private View clItemTable;
        private View clImgCoin;

        public HolderShiftTableAdapter(@NonNull View itemView) {
            super(itemView);
            clImgCoin = itemView.findViewById(R.id.clImgCoin);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgAttendanceStatus = itemView.findViewById(R.id.imgAttendanceStatus);
            imgAttendanceStatus.getLayoutParams().width = getSizeWithScale(71);
            imgAttendanceStatus.getLayoutParams().height = getSizeWithScale(45);
            tvAttendanceStatus = itemView.findViewById(R.id.tvAttendanceStatus);
            tvAttendanceTime = itemView.findViewById(R.id.tvAttendanceTime);
            tvLeaveTime = itemView.findViewById(R.id.tvLeaveTime);
            tvStandby = itemView.findViewById(R.id.tvStandby);
            clItemTable = itemView.findViewById(R.id.clItemTable);
            clItemTable.getLayoutParams().height = getSizeWithScale(53);
        }

        @SuppressLint("SetTextI18n")
        public void bindData(EntityShiftTable entityShiftTable) {
            int date = entityShiftTable.date;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, date);
            calendar.set(Calendar.MONTH, entityShiftTable.month);
            calendar.set(Calendar.YEAR, entityShiftTable.year);
            String dayOfWeek = new SimpleDateFormat("EE", new Locale("ja")).format(calendar.getTime());
            tvDate.setText(entityShiftTable.date + "(" + dayOfWeek + ")");
            tvAttendanceStatus.setText(entityShiftTable.attendanceStatus);
            tvAttendanceTime.setText(entityShiftTable.attendanceTime);
            tvLeaveTime.setText(entityShiftTable.leaveTime);
            tvStandby.setText(entityShiftTable.standby);
            clItemTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null) {
                        onClickItemListener.onClickItem(entityShiftTable);
                    }
                }
            });
            if (entityShiftTable.paidHoliday == 1) {
                clImgCoin.setBackgroundResource(R.drawable.bg_item_table_yellow);
                imgAttendanceStatus.setImageResource(R.drawable.ic_coin);
                tvAttendanceTime.setBackgroundResource(R.drawable.bg_item_table_yellow);
                tvLeaveTime.setBackgroundResource(R.drawable.bg_item_table_yellow);
                tvStandby.setBackgroundResource(R.drawable.bg_item_table_yellow);
            } else if (dayOfWeek.equals("土")) {
                entityShiftTable.holiday = 1;
                clImgCoin.setBackgroundResource(R.drawable.bg_item_table_green);
                imgAttendanceStatus.setImageResource(0);
                tvAttendanceTime.setBackgroundResource(R.drawable.bg_item_table_green);
                tvLeaveTime.setBackgroundResource(R.drawable.bg_item_table_green);
                tvStandby.setBackgroundResource(R.drawable.bg_item_table_green);
            } else if (dayOfWeek.equals("日")) {
                entityShiftTable.holiday = 1;
                clImgCoin.setBackgroundResource(R.drawable.bg_item_table_pink);
                imgAttendanceStatus.setImageResource(0);
                tvAttendanceTime.setBackgroundResource(R.drawable.bg_item_table_pink);
                tvLeaveTime.setBackgroundResource(R.drawable.bg_item_table_pink);
                tvStandby.setBackgroundResource(R.drawable.bg_item_table_pink);
            } else {
                entityShiftTable.holiday = 0;
                clImgCoin.setBackgroundResource(R.drawable.bg_item_table);
                imgAttendanceStatus.setImageResource(0);
                tvAttendanceTime.setBackgroundResource(R.drawable.bg_item_table);
                tvLeaveTime.setBackgroundResource(R.drawable.bg_item_table);
                tvStandby.setBackgroundResource(R.drawable.bg_item_table);
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
