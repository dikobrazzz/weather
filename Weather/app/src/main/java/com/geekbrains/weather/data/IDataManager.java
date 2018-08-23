package com.geekbrains.weather.data;

public interface IDataManager {
    public void initRetrofit();

    public void requestRetrofit(String city, String keyApi);

    public void requestRetrofitC(String lat, String lon, String keyApi);

}
