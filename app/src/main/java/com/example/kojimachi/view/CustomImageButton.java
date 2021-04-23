package jp.co.kojimachi.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageButton extends AppCompatImageView {

    /**
     * @param context
     */
    public CustomImageButton(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //setBackground(null);
    }

    /*
     *
     * @see android.widget.ImageView#drawableStateChanged()
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Drawable drawable = getDrawable();
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
                getDrawable().setColorFilter(0x7F000000, PorterDuff.Mode.SRC_ATOP);
                return;
            }
        }
        getDrawable().setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
    }
}
