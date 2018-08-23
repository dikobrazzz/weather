package com.geekbrains.weather.ui.geoweb;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequester {
//    private OkHttpClient client;
    private OnResponseCompleted responseCompleted;

    public OkHttpRequester(OnResponseCompleted responseCompleted) {
//        this.client = new OkHttpClient();
        this.responseCompleted = responseCompleted;
    }

    public void run(String url){
        OkHttpClient client = new OkHttpClient();
        Request.Builder  builder = new Request.Builder();
        builder.url(url);

        Request request = builder.build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            final Handler handler = new Handler();
            @Override
            public void onFailure(Call call, IOException e) {
                fail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String answer = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseCompleted.onCompleted(answer);
                    }
                });
            }
        });

//        Request.Builder builder = new Request.Builder();
//        builder.url(url);
//        Request request = builder.build();
//
//        try (Response response = client.newCall(request).execute()){
//           String answer = response.body().string();
//           responseCompleted.onCompleted(answer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void fail(IOException e) {
        Log.e("OkHttp",e.getMessage(), e);
    }

    public interface OnResponseCompleted {
        void onCompleted(String content);
    }

    static class Requester extends AsyncTask<String,Void,String>{
        OnResponseCompleted completed;

        public Requester(OnResponseCompleted completed) {
            this.completed = completed;
        }

        @Override
        protected String doInBackground(String... content) {
            return getContent(content[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            completed.onCompleted(s);
        }

        private String getContent(String url) {
            OkHttpClient client = new OkHttpClient();
            Request.Builder  builder = new Request.Builder();
            builder.url(url);

            Request request = builder.build();

            try (Response response = client.newCall(request).execute()){
                return response.body().string();
            } catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
    }
}
