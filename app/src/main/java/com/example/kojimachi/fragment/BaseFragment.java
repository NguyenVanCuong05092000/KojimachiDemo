package com.example.kojimachi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kojimachi.activities.ActMain;
import com.example.kojimachi.constant.ExtraConstants;

public abstract class BaseFragment extends Fragment implements ExtraConstants {
    protected String TAG = this.getClass().getSimpleName();

    protected abstract int getLayoutResId();

    protected abstract int getCurrentFragment();

    protected abstract void finish();

    public boolean isBackPreviousEnable() {
        return false;
    }

    public void backToPrevious() {
    }

    public boolean isMenu() {
        return false;
    }

    protected ActMain activity;
    //TODO click manager
    private boolean mIsClickAble = true;
    private Handler mHandlerClick = new Handler();
    private Runnable changeStateClickAble = new Runnable() {
        @Override
        public void run() {
            mIsClickAble = true;
        }
    };

    public boolean isClickAble() {
        if (mIsClickAble) {
            mIsClickAble = false;
            mHandlerClick.removeCallbacks(changeStateClickAble);
            mHandlerClick.postDelayed(changeStateClickAble, 350);
            return true;
        }

        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ActMain) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        if (activity == null)
            activity = (ActMain) getActivity();
        loadControlsAndResize(view);
        return view;
    }

    protected abstract void loadControlsAndResize(View view);

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null)
            activity = (ActMain) getActivity();

        if (!isMenu() && getCurrentFragment() != -1)
            activity.setCurrentScreen(getCurrentFragment());
    }

    protected void showToast(int msg) {
        try {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showToast(String msg) {
        try {
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPermissionsGranted() {
    }

    public void onPermissionsDenied() {
    }

    public void setText(TextView textView, String text) {
        try {
            if (textView != null)
                textView.setText(TextUtils.isEmpty(text) || text.equals("null") ? "" : text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText(TextView textView) {
        try {
            if (textView != null)
                return textView.getText().toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onDestroy() {
        try {
            mHandlerClick.removeCallbacks(changeStateClickAble);
            System.gc();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
