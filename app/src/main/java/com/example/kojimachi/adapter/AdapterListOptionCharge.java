package com.example.kojimachi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.kojimachi.R;
import com.example.kojimachi.entity.EntityArrayAdapter;
import com.example.kojimachi.entity.EntityOptionBonus;
import com.example.kojimachi.utils.AppUtils;
import com.example.kojimachi.view.CustomSpinner;

public class AdapterListOptionCharge extends RecyclerView.Adapter<AdapterListOptionCharge.HolderAdapterCheckBox> {
    private ArrayList<EntityOptionBonus> bonusChargeArrayList;
    private Context context;

    public interface OnClickItemListener {
        void onClickNumber();

        void onClickNameOption(int position);
    }

    private OnClickItemListener onClickItemListener;

    public AdapterListOptionCharge(ArrayList<EntityOptionBonus> bonusChargeArrayList, Context context, OnClickItemListener onClickItemListener) {
        this.bonusChargeArrayList = bonusChargeArrayList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public HolderAdapterCheckBox onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_option, parent, false);
        return new HolderAdapterCheckBox(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HolderAdapterCheckBox holder, int position) {
        if (bonusChargeArrayList != null) {
            EntityOptionBonus entityOptionBonusCharge = bonusChargeArrayList.get(position);
            holder.bindData(entityOptionBonusCharge, position);
        }
    }

    @Override
    public int getItemCount() {
        return bonusChargeArrayList == null ? 0 : bonusChargeArrayList.size();
    }

    public class HolderAdapterCheckBox extends RecyclerView.ViewHolder {
        private TextView tvNameOption;
        private CustomSpinner tvSpinnerNumber;
        private View viewLine;

        public HolderAdapterCheckBox(@NonNull View itemView) {
            super(itemView);
            tvNameOption = itemView.findViewById(R.id.tvNameOption);
            tvSpinnerNumber = itemView.findViewById(R.id.tvSpinnerNumber);
            viewLine = itemView.findViewById(R.id.viewLine);
        }

        @SuppressLint("SetTextI18n")
        private void bindData(EntityOptionBonus entityOptionBonus, int position) {
            try {
                tvNameOption.setText(entityOptionBonus.nameOption);
                setView(entityOptionBonus);
                tvNameOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        entityOptionBonus.isCheck = !entityOptionBonus.isCheck;
                        if (onClickItemListener != null)
                            onClickItemListener.onClickNameOption(position);
                    }
                });
                tvSpinnerNumber.setText(entityOptionBonus.numberOption + "");
                arrayList.clear();
                for (int i = 1; i <= 30; i++) {
                    arrayList.add(new EntityArrayAdapter(String.valueOf(i), false));
                }
                tvSpinnerNumber.changListData(arrayList);
                tvSpinnerNumber.setOnSpinnerClickListener(positionSelect -> {
                    entityOptionBonus.numberOption = AppUtils.parseInt(arrayList.get(positionSelect).name);
                    if (onClickItemListener != null) {
                        onClickItemListener.onClickNumber();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setView(EntityOptionBonus entityOptionBonus) {
            try {
                if (entityOptionBonus.isCheck) {
                    itemView.setBackgroundResource(R.drawable.bg_rc_add_option_seleted);
                    viewLine.setBackgroundResource(R.color.white);
                    tvNameOption.setTextColor(Color.WHITE);
                    tvSpinnerNumber.changeTextColor(Color.WHITE);
                } else if (!entityOptionBonus.isCheckKindCharge) {
                    itemView.setBackgroundResource(R.drawable.bg_rc_add_option_disible);
                    viewLine.setBackgroundResource(R.color.colorMain);
                    tvNameOption.setTextColor(Color.parseColor("#707070"));
                    tvSpinnerNumber.changeTextColor(Color.parseColor("#707070"));
                } else {
                    itemView.setBackgroundResource(R.drawable.bg_rc_add_option);
                    viewLine.setBackgroundResource(R.color.colorMain);
                    tvNameOption.setTextColor(Color.parseColor("#707070"));
                    tvSpinnerNumber.changeTextColor(Color.parseColor("#707070"));
                }
                tvSpinnerNumber.setEnabled(entityOptionBonus.kindCharge != 0);
                if (entityOptionBonus.kindCharge == 0) {
                    tvSpinnerNumber.disableArrow();
                } else
                    tvSpinnerNumber.enableArrow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //init PopupWindow
    private ArrayList<EntityArrayAdapter> arrayList = new ArrayList<>();

}
