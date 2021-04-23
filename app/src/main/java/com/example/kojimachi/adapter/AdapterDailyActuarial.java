package com.example.kojimachi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.EntityFeeDailyActuarial;
import jp.co.kojimachi.utils.AppUtils;

public class AdapterDailyActuarial extends RecyclerView.Adapter<AdapterDailyActuarial.HolderDailyActuarial> {
    private ArrayList<EntityFeeDailyActuarial> entityFeeDailyActuarials;

    public AdapterDailyActuarial(ArrayList<EntityFeeDailyActuarial> entityFeeDailyActuarials) {
        this.entityFeeDailyActuarials = entityFeeDailyActuarials;
    }

    @NonNull
    @Override
    public HolderDailyActuarial onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_actuarial, parent, false);
        return new HolderDailyActuarial(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDailyActuarial holder, int position) {
        EntityFeeDailyActuarial entityFeeDailyActuarial = getItemAt(position);
        if (entityFeeDailyActuarials != null) {
            holder.bindData(entityFeeDailyActuarial);
        }
    }

    private EntityFeeDailyActuarial getItemAt(int position) {
        return entityFeeDailyActuarials != null && entityFeeDailyActuarials.size() > position ? entityFeeDailyActuarials.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return entityFeeDailyActuarials == null ? 0 : entityFeeDailyActuarials.size();
    }

    public class HolderDailyActuarial extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvFee;

        public HolderDailyActuarial(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFee = itemView.findViewById(R.id.tvFee);
        }

        void bindData(EntityFeeDailyActuarial entityFeeDailyActuarial) {
            try {
                tvTitle.setText(entityFeeDailyActuarial.title);
                tvFee.setText(formatPrice(AppUtils.parseInt(entityFeeDailyActuarial.fee)));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private String formatPrice(int price) {
        try {
            return String.format(Locale.JAPAN, "%,d", price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
}
