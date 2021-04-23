package jp.co.kojimachi.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import jp.co.kojimachi.listener.OnTextChangedListener;


public class CustomCurrencyEditText extends AppCompatEditText implements OnTextChangedListener {

    public CustomCurrencyEditText(Context context) {
        super(context);
        init();
    }

    public CustomCurrencyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomCurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        this.addTextChangedListener(new CurrencyTextWatcher(this, this));
    }

    private OnTextChangedListener onTextChangedListener;

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    @Override
    public void onTextChanged() {
        onTextChangedListener.onTextChanged();
    }
}
