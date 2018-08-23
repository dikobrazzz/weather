package com.geekbrains.weather;

public interface PrefHelper {

    public String getSharedPreferences(String keyPref);

    public void saveSharedPreferences(String keyPref, String value);

    public void deleteSharedPreferences(String keyPref, String value);
}
