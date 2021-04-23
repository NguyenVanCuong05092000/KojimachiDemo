package com.example.kojimachi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.appcompat.widget.AppCompatButton;

import com.example.kojimachi.R;
import com.example.kojimachi.constant.AppConstants;

public class CustomButton extends AppCompatButton {

    /**
     * @param context
     */
    public CustomButton(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isButtonNotShadow = false;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
            isButtonNotShadow = typedArray.getBoolean(R.styleable.CustomButton_isButtonNotShadow, false);
            typedArray.recycle();
        }

        if (!isButtonNotShadow)
            setPadding(0, 0, 0, getSizeWithScale(18));
    }

    /*
     *
     * @see android.widget.ImageView#drawableStateChanged()
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Drawable drawable = getBackground();
        if (drawable == null) {
            return;
        }

        int[] states = getDrawableState();

        int length = states.length;
        for (int i = 0; i < length; i++) {
            if (states[i] == android.R.attr.state_focused || states[i] == android.R.attr.state_pressed) {
                drawable.setColorFilter(0x7F000000, PorterDuff.Mode.SRC_ATOP);
                return;
            }
        }
        drawable.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }

    /**
     *
     */
    public void reinitDrawableState() {
        int[] states = getDrawableState();

        int length = states.length;
        for (int i = 0; i < length; i++) {
            if (states[i] == android.R.attr.state_focused || states[i] == android.R.attr.state_pressed) {
                getBackground().setColorFilter(0x7F000000, PorterDuff.Mode.SRC_ATOP);
                return;
            }
        }
        getBackground().setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }

    //
    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getContext().getResources().getDisplayMetrics();
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
