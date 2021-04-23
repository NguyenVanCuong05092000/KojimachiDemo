package com.example.kojimachi.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import com.example.kojimachi.constant.PrefConstants;
import com.example.kojimachi.entity.prefentity.PrefBoolean;
import com.example.kojimachi.entity.prefentity.PrefFloat;
import com.example.kojimachi.entity.prefentity.PrefInt;
import com.example.kojimachi.entity.prefentity.PrefLong;
import com.example.kojimachi.entity.prefentity.PrefString;
import com.example.kojimachi.entity.prefentity.PrefValue;

public class PrefManager implements PrefConstants {

    private static SharedPreferences settings;

    private PrefManager() {
    }

    private static com.example.kojimachi.data.PrefManager prefManager;

    public static com.example.kojimachi.data.PrefManager getInstance(Context context) {
        if (prefManager == null) {
            prefManager = new com.example.kojimachi.data.PrefManager();
            settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return prefManager;
    }

    public void writeList(List<PrefValue> list) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            for (PrefValue prefValue : list) {
                if (prefValue instanceof PrefBoolean)
                    ed.putBoolean(prefValue.key, ((PrefBoolean) prefValue).value);
                else if (prefValue instanceof PrefInt)
                    ed.putInt(prefValue.key, ((PrefInt) prefValue).value);
                else if (prefValue instanceof PrefLong)
                    ed.putLong(prefValue.key, ((PrefLong) prefValue).value);
                else if (prefValue instanceof PrefFloat)
                    ed.putFloat(prefValue.key, ((PrefFloat) prefValue).value);
                else if (prefValue instanceof PrefString)
                    ed.putString(prefValue.key, ((PrefString) prefValue).value);
            }
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeArr(PrefValue[] list) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            for (PrefValue prefValue : list) {
                if (prefValue instanceof PrefBoolean)
                    ed.putBoolean(prefValue.key, ((PrefBoolean) prefValue).value);
                else if (prefValue instanceof PrefInt)
                    ed.putInt(prefValue.key, ((PrefInt) prefValue).value);
                else if (prefValue instanceof PrefLong)
                    ed.putLong(prefValue.key, ((PrefLong) prefValue).value);
                else if (prefValue instanceof PrefFloat)
                    ed.putFloat(prefValue.key, ((PrefFloat) prefValue).value);
                else if (prefValue instanceof PrefString)
                    ed.putString(prefValue.key, ((PrefString) prefValue).value);
            }
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeValues(PrefValue... list) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            for (PrefValue prefValue : list) {
                if (prefValue instanceof PrefBoolean)
                    ed.putBoolean(prefValue.key, ((PrefBoolean) prefValue).value);
                else if (prefValue instanceof PrefInt)
                    ed.putInt(prefValue.key, ((PrefInt) prefValue).value);
                else if (prefValue instanceof PrefLong)
                    ed.putLong(prefValue.key, ((PrefLong) prefValue).value);
                else if (prefValue instanceof PrefFloat)
                    ed.putFloat(prefValue.key, ((PrefFloat) prefValue).value);
                else if (prefValue instanceof PrefString)
                    ed.putString(prefValue.key, ((PrefString) prefValue).value);
            }
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBoolean(String key, boolean holder) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            ed.putBoolean(key, holder);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeLong(String key, long holder) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            ed.putLong(key, holder);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFloat(String key, float holder) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            ed.putFloat(key, holder);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInt(String key, int holder) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            ed.putInt(key, holder);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeString(String key, String holder) {
        try {
            SharedPreferences.Editor ed = settings.edit();
            ed.putString(key, holder);
            ed.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        try {
            return settings.getString(key, "");
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(String key, String keyDefault) {
        try {
            return settings.getString(key, keyDefault);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyDefault;
    }

    public boolean getBoolean(String key) {
        try {
            return settings.getBoolean(key, false);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getBoolean(String key, boolean defValue) {
        try {
            return settings.getBoolean(key, defValue);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public long getLong(String key) {
        try {
            return settings.getLong(key, -1);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getLong(String key, long defValue) {
        try {
            return settings.getLong(key, defValue);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public float getFloat(String key) {
        try {
            return settings.getFloat(key, 0);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float getFloat(String key, float defValue) {
        try {
            return settings.getFloat(key, defValue);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public int getInt(String key) {
        try {
            return settings.getInt(key, 0);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getInt(String key, int defaultValues) {
        int value;

        try {
            value = settings.getInt(key, defaultValues);
        } catch (ClassCastException e) {
            e.printStackTrace();
            value = defaultValues;
        } catch (Exception e) {
            e.printStackTrace();
            value = defaultValues;
        }

        return value;
    }

    public boolean containsKey(String key) {
        try {
            return settings.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void releaseMemoryFromKey(String key) {
        SharedPreferences.Editor ed = settings.edit();
        ed.remove(key);
        ed.commit();
    }

    public void releaseMemory() {
        SharedPreferences.Editor ed = settings.edit();
        ed.clear();
        ed.commit();
    }
}
