package com.example.kojimachi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.kojimachi.R;


public class DialogNotice extends Dialog {

    private boolean isCancelAble;

    public DialogNotice(Context context) {
        super(context, R.style.dialogWithoutBox);
        setContentView(R.layout.dialog_notice);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        View vContainer = findViewById(R.id.rlContent);
        vContainer.getLayoutParams().width = displayMetrics.widthPixels;
        vContainer.getLayoutParams().height = displayMetrics.heightPixels;
        vContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isCancelAble)
                    dismiss();
            }
        });

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(int message, boolean cancelAble, OnDismissListener dismissListener) {
        try {
            this.isCancelAble = cancelAble;
            setCancelable(cancelAble);
            setOnDismissListener(dismissListener);
            ((TextView) findViewById(R.id.tvMessage)).setText(message);

            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(CharSequence message, boolean cancelAble, OnDismissListener dismissListener) {
        try {
            this.isCancelAble = cancelAble;
            setCancelable(cancelAble);
            setOnDismissListener(dismissListener);
            ((TextView) findViewById(R.id.tvMessage)).setText(message);

            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}