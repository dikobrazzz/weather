package com.geekbrains.weather.retrofit;

import com.geekbrains.weather.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String city, @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeatherCoord(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String keyApi);

}
