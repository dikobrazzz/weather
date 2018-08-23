package com.geekbrains.weather.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.geekbrains.weather.ui.base.BaseActivity;

import java.io.BufferedOutputStream;
import java.util.List;

import static android.app.Service.START_NOT_STICKY;

public class MyService extends Service implements SensorEventListener{
    IBinder mBinder; // Интерфейс связи с клиентом
    private SensorManager sensorManager;
    private BufferedOutputStream bufferedOutputStream;
    private List<Sensor> listSensors;
    private Sensor sensor;
    @Override
    public void onCreate() {
        // Служба создается
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Служба стартовала
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        listSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        listSensors.get(0).getName();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
    // Привязка клиента
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
    // Удаление привязки
        return true;
    }
    @Override
    public void onRebind(Intent intent) {
    // Перепривязка клиента
    }
    @Override
    public void onDestroy() {
    // Уничтожение службы
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Intent intent = new Intent(BaseActivity.BROADCAST_ACTION);
        intent.putExtra(BaseActivity.SENSOR_VAL, sensorEvent.values);
        sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}