package jp.co.kojimachi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.EntityArrayAdapter;

public class CustomSpinner extends ConstraintLayout {

    private static final String TAG = "CustomSpinner";

    public CustomSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public interface OnSpinnerClickListener {
        void onClickSpinner(int positionSelect);
    }

    private Context mContext;
    private TextView tvSpinner;
    private CustomArrayAdapter adapter;
    private ArrayList<EntityArrayAdapter> arrayList = new ArrayList<>();
    private TextView valueSpinnerCareer;
    private PopupWindow popupWindow;
    private OnSpinnerClickListener onSpinnerClickListener;
    private boolean isOptionBonus = false;

    public void setOnSpinnerClickListener(OnSpinnerClickListener onSpinnerClickListener) {
        this.onSpinnerClickListener = onSpinnerClickListener;
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_spinner, this);

        tvSpinner = view.findViewById(R.id.tvSpinner);
        initPopupWindow();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSpinner);
            isOptionBonus = typedArray.getBoolean(R.styleable.CustomSpinner_isOptionBonus, false);
            typedArray.recycle();
        }

        if (isOptionBonus) {
            int paddingLeft = (int) context.getResources().getDimension(R.dimen._5sdp);
            tvSpinner.setPadding(paddingLeft, 0, 0, 0);
            tvSpinner.setBackgroundResource(0);
            tvSpinner.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
        }

        tvSpinner.setOnClickListener(v -> {
            try {
                valueSpinnerCareer = tvSpinner;
                popupWindow.showAsDropDown(tvSpinner, 0, 0);
                popupWindow.update(tvSpinner.getWidth(), 400);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void enableArrow() {
        tvSpinner.setEnabled(true);
        tvSpinner.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
    }

    public void disableArrow() {
        tvSpinner.setEnabled(false);
        tvSpinner.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void changeTextColor(int textColor) {
        tvSpinner.setTextColor(textColor);
    }

    public void changListData(ArrayList<EntityArrayAdapter> list) {
        arrayList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public String getText() {
        try {
            if (tvSpinner != null)
                return tvSpinner.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setText(String text) {
        try {
            if (tvSpinner != null)
                tvSpinner.setText(TextUtils.isEmpty(text) || text.equals("null") ? "" : text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_drop_down_popup, null);
            popupWindow = new PopupWindow(view);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            ListView listView = view.findViewById(R.id.listView);
            adapter = new CustomArrayAdapter(mContext, arrayList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view1, position, id) -> {
                onSpinnerClickListener.onClickSpinner(position);
                valueSpinnerCareer.setText(arrayList.get(position).name);
                adapter.changeTextColor(position);
                popupWindow.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
