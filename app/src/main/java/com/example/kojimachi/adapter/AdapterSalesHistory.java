package com.example.kojimachi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.kojimachi.R;
import com.example.kojimachi.entity.EntityViewSales;
import com.example.kojimachi.utils.AppUtils;

public class AdapterSalesHistory extends RecyclerView.Adapter<AdapterSalesHistory.SalesHistoryViewHolder> {
    private final Context mContext;
    private final ArrayList<EntityViewSales> viewSalesArrayList;
    private final int hImgDash;

    public interface OnClickItemListener {
        void onClickItem(int position, int id);
    }

    private final OnClickItemListener onClickItemListener;

    public AdapterSalesHistory(Context mContext, ArrayList<EntityViewSales> viewSalesArrayList, int hImgDash, OnClickItemListener onClickItemListener) {
        this.mContext = mContext;
        this.viewSalesArrayList = viewSalesArrayList;
        this.hImgDash = hImgDash;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public SalesHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view_sales, parent, false);
        return new SalesHistoryViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SalesHistoryViewHolder holder, int position) {
        try {
            EntityViewSales entityViewSales = viewSalesArrayList.get(position);
            holder.bind(entityViewSales, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return viewSalesArrayList == null ? 0 : viewSalesArrayList.size();
    }

    public class SalesHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvTotalSales;
        private final RecyclerView rcItemViewSales;

        public SalesHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalSales = itemView.findViewById(R.id.tvTotalSales);
            rcItemViewSales = itemView.findViewById(R.id.rcItemViewSales);
        }

        @SuppressLint("SetTextI18n")
        public void bind(EntityViewSales entityViewSales, int position) {
            try {
                if (entityViewSales != null) {
                    tvDate.setText(entityViewSales.displayDateTime);
                    tvTotalSales.setText(AppUtils.formatPrice(entityViewSales.totalCommission) + " 円");
                    if (entityViewSales.viewSalesSubArrayList != null) {
                        AdapterSalesHistorySub adapterViewSalesSub = new AdapterSalesHistorySub(entityViewSales.viewSalesSubArrayList, hImgDash, id -> {
                            if (onClickItemListener != null)
                                onClickItemListener.onClickItem(position, id);
                        });
                        rcItemViewSales.setLayoutManager(new LinearLayoutManager(mContext));
                        rcItemViewSales.setAdapter(adapterViewSalesSub);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
