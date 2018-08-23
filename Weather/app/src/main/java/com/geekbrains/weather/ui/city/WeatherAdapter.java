package com.geekbrains.weather.ui.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PrefData;
import com.geekbrains.weather.PrefHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseActivity;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Soc> dataSource; // Наш источник данных
    private OnItemClickListener itemClickListener;
    private CreateActionFragment.OnHeadlineSelectedListener mCallback;
    private Context context;
    private PrefHelper prefHelper;
    // Слушатель, будет
//    устанавливаться извне
    // Этот класс хранит связь между данными и элементами View
// Сложные данные могут потребовать несколько View на
// один пункт списка.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public ImageView picture;
        public CheckBox like;
        public ViewHolder(View v) {
            super(v);
            description = v.findViewById(R.id.description);
            picture = v.findViewById(R.id.picture);
            like = v.findViewById(R.id.like);
// Обработчик нажатий на этом ViewHolder
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }
    // Интерфейс для обработки нажатий как в ListView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    // Сеттер слушателя нажатий
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
// Передаем в конструктор источник данных
// В нашем случае это массив, но может быть и запросом к БД
public WeatherAdapter(Context context, List<Soc> dataSource, CreateActionFragment.OnHeadlineSelectedListener mCallback, BaseActivity baseActivity) {
        this.context = context;
        this.dataSource = dataSource;
        this.mCallback = mCallback;
        prefHelper = new PrefData(baseActivity);

}
    // Создать новый элемент пользовательского интерфейса
// Запускается менеджером
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
// Создаем новый элемент пользовательского интерфейса
// Через Inflater
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
// Здесь можно установить всякие параметры
        ViewHolder vh = new ViewHolder(v);
// На каком-то этапе будет переиспользование карточки, и в лог эта строка
//        не попадет
// а строка onBindViewHolder попадет. Это будет означать, что старая
//        карточка
// переоткрыта с новыми данными
        Log.d("WeatherAdapter", "onCreateViewHolder");
        return vh;
    }
    // Заменить данные в пользовательском интерфейсе
// Вызывается менеджером
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
// Получить элемент из источника данных (БД, интернет...)
        Soc item = dataSource.get(position);
// Вынести на экран используя ViewHolder
        holder.description.setText(item.getDescription());
        holder.picture.setImageResource(item.getPicture());
        holder.like.setChecked(item.getLike());
        saveInPref(item.getDescription());

// Отрабатывает при необходимости нарисовать карточку
        Log.d("SocnetAdapter", "onBindViewHolder");
    }

//    private void deleteFromPref(String description) {
//        prefHelper.deleteSharedPreferences(Constants.CITY, description);
//    }
//
    private void saveInPref(String s) {
        prefHelper.saveSharedPreferences(Constants.CITY, s);
    }

    // Вернуть размер данных, вызывается менеджером
    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}

