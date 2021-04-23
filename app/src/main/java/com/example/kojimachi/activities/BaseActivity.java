package com.example.kojimachi.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import jp.co.kojimachi.R;
import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.ExtraConstants;
import jp.co.kojimachi.constant.FragmentConstants;
import jp.co.kojimachi.data.PrefManager;
import jp.co.kojimachi.dialog.DialogConfirm;
import jp.co.kojimachi.dialog.DialogNotice;
import jp.co.kojimachi.entity.prefentity.PrefBoolean;
import jp.co.kojimachi.entity.prefentity.PrefFloat;
import jp.co.kojimachi.entity.prefentity.PrefInt;
import jp.co.kojimachi.entity.prefentity.PrefLong;
import jp.co.kojimachi.entity.prefentity.PrefString;
import jp.co.kojimachi.entity.prefentity.PrefValue;
import jp.co.kojimachi.listener.CallbackAction;
import jp.co.kojimachi.secret.SecretHelper;


/**
 * Created by HERO on 16/01/2017.
 */
public abstract class BaseActivity extends AppCompatActivity implements ApiConstants, FragmentConstants, ExtraConstants {

    private Dialog dialogProgress;
    private DialogNotice dialogNotice;
    private DialogConfirm dialogConfirm;

    public String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    public void finish() {
        closeAllDialog();
        super.finish();
    }

    public void closeAllDialog() {
        try {
            if (dialogProgress != null && dialogProgress.isShowing())
                dialogProgress.dismiss();
            if (dialogNotice != null && dialogNotice.isShowing())
                dialogNotice.dismiss();
            if (dialogConfirm != null && dialogConfirm.isShowing())
                dialogConfirm.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(boolean cancelAble) {
        try {
            if (dialogProgress == null) {
                dialogProgress = new Dialog(this, R.style.dialogNotice);
                dialogProgress.setContentView(R.layout.dialog_progress);
            }

            dialogProgress.setCancelable(cancelAble);
            dialogProgress.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgress() {
        try {
            if (dialogProgress != null)
                dialogProgress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isActOnBackground = false;

    @Override
    protected void onPause() {
        super.onPause();
        isActOnBackground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActOnBackground = false;
    }

    public void showNotice(String message, boolean cancelAble, DialogInterface.OnDismissListener listener) {
        try {
            if (dialogNotice == null) {
                dialogNotice = new DialogNotice(this);
            }

            dialogNotice.show(message, cancelAble, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNotice(int message, boolean cancelAble, DialogInterface.OnDismissListener listener) {
        try {
            if (dialogNotice == null) {
                dialogNotice = new DialogNotice(this);
            }

            dialogNotice.show(message, cancelAble, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideDialogNotice() {
        try {
            if (dialogNotice != null)
                dialogNotice.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfirm(CharSequence title, boolean titleHighLight, CharSequence message,
                            String labelOK, String labelCancel, CallbackAction callback) {
        try {
            if (dialogConfirm == null) {
                dialogConfirm = new DialogConfirm(this).setCancel(false);
            }

            dialogConfirm.show(title, titleHighLight, message, labelOK, labelCancel, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfirm(int title, boolean titleHighLight, int message,
                            int labelOK, int labelCancel, CallbackAction callback) {
        try {
            if (dialogConfirm == null) {
                dialogConfirm = new DialogConfirm(this).setCancel(false);
            }

            dialogConfirm.show(title, titleHighLight, message, labelOK, labelCancel, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(CharSequence msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(int msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOrReplaceFragment(int idContent, Fragment f) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(idContent);
            if (currentFragment != null) {
                fragmentManager.beginTransaction()
                        .replace(idContent, f)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                        .commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction()
                        .add(idContent, f)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                        .commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(int idContent, Fragment f, String tag) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(idContent, f, tag)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragmentAddBackStack(int idContent, Fragment f, String tag) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(idContent, f, tag)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(int idContent, Fragment f, String tag) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .add(idContent, f, tag)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragmentAddBackStack(int idContent, Fragment f, String tag) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .add(idContent, f, tag)
//					.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment findFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void showFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .show(fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideFragment(String tag) {
        try {
            Fragment fragment = findFragment(tag);
            if (fragment != null)
                getSupportFragmentManager().beginTransaction()
                        .hide(fragment)
                        .commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO == pref manager ============================
    private PrefManager prefManager;

    private PrefManager getPrefManager() {
        if (prefManager == null) {
            prefManager = PrefManager.getInstance(this);
        }

        return prefManager;
    }

    public void prefWriteValue(PrefValue prefValue) {
        try {
            if (prefValue instanceof PrefBoolean)
                getPrefManager().writeBoolean(prefValue.key, ((PrefBoolean) prefValue).value);
            else if (prefValue instanceof PrefInt)
                getPrefManager().writeInt(prefValue.key, ((PrefInt) prefValue).value);
            else if (prefValue instanceof PrefLong)
                getPrefManager().writeLong(prefValue.key, ((PrefLong) prefValue).value);
            else if (prefValue instanceof PrefFloat)
                getPrefManager().writeFloat(prefValue.key, ((PrefFloat) prefValue).value);
            else if (prefValue instanceof PrefString)
                getPrefManager().writeString(prefValue.key, ((PrefString) prefValue).value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteBoolean(String key, boolean value) {
        try {
            getPrefManager().writeBoolean(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteString(String key, String value) {
        try {
            getPrefManager().writeString(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteLong(String key, long value) {
        try {
            getPrefManager().writeLong(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteFloat(String key, float value) {
        try {
            getPrefManager().writeFloat(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteInt(String key, int value) {
        try {
            getPrefManager().writeInt(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteList(List<PrefValue> list) {
        try {
            if (list != null && !list.isEmpty())
                getPrefManager().writeList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prefWriteArr(PrefValue... list) {
        try {
            if (list != null && list.length > 0)
                getPrefManager().writeArr(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String prefGetString(String key) {
        try {
            return getPrefManager().getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String prefGetString(String key, String keyDefault) {
        try {
            return getPrefManager().getString(key, keyDefault);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyDefault;
    }

    public boolean prefGetBoolean(String key) {
        try {
            return getPrefManager().getBoolean(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean prefGetBoolean(String key, boolean defValue) {
        try {
            return getPrefManager().getBoolean(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public long prefGetLong(String key) {
        try {
            return getPrefManager().getLong(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long prefGetLong(String key, long defValue) {
        try {
            return getPrefManager().getLong(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public float prefGetFloat(String key) {
        try {
            return getPrefManager().getFloat(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float prefGetFloat(String key, float defValue) {
        try {
            return getPrefManager().getFloat(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public int prefGetInt(String key) {
        try {
            return getPrefManager().getInt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int prefGetInt(String key, int defaultValues) {
        int value;

        try {
            value = getPrefManager().getInt(key, defaultValues);
        } catch (Exception e) {
            e.printStackTrace();
            value = defaultValues;
        }

        return value;
    }

    public boolean prefIsContainsKey(String key) {
        boolean value;

        try {
            value = getPrefManager().containsKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            value = false;
        }

        return value;
    }
    //=================================================

    private SecretHelper secretHelper;

    private SecretHelper getSecretHelper() {
        if (secretHelper == null)
            secretHelper = new SecretHelper();
        return secretHelper;
    }

    protected String getTextDecrypt(String text, String tag) {
        return getSecretHelper().getTextDecrypt(this, text, tag);
    }
}
