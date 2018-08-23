package com.geekbrains.weather.ui.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PrefData;
import com.geekbrains.weather.PrefHelper;
import com.geekbrains.weather.R;

import com.geekbrains.weather.data.DataManager;
import com.geekbrains.weather.data.IDataManager;
import com.geekbrains.weather.ui.base.BaseFragment;
import com.geekbrains.weather.ui.city.CreateActionFragment;

import java.util.ArrayList;

public class WeatherFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback, CreateActionFragment.OnHeadlineSelectedListener {

    private static final String ARG_COUNTRY = "ARG_COUNTRY";
    private String country;
    private TextView textView;
    private FloatingActionButton fab;
    private BottomAppBar bar;
    private PrefHelper prefHelper;
    private IDataManager dataManager;
    private LocationManager locationManager;
    private String locProvider;
    private static final int PERMISSION_REQUEST_CODE = 10;
    String lat;
    String lon;

    public WeatherFragment() {
//        Особенностью поведения android-а состоит в том, что в любой момент
//        он может убить конкретный фрагмент (с случаи нехватки памяти например)
//        и потом попытаться восстановить его, используя конструктор без параметров,
//                следовательно передача параметров через конструкторы черевата
//        крэшами приложения в произвольный момент времени.
    }

    public static WeatherFragment newInstance(String country) {
//        Для того что бы положить требуемые значения во фрагмент,
//        нужно обернуть их в Bundle и передать через метод setArguments.
//        Стандартным способом передачи параметров считается создание статического
//        метода newInstance (...),
//        а для восстановление параметров используется метод getArguments(...),вызываемый в
//        методе жизненного цикла onCreate (...) .
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, country);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            country = getArguments().getString(ARG_COUNTRY);
        }
        prefHelper = new PrefData(getBaseActivity());
        dataManager = new DataManager(getBaseActivity());

        if (ActivityCompat.checkSelfPermission(getBaseActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            requestLocation();
        } else{
            requestLocPermission();
        }
        initRetrofit();
    }

    private void requestLocPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.CALL_PHONE)){
            ActivityCompat.requestPermissions(getBaseActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length == 2 && (grantResults[0]) == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED){
                requestLocation();
            }
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        locationManager = (LocationManager)getBaseActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        locProvider = locationManager.getBestProvider(criteria, true);
        if (locProvider != null){
            locationManager.requestLocationUpdates(locProvider, 10000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    IDataManager dataManager = new DataManager(getBaseActivity());
                    lat = Double.toString(location.getLatitude());
                    lon = Double.toString(location.getLongitude());
                    String acc = Double.toString(location.getAccuracy());
                    dataManager.requestRetrofitC(lat,lon,"06976acfd4bb435e374a9e532d1b1df1");

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    private void initRetrofit() {
        IDataManager dataManager = new DataManager(getBaseActivity());
        dataManager.initRetrofit();
        if (country != null) {
            dataManager.requestRetrofit(country + ",ru", "06976acfd4bb435e374a9e532d1b1df1");
        } else{
            if (lat != null && lon != null){
                dataManager.requestRetrofitC(lat,lon, "06976acfd4bb435e374a9e532d1b1df1");
                textView.setText(Constants.CITY);
            } else dataManager.requestRetrofit("Yekaterinburg,ru", "06976acfd4bb435e374a9e532d1b1df1");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        textView = view.findViewById(R.id.tv_country);

        if (country != null && country.length() > 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(country);
            initRetrofit();
        } else {
            textView.setVisibility(View.GONE);
        }

        String getSP = prefHelper.getSharedPreferences(Constants.CITY);
        if (!getSP.equals("")){
            textView.setVisibility(View.VISIBLE);
            textView.setText(getSP);
            country = getSP;
            initRetrofit();
        }
        //проверяем нашу переменную если она не пустая показываем город, если наоборот - ничего не показываем
        setValues();
    }

    @Override
    public void onResume() {
        bar = getBaseActivity().findViewById(R.id.bottomAppBar);
        bar.setVisibility(View.VISIBLE);
        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onArticleSelected(ArrayList<String> citiesList) {
        textView.setVisibility(View.VISIBLE);
        String cities = citiesList.toString();
//        textView.setText(cities.substring(cities.indexOf("[") + 1, cities.indexOf("]")));
    }

    public void setValues(){
        String temp = prefHelper.getSharedPreferences(Constants.TEMP);
        int tmp = (int) (Float.parseFloat(temp) - 273.1);
        String tmp1 = String.valueOf(tmp);
        String hum = prefHelper.getSharedPreferences(Constants.HUM);
        String pres = prefHelper.getSharedPreferences(Constants.PRES);

        if (tmp > 0){
            ((TextView) getBaseActivity().findViewById(R.id.bigTemp)).setText("+" + tmp1);
        } else {
            ((TextView) getBaseActivity().findViewById(R.id.bigTemp)).setText(tmp1);
        }
        ((TextView) getBaseActivity().findViewById(R.id.tv_humidity)).setText(hum);
        ((TextView) getBaseActivity().findViewById(R.id.tv_pressure)).setText(pres);
    }
}
