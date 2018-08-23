package com.geekbrains.weather.ui.city;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.geekbrains.weather.R;

import java.util.ArrayList;
import java.util.List;

public class DataSourceBuilder {
    private List<Soc> dataSource; // Строим этот источник данных
    private Resources resources; // Ресурсы приложения
    public DataSourceBuilder(Resources resources) {
        dataSource = new ArrayList<>(6);
        this.resources = resources;
    }
    // Строим данные
    public List<Soc> build() {
// Строки описаний из ресурсов
        String[] descriptions = resources.getStringArray(R.array.descriptions);
// Изображения
        int[] pictures = getImageArray();
// Заполнение источника данных
        for (int i = 0; i < descriptions.length; i++) {
            dataSource.add(new Soc(descriptions[i], pictures[i], false));
        }
        return dataSource;
    }
    // Механизм вытаскивания идентификаторов картинок (к сожалению, просто
//    массив не работает)
//

    private int[] getImageArray(){
        TypedArray pictures = resources.obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for(int i = 0; i < length; i++){
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }
}
