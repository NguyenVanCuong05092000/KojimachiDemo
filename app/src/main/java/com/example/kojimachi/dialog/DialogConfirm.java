package com.example.kojimachi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import jp.co.kojimachi.R;
import jp.co.kojimachi.listener.CallbackAction;

public class DialogConfirm extends Dialog {

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnOK;
    private TextView btnCancel;
    private CallbackAction callback;

    public DialogConfirm(Context context) {
        super(context, R.style.dialogWithoutBox);
        setContentView(R.layout.dialog_confirm);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        View vContainer = findViewById(R.id.rlContent);
        vContainer.getLayoutParams().width = displayMetrics.widthPixels;
        vContainer.getLayoutParams().height = displayMetrics.heightPixels;

        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        btnOK = findViewById(R.id.btnOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (callback != null)
                        callback.onActionFinished();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(int title, boolean titleHighLight, int message,
                     int labelOK, int labelCancel, CallbackAction callback) {
        try {
            this.callback = callback;
            if (title == 0)
                tvTitle.setVisibility(View.GONE);
            else {
                tvTitle.setText(title);
                tvTitle.setSelected(titleHighLight);
                tvTitle.setVisibility(View.VISIBLE);
            }
            tvMessage.setText(message);
            btnOK.setText(labelOK);
            btnCancel.setText(labelCancel);

            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(CharSequence title, boolean titleHighLight, CharSequence message,
                     String labelOK, String labelCancel, CallbackAction callback) {
        try {
            this.callback = callback;
            if (TextUtils.isEmpty(title))
                tvTitle.setVisibility(View.GONE);
            else {
                tvTitle.setText(title);
                tvTitle.setSelected(titleHighLight);
                tvTitle.setVisibility(View.VISIBLE);
            }
            tvMessage.setText(message);
            btnOK.setText(labelOK);
            btnCancel.setText(labelCancel);

            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DialogConfirm setCancel(boolean allowCancel) {
        setCancelable(allowCancel);
        return this;
    }
}
