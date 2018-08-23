package com.geekbrains.weather.ui.city;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.PrefData;
import com.geekbrains.weather.PrefHelper;
import com.geekbrains.weather.R;
import com.geekbrains.weather.ui.base.BaseFragment;

import java.util.ArrayList;

public class CreateActionFragment extends BaseFragment {

    private EditText editTextCountry;
    private RecyclerView recyclerView;
    OnHeadlineSelectedListener mCallback;
    private LinearLayout linearLayout;
    private ArrayList<String> cityList;
    private TextInputLayout textInputLayout;
    private PrefHelper prefHelper;

    public interface OnHeadlineSelectedListener {
        void onArticleSelected(ArrayList<String> position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(getContext(), "onAttachAction", Toast.LENGTH_SHORT).show();

        try {
            mCallback = (OnHeadlineSelectedListener) getBaseActivity().getAnotherFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getBaseActivity().getAnotherFragment().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //обращаемся к layout который будет содержать наш фрагмент
        return inflater.inflate(R.layout.create_action_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        prefHelper = new PrefData(getBaseActivity());
        initCountryList();

        recyclerView = view.findViewById(R.id.list_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DataSourceBuilder builder = new DataSourceBuilder(getResources());
// Установим адаптер

        final WeatherAdapter adapter = new WeatherAdapter(getContext(), builder.build(), mCallback, getBaseActivity());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WeatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getBaseActivity().startWeatherFragment(cityList.get(position));
                for (int i = 0; i < cityList.size(); i++) {
                    if (i == position){
                        saveInPref(cityList.get(position));
                    }else deleteFromPref(cityList.get(i));
                }
            }
        });

        //инициализация edittext и листенер на ключи при взаимодействии с ним, когда мы нажимаем enter у нас опускается клавиатура и запускается WeatherFragment
        editTextCountry = (EditText) view.findViewById(R.id.et_country);

        editTextCountry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (editTextCountry.getText().toString().length() > 20 ||
                            containsError(editTextCountry.getText().toString())){
                        showError();
                    } else {
                        hideError();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        String country = editTextCountry.getText().toString().trim();
                        getBaseActivity().startWeatherFragment(country);
                        saveInPref(country);
//                        ArrayList<String> arrayList = new ArrayList<>();
//                        arrayList.add(country);
//                        cityList.add(country);
//                        mCallback.onArticleSelected(arrayList);
                        return true;
                    }
                }
                return false;
            }
        });

        textInputLayout = view.findViewById(R.id.til);
    }
    private void deleteFromPref(String description) {
        prefHelper.deleteSharedPreferences(Constants.CITY, description);
    }

    private void saveInPref(String s) {
        prefHelper.saveSharedPreferences(Constants.CITY, s);
    }

    private boolean containsError(String text){
        String[] simbols = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "?", ".", ",", "&", "%","#"};
        for (int i = 0; i < simbols.length; i++) {
            if (text.contains(simbols[i])){
                return true;
            }
        }
        return false;
    }

    private void showError(){
        textInputLayout.setError("Error");
    }
    private void hideError(){
        textInputLayout.setError("");
    }

    private void initCountryList() {
        cityList = new ArrayList<>();

        cityList.add("Moscow");
        cityList.add("Saint Petersburg");
        cityList.add("Yekaterinburg");
    }
}
