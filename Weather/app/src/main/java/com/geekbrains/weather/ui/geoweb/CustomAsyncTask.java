package com.geekbrains.weather.ui.geoweb;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CustomAsyncTask extends AsyncTask<String, String, String> {

    private RequestMaker.OnRequestListener listener;

    public CustomAsyncTask(RequestMaker.OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        listener.onStatusProgress(values[0]);
    }

    @Override
    protected String doInBackground(String... strings) {
        return getResourceUri(strings[0]);
    }

    private String getResourceUri(String uri) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            publishProgress("Загрузка");
            urlConnection.connect();
            publishProgress("Соединяемся");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            publishProgress("Получение данных");
            String line = null;
            int numLine = 0;

            while ((line = bufferedReader.readLine()) != null){
                numLine++;
                publishProgress(String.format("Строка id", numLine));
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            publishProgress("Ошибка");
        } catch (ProtocolException e) {
            e.printStackTrace();
            publishProgress("Ошибка");
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Ошибка");
        }finally {
            if (urlConnection != null) urlConnection.disconnect();

            return "Ошибка получения url";
        }
    }
}
