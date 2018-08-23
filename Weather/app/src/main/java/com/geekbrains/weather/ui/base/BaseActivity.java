package com.geekbrains.weather.ui.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PrefData;
import com.geekbrains.weather.PrefHelper;
import com.geekbrains.weather.data.DataManager;
import com.geekbrains.weather.data.IDataManager;
import com.geekbrains.weather.service.MyService;
import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.city.CreateActionFragment;
import com.geekbrains.weather.ui.geoweb.GeoFragment;
import com.geekbrains.weather.ui.plan.PlanFragment;
import com.geekbrains.weather.ui.weather.WeatherFragment;

import java.util.List;

public class BaseActivity extends AppCompatActivity
        implements BaseView.View, BaseFragment.Callback, NavigationView.OnNavigationItemSelectedListener {

    public static final String BROADCAST_ACTION = "BROADCAST_ACTION";
    public static final String SENSOR_VAL = "SENSOR_VAL";
    private static final int PERMISSION_REQUEST_CODE = 111;
    private static final String MY_EXTRA = "MY_EXTRA";

    //инициализация переменных
    private FloatingActionButton fab;
    private TextView textView;
    private static final String TEXT = "TEXT";
    private static String country;
    private boolean isChecked = false;
    private SensorManager sensorManager;
    private List<Sensor> listSensors;
    private Sensor sensor;
    private BroadcastReceiver broadcastReceiver;
    private static final String MY_ACTION = "MY_ACTION";
    private String myData = "myData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            TextView tv = findViewById(R.id.tvUsername);
            country = savedInstanceState.getString("NAME");
        }
        setContentView(R.layout.activity_base);

//        initRetrofit();

        initLayout();

        startService();

        startReceiver();
    }

    void startReceiver(){
        Intent intent = new Intent(MY_ACTION);
        intent.putExtra(MY_EXTRA, myData);
        sendBroadcast(intent);


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String test = intent.getStringExtra("Test");
            }
        };
        IntentFilter intentFilter = new IntentFilter("com.geekbrains.weather.receiver.SMSReceiver");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void startService(){
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        Intent intent = new Intent(BaseActivity.this, MyService.class);
        startService(intent);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = intent.getStringExtra(SENSOR_VAL);
//                Log.d(SENSOR_VAL, value);
            }
        };

        registerReceiver(broadcastReceiver, filter);

//        initSensor();

        //запуск сервиса
        startService(new Intent(BaseActivity.this, MyService.class));
    }

//    private void initRetrofit() {
//        IDataManager dataManager = new DataManager(this);
//        dataManager.initRetrofit();
//        if (country != null) {
//            dataManager.requestRetrofit(country + ",ru", "06976acfd4bb435e374a9e532d1b1df1");
//        } else
//         dataManager.requestRetrofit("Yekaterinburg,ru", "06976acfd4bb435e374a9e532d1b1df1");
//    }

//    private void initSensor() {
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        listSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
//        listSensors.get(0).getName();
//    }

    private void initLayout() {
        //устанавливает тулбар
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //устанавливаем drawer (выездное меню)
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //анимация клавищи (три палочки сверху) выездного меня
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //инициализация навигации
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new CreateActionFragment());
            }
        });

        //addFragment(new WeatherFragment());
        startWeatherFragment(country);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addFragment1(new CreateActionFragment());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("NAME", ((TextView) findViewById(R.id.tvUsername)).getText().toString());
        super.onSaveInstanceState(outState);
    }


    private void addFragment(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                //.addToBackStack("")
                .commit();
    }

    private void addFragment1(Fragment fragment) {
        //вызываем SupportFragmentManager и указываем в каком контейнере будет находиться наш фрагмент
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame2, fragment)
                //.addToBackStack("")
                .commit();
    }


    private Fragment getCurrentFragment() {
        //получаем наименование фрагмента находящегося в контейнере в данных момент
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        //закрываем drawer если он был открыт при нажатии на аппаратную клавишу назад
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getCurrentFragment() instanceof CreateActionFragment) {
            addFragment(new WeatherFragment());
        } else if (getCurrentFragment() instanceof GeoFragment) {
            addFragment(new WeatherFragment());
        } else if (getCurrentFragment() instanceof PlanFragment) {
            addFragment(new WeatherFragment());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                Intent choose = Intent.createChooser(intent, "Select app");
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(choose);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestForCallPerm() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1234567"));
                startActivity(intent);
            }
        }
    }

    //работаем с навигацией
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_info) {
            // Handle the camera action
        } else if (id == R.id.nav_web) {
            addFragment(new GeoFragment());
        } else if (id == R.id.nav_home) {
            addFragment(new WeatherFragment());
        }else if (id == R.id.nav_note) {
            addFragment(new PlanFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Boolean inNetworkAvailable() {
        return true;
    }

    @Override
    public void initDrawer(String username, Bitmap profileImage) {

    }

    @Override
    public void onFragmentAttached() {


    }

    @Override
    public void onFragmentDetached(String tag) {

    }


    public void startWeatherFragment(String country) {
        //запускаем WeatherFragment и передаем туда country
        addFragment(WeatherFragment.newInstance(country));
    }

    public Fragment getAnotherFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);

    }

//    SensorEventListener listener = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent sensorEvent) {
//            showLightSensors(sensorEvent);
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int i) {
//
//        }
//    };
//
//    private void showLightSensors(SensorEvent sensorEvent) {
//        String light = sensorEvent.values.toString();
//    }

}

