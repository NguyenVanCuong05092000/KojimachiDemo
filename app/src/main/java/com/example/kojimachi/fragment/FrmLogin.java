package com.example.kojimachi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

import jp.co.kojimachi.R;
import jp.co.kojimachi.adapter.AdapterViewPageLogo;
import jp.co.kojimachi.utils.AppUtils;

import static jp.co.kojimachi.constant.AppConstants.ID_DRIVER;
import static jp.co.kojimachi.constant.AppConstants.ID_TECHNICIAN;

public class FrmLogin extends BaseFragment implements View.OnClickListener {
    private final int[] LIST_LOGO_DEFAULT = {R.drawable.img_logo, R.drawable.img_logo2};
    private final int[] LIST_LOGO_TECH = {R.drawable.img_logo};
    private final int[] LIST_LOGO_DRIVER = {R.drawable.img_logo2};
    private EditText edtLoginID;
    private EditText edtLoginPass;
    private ViewPager viewPageLogo;

    public static FrmLogin getInstance() {
        return new FrmLogin();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frm_login;
    }

    @Override
    protected int getCurrentFragment() {
        return 0;
    }

    @Override
    protected void finish() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void loadControlsAndResize(View view) {
        viewPageLogo = view.findViewById(R.id.imgLogo);
        viewPageLogo.getLayoutParams().width = activity.getSizeWithScale(288);
        viewPageLogo.getLayoutParams().height = activity.getSizeWithScale(169);

        View imgLogoText = view.findViewById(R.id.imgLogoText);
        imgLogoText.getLayoutParams().width = activity.getSizeWithScale(247);
        imgLogoText.getLayoutParams().height = activity.getSizeWithScale(53);

        int widthInputCommon = activity.getSizeWithScale(302);
        int heightInputCommon = activity.getSizeWithScale(46);

        edtLoginID = view.findViewById(R.id.edtLoginID);
        edtLoginID.getLayoutParams().width = widthInputCommon;
        edtLoginID.getLayoutParams().height = heightInputCommon;

        edtLoginPass = view.findViewById(R.id.edtLoginPass);
        edtLoginPass.getLayoutParams().width = widthInputCommon;
        edtLoginPass.getLayoutParams().height = heightInputCommon;

        TextView btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.getLayoutParams().width = widthInputCommon;
        btnLogin.getLayoutParams().height = heightInputCommon;

        view.findViewById(R.id.clRoot).setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        viewPageLogo.setOnTouchListener((v, event) -> true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSlideLogo();
        edtLoginID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtLoginID.setNextFocusDownId(R.id.edtLoginPass);
                }
                return false;
            }
        });
        edtLoginPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    AppUtils.hideKeyboard(getView());
                    login();
                }
                return false;
            }
        });
    }

    private int currentPosition = 0;

    private void setupSlideLogo() {
        try {
            AdapterViewPageLogo adapterViewPageLogo = new AdapterViewPageLogo(activity);
            viewPageLogo.setAdapter(adapterViewPageLogo);

            switch (activity.getCurrentRole()) {
                case ID_TECHNICIAN:
                    adapterViewPageLogo.updateListLogo(LIST_LOGO_TECH);
                    break;
                case ID_DRIVER:
                    adapterViewPageLogo.updateListLogo(LIST_LOGO_DRIVER);
                    break;
                default:
                    adapterViewPageLogo.updateListLogo(LIST_LOGO_DEFAULT);
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            viewPageLogo.post(new Runnable() {
                                @Override
                                public void run() {

                                    viewPageLogo.setCurrentItem(currentPosition++, true);
                                }
                            });
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(timerTask, 2000, 2000);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login() {
        try {
            String loginID = edtLoginID.getText().toString().trim();
            String password = edtLoginPass.getText().toString().trim();
            if (TextUtils.isEmpty(loginID) || TextUtils.isEmpty(password)) {
                activity.showNotice(R.string.msg_login_error, true, null);
            } else {
                activity.login(loginID, password);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!isClickAble())
            return;
        AppUtils.hideKeyboard(v);
        switch (v.getId()) {
            case R.id.clRoot:
                AppUtils.hideKeyboard(getView());
                break;
            case R.id.btnLogin:
                login();
                break;
        }
    }

}