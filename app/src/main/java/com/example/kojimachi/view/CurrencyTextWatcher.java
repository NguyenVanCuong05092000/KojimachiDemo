package jp.co.kojimachi.view;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import jp.co.kojimachi.listener.OnTextChangedListener;
import jp.co.kojimachi.utils.AppUtils;

public class CurrencyTextWatcher implements TextWatcher {

    private static final String TAG = "CurrencyTextWatcher";


    private final EditText editText;
    private String previousCleanString;

    int oriLen, changedLen, cp;
    private final OnTextChangedListener onTextChangedListener;
    private String tmpValue = "";


    public CurrencyTextWatcher(EditText editText, OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        previousCleanString = s.toString();
        cp = editText.getSelectionStart();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (onTextChangedListener != null) onTextChangedListener.onTextChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        cp = editText.getSelectionStart();
        oriLen = editText.getText().length();
        boolean isDeleteComma = false;

        String str = editable.toString();
        if (!TextUtils.isEmpty(tmpValue) && cp < tmpValue.length() && tmpValue.length() - str.length() == 1 && tmpValue.charAt(cp) == ',') {
            if (cp - 1 < str.length()) {
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.deleteCharAt(cp - 1);
                str = stringBuilder.toString();
                isDeleteComma = true;
            }
        }
        String cleanString = str.replaceAll("[,]", "");
        if (cleanString.equals(previousCleanString) || cleanString.isEmpty()) {
            return;
        }
        previousCleanString = cleanString;
        String formattedString;
        formattedString = AppUtils.formatPrice(cleanString);
        tmpValue = formattedString;
        editText.removeTextChangedListener(this); // Remove listener
        editText.setText(formattedString);
        if (isDeleteComma) onTextChangedListener.onTextChanged();
        handleSelection();
        editText.addTextChangedListener(this); // Add back the listener
    }


    private void handleSelection() {
        //
        changedLen = editText.getText().length();
        int sel = (cp + (changedLen - oriLen));
        if (sel > 0 && sel <= editText.getText().length()) {
            editText.setSelection(sel);
        }
    }

}