package com.geekbrains.weather.ui.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.geekbrains.weather.ui.base.BaseActivity;
import com.geekbrains.weather.ui.base.BaseView;

public abstract class BaseFragment extends Fragment implements BaseView.View {

    //это базовый фрагмент от которого будут наследоваться все остальные наши фрагменты
    // необходим для того чтобы не писать один и тот же код несколько раз подряд
    private BaseActivity baseActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //указываем что после того как view фрагмента создано нужно вызвать метод initLayout
        initLayout(view, savedInstanceState);
    }

    protected abstract void initLayout(View view, Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //когда наш фрагмент "прицепился" к активности получаем ее параметры чтобы потом обращаться к ней через getter
        baseActivity = (BaseActivity) context;
        baseActivity.onFragmentAttached();
    }

    @Override
    public void onDetach() {
        //обнуляем когда "отцепляемся" от активности
        baseActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    @Override
    public Boolean inNetworkAvailable() {
        //проверка доступа к сети, пока не работает, тк мы еще не работали с сетью
        if (baseActivity != null) {
            return baseActivity.inNetworkAvailable();
        }
        return false;
    }

    @Override
    public void initDrawer(String username, Bitmap profileImage) {
        //инициализация выездной навигации
    }


    interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}
