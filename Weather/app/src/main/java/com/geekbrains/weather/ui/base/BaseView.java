package com.geekbrains.weather.ui.base;

import android.graphics.Bitmap;

//базовый интерфейс содержащий основные методы
public interface BaseView {

    interface View {

        Boolean inNetworkAvailable();

        void initDrawer(String username, Bitmap profileImage);
    }

    interface Presenter<V> {

        void onAttach(V view);

        void onDetach();

        void onDetachView();
    }
}
