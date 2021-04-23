package com.example.kojimachi.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.EntityViewSalesSub;
import jp.co.kojimachi.utils.AppUtils;

public class AdapterSalesHistorySub extends RecyclerView.Adapter<AdapterSalesHistorySub.SalesHistorySubViewHolder> {

    private final ArrayList<EntityViewSalesSub> viewSalesSubArrayList;
    private final int hImgDash;

    public interface OnClickItemListener {
        void onClickItem(int id);
    }

    private final OnClickItemListener onClickItemListener;

    public AdapterSalesHistorySub(ArrayList<EntityViewSalesSub> viewSalesSubArrayList, int hImgDash, OnClickItemListener onClickItemListener) {
        this.viewSalesSubArrayList = viewSalesSubArrayList;
        this.hImgDash = hImgDash;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public SalesHistorySubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_sales, parent, false);
        return new SalesHistorySubViewHolder(view, hImgDash);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesHistorySubViewHolder holder, int position) {
        try {
            EntityViewSalesSub entityViewSalesSub = viewSalesSubArrayList.get(position);
            holder.bind(entityViewSalesSub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return viewSalesSubArrayList == null ? 0 : viewSalesSubArrayList.size();
    }

    public class SalesHistorySubViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvCourse;
        private final TextView tvSalesAmount;
        private final TextView tvCommission;
        private final TextView tvTitleCourse;
        private final ImageView imgDashBelow;
        private final ImageView imgDashUpper;

        public SalesHistorySubViewHolder(@NonNull View itemView, int hImgDash) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitleCourse = itemView.findViewById(R.id.tvTitleCourse);
            tvCourse = itemView.findViewById(R.id.tvCourse);
            tvSalesAmount = itemView.findViewById(R.id.tvSalesAmount);
            tvCommission = itemView.findViewById(R.id.tvCommission);
            imgDashBelow = itemView.findViewById(R.id.imgDashBelow);
            imgDashBelow.setImageResource(R.drawable.img_dash_below);
            imgDashBelow.getLayoutParams().height = hImgDash;
            imgDashUpper = itemView.findViewById(R.id.imgDashUpper);
            imgDashUpper.setImageResource(R.drawable.img_dash_upper);
            imgDashUpper.getLayoutParams().height = hImgDash;

        }

        @SuppressLint("SetTextI18n")
        public void bind(EntityViewSalesSub entityViewSalesSub) {
            try {
                if (entityViewSalesSub != null) {
                    tvTime.setText(entityViewSalesSub.timeStart + " " + entityViewSalesSub.nameShop);
                    tvTitleCourse.setText(entityViewSalesSub.nameDataCharge);
                    tvCourse.setText(AppUtils.formatPrice(String.valueOf(entityViewSalesSub.treatmentTime)) + " 分");
                    tvSalesAmount.setText(AppUtils.formatPrice(String.valueOf(entityViewSalesSub.totalPrice)) + " 円");
                    tvCommission.setText(AppUtils.formatPrice(String.valueOf(entityViewSalesSub.priceCommission)) + " 円");
                    if (entityViewSalesSub.isEnd) {
                        imgDashUpper.setVisibility(View.GONE);
                    }
                    if (entityViewSalesSub.isStart) {
                        imgDashBelow.setVisibility(View.GONE);
                    }

                    itemView.setOnClickListener(v -> {
                        if (onClickItemListener != null) {
                            onClickItemListener.onClickItem(entityViewSalesSub.id);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
