package com.geekbrains.weather.data;

import android.util.Log;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PrefData;
import com.geekbrains.weather.PrefHelper;
import com.geekbrains.weather.retrofit.OpenWeather;
import com.geekbrains.weather.ui.base.BaseActivity;
import com.geekbrains.weather.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataManager implements IDataManager{
    public DataManager(BaseActivity baseActivity) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        prefHelper = new PrefData(baseActivity);
    }

    private static final String BASE_URL = "https://api.openweathermap.org";
    private static final String TAG = "DataManager";
    private OpenWeather openWeather;
    private PrefHelper prefHelper;
    double temp;
    int humidity;
    int pressure;


    @Override
    public void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);

    }

    @Override
    public void requestRetrofitC(String lat, String lon, String keyApi){
        openWeather.loadWeatherCoord(lat, lon, keyApi).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response != null){
                    Log.d(TAG, response.body().toString());
                    saveInPref(response.body().getMain().getTemp().toString(),
                            response.body().getMain().getHumidity().toString(),
                            response.body().getMain().getPressure().toString(),
                            response.body().getName().toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {

            }
        });
    }

    @Override
    public void requestRetrofit(String city, String keyApi){
        openWeather.loadWeather(city,keyApi).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response != null){
                    Log.d(TAG, response.body().toString());
                    saveInPref(response.body().getMain().getTemp().toString(),
                            response.body().getMain().getHumidity().toString(),
                            response.body().getMain().getPressure().toString(),
                            response.body().getName().toString());
//                    double temp = response.body().getMain().getTemp();
//                    int humidity = response.body().getMain().getHumidity();
//                    int pressure = response.body().getMain().getPressure();
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void saveInPref(String temp, String hum, String pres, String city) {
        prefHelper.saveSharedPreferences(Constants.TEMP, temp);
        prefHelper.saveSharedPreferences(Constants.HUM, hum);
        prefHelper.saveSharedPreferences(Constants.PRES, pres);
        prefHelper.saveSharedPreferences(Constants.CITY, city);
    }
}
