package com.nan.noisedetector.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.Map;
import java.util.Set;

public class PreferenceUtil {

    @SuppressLint("StaticFieldLeak")
    private volatile static PreferenceUtil instance ;
    private SharedPreferences preferences;
    private Context context;

    static PreferenceUtil getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null){
            synchronized (PreferenceUtil.class) {
                if (instance == null){
                    instance = new PreferenceUtil(context);
                }
            }
        }
    }

    private PreferenceUtil(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences.Editor getEdit() {
        checkNull();
        return preferences.edit();
    }

    public boolean putString(String key, @Nullable String value) {
        checkNull();
        return preferences.edit().putString(key, value).commit();
    }

    public boolean putString(@StringRes int resId, @Nullable String value) {
        checkNull();
        return preferences.edit().putString(context.getString(resId), value).commit();
    }

    public boolean putStringSet(String key, @Nullable Set<String> values) {
        checkNull();
        return preferences.edit().putStringSet(key, values).commit();
    }

    public boolean putStringSet(@StringRes int resId, @Nullable Set<String> values) {
        checkNull();
        return preferences.edit().putStringSet(context.getString(resId), values).commit();
    }

    public boolean putInt(String key, int value) {
        checkNull();
        return preferences.edit().putInt(key, value).commit();
    }

    public boolean putInt(@StringRes int resId, int value) {
        checkNull();
        return preferences.edit().putInt(context.getString(resId), value).commit();
    }

    public boolean putLong(String key, long value) {
        checkNull();
        return preferences.edit().putLong(key, value).commit();
    }

    public boolean putLong(@StringRes int resId, long value) {
        checkNull();
        return preferences.edit().putLong(context.getString(resId), value).commit();
    }

    public boolean putFloat(String key, float value) {
        checkNull();
        return preferences.edit().putFloat(key, value).commit();
    }

    public boolean putFloat(@StringRes int resId, float value) {
        checkNull();
        return preferences.edit().putFloat(context.getString(resId), value).commit();
    }

    public boolean putBoolean(String key, boolean value) {
        checkNull();
        return preferences.edit().putBoolean(key, value).commit();
    }

    public boolean putBoolean(@StringRes int resId, boolean value) {
        checkNull();
        return preferences.edit().putBoolean(context.getString(resId), value).commit();
    }

    public boolean putLongArray(Map<String,Long> longMap){
        checkNull();
        if (longMap == null || longMap.isEmpty()) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : longMap.keySet()){
            editor.putLong(key,longMap.get(key));
        }
        return editor.commit();
    }

    public boolean remove(String key) {
        checkNull();
        return preferences.edit().remove(key).commit();
    }

    public boolean clear() {
        checkNull();
        return preferences.edit().clear().commit();
    }

    public Map<String, ?> getAll() {
        checkNull();
        return preferences.getAll();
    }

    public String getString(String key, @Nullable String defValue) {
        checkNull();
        return preferences.getString(key, defValue);
    }

    public String getString(@StringRes int resId, @Nullable String defValue) {
        checkNull();
        return preferences.getString(context.getString(resId), defValue);
    }

    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        checkNull();
        return preferences.getStringSet(key, defValues);
    }

    public Set<String> getStringSet(@StringRes int resId, @Nullable Set<String> defValues) {
        checkNull();
        return preferences.getStringSet(context.getString(resId), defValues);
    }

    public int getInt(String key, int defValue) {
        checkNull();
        return preferences.getInt(key, defValue);
    }

    public int getInt(@StringRes int resId, int defValue) {
        checkNull();
        return preferences.getInt(context.getString(resId), defValue);
    }

    public long getLong(String key, long defValue) {
        checkNull();
        return preferences.getLong(key, defValue);
    }

    public long getLong(@StringRes int resId, long defValue) {
        checkNull();
        return preferences.getLong(context.getString(resId), defValue);
    }

    public float getFloat(String key, float defValue) {
        checkNull();
        return preferences.getFloat(key, defValue);
    }

    public float getFloat(@StringRes int resId, float defValue) {
        checkNull();
        return preferences.getFloat(context.getString(resId), defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        checkNull();
        return preferences.getBoolean(key, defValue);
    }

    public boolean getBoolean(@StringRes int resId, boolean defValue) {
        checkNull();
        return preferences.getBoolean(context.getString(resId), defValue);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    private void checkNull() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }
}
