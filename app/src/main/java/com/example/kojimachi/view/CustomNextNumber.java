package jp.co.kojimachi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;

import jp.co.kojimachi.R;
import jp.co.kojimachi.constant.AppConstants;

public class CustomNextNumber extends ConstraintLayout {
    public CustomNextNumber(Context context) {
        super(context);
        init(context);
    }

    public CustomNextNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private Context context;

    private ImageView imgNext;
    private ImageView imgPre;
    private TextView tvNumberPieces;
    private int numberPieces;
    private int minNumberPieces;
    private int maxNumberPieces;
    public void setMinNumberPieces(int minNumberPieces) {
        this.minNumberPieces = minNumberPieces;
        checkMinMax();
    }

    public int getNumberPieces() {
        return numberPieces;
    }

    public void setMaxNumberPieces(int maxNumberPieces) {
        this.maxNumberPieces = maxNumberPieces;
        checkMinMax();
    }

    public void checkMinMax() {
        try {
                if (numberPieces <= minNumberPieces) {
                    disableIcon(true);
                } else enableIcon(true);
                if (numberPieces >= maxNumberPieces ) {
                    disableIcon(false);
                } else {
                    enableIcon(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableIcon(boolean isLeft) {
        if (isLeft) {
            imgPre.setImageResource(R.drawable.ic_iv_minus);
            imgPre.setEnabled(true);
        } else {
            imgNext.setImageResource(R.drawable.ic_iv_plus);
            imgNext.setEnabled(true);
        }
    }

    private void disableIcon(boolean isLeft) {
        if (isLeft) {
            imgPre.setImageResource(R.drawable.ic_iv_minus_grey);
            imgPre.setEnabled(false);
        } else {
            imgNext.setImageResource(R.drawable.ic_iv_plus_grey);
            imgNext.setEnabled(false);
        }
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_next_number, this);
        tvNumberPieces = view.findViewById(R.id.tvNumberPieces);
        imgNext = view.findViewById(R.id.ivPlus);
        imgNext.getLayoutParams().width = getSizeWithScale(24);
        imgNext.getLayoutParams().height = getSizeWithScale(24);
        imgPre = view.findViewById(R.id.ivMinus);
        imgPre.getLayoutParams().width = getSizeWithScale(24);
        imgPre.getLayoutParams().height = getSizeWithScale(24);
        numberPieces = 0;
            tvNumberPieces.setText(String.valueOf(numberPieces));
            imgPre.setOnClickListener(v -> {
                preNumber();
            });
            imgNext.setOnClickListener(v -> {
                nextNumber();
            });

    }

    private void preNumber() {
        try {
            numberPieces = numberPieces - 1;
            checkMinMax();
            tvNumberPieces.setText(String.valueOf(numberPieces));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void nextNumber() {
        try {
            numberPieces = numberPieces + 1;
            checkMinMax();
            tvNumberPieces.setText(String.valueOf(numberPieces));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //RESIZE
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = context.getResources().getDisplayMetrics();
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
