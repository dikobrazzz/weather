package com.geekbrains.weather;

import android.content.SharedPreferences;

import com.geekbrains.weather.ui.base.BaseActivity;

import static android.content.Context.MODE_PRIVATE;

public class PrefData implements PrefHelper{

    private SharedPreferences sharedPreferences;

    public PrefData(BaseActivity baseActivity) {
        this.sharedPreferences = baseActivity.getPreferences(MODE_PRIVATE);
    }

    @Override
    public String getSharedPreferences(String keyPref) {
        return sharedPreferences.getString(keyPref, "");
    }

    @Override
    public void saveSharedPreferences(String keyPref, String value) {
        sharedPreferences.edit().putString(keyPref, value).apply();
    }

    @Override
    public void deleteSharedPreferences(String keyPref, String value) {
        sharedPreferences.edit().remove(keyPref).apply();
    }
}
