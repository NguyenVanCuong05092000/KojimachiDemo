package com.example.kojimachi.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

public class AutoShrinkTextView extends AppCompatTextView {

    /**
     * 最小のテキストサイズ。
     */
    private static final float MIN_TEXT_SIZE = 6.0f;

    /**
     * テキスト幅計測用のペイント。
     */
    private Paint mPaint = new Paint();

    /**
     * @param context
     */
    public AutoShrinkTextView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public AutoShrinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AutoShrinkTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float measuredWidth = getMeasuredWidth();
        if (measuredWidth > 0) {
            shrinkTextSize();
        }
    }

    /**
     * テキストサイズを縮小します。
     */
    private void shrinkTextSize() {
        // テキストサイズを取得します。
        float tempTextSize = getTextSize();

        String text = getText().toString();
        if (!TextUtils.isEmpty(text) && text.contains("\n")) {
            String[] arrText = text.split("\n");
            float currentWidth = 0;
            text = null;
            for (String child : arrText) {
                float textWidth = getTextWidth(MIN_TEXT_SIZE, child);
                if (currentWidth < textWidth) {
//					if (AppConstants.LOG_DEBUG) Log.e("AutoShrinkTextView",
//							"currentWidth:" + currentWidth + " - textWidth:" + textWidth
//									+ "\n" + text + "\n->" + child);
                    text = child;
                    currentWidth = textWidth;
                }
            }
        }
        // テキスト幅が入りきっていない場合は、入るまで繰り返します。
        while (getMeasuredWidth() < getTextWidth(tempTextSize, text)) {
            // テキストサイズを縮小します。
            tempTextSize--;

            if (tempTextSize <= MIN_TEXT_SIZE) {
                // 最小テキストサイズより小さくなった場合は、最小テキストサイズをセットして終了します。
                tempTextSize = MIN_TEXT_SIZE;
                break;
            }
        }

        // 調整した結果のテキストサイズをセットします。
        setTextSize(TypedValue.COMPLEX_UNIT_PX, tempTextSize);
    }

    /**
     * テキスト幅を取得します。
     *
     * @param text
     * @param textSize
     * @return
     */
    float getTextWidth(float textSize, String text) {
        mPaint.setTextSize(textSize);
        return mPaint.measureText(text);
    }

    float getTextHeight(float textSize, String text) {
        mPaint.setTextSize(textSize);
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }
}
