package com.geekbrains.weather.ui.geoweb;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;

import okhttp3.OkHttpClient;

public class GeoFragment extends BaseFragment {
    private TextView tvProgress;
    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_geo_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        tvProgress = view.findViewById(R.id.status);
        webView = view.findViewById(R.id.webView);

//        final RequestMaker requestMaker = new RequestMaker(new RequestMaker.OnRequestListener() {
//            @Override
//            public void onStatusProgress(String updateProgress) {
//                tvProgress.setText(updateProgress);
//            }
//
//            @Override
//            public void onComplete(String result) {
//                webView.loadData(result, "text/html; charset=utf-8", "utf-8");
//            }
//        });

        final OkHttpRequester requester = new OkHttpRequester(new OkHttpRequester.OnResponseCompleted() {
            @Override
            public void onCompleted(String content) {
             webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            }
        });
        requester.run("https://www.gismeteo.ru/weather-yekaterinburg-4517/3-days/");

//        requestMaker.make("https://www.gismeteo.ru/weather-yekaterinburg-4517/3-days/");
    }
}
