package com.example.shopapp;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.ui.component.HiBaseActivity;
import com.example.library.restful.HiCallBack;
import com.example.library.restful.HiResponse;
import com.example.shopapp.http.ApiFactory;
import com.example.shopapp.http.TestApi;
import com.example.shopapp.logic.MainActivityLogic;

import org.json.JSONObject;

public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic logic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logic = new MainActivityLogic(this, savedInstanceState);
        ApiFactory.INSTANCE.create(TestApi.class).listCities("immoc").enqueue(new HiCallBack<JSONObject>() {
            @Override
            public void onSuccess(@NonNull HiResponse<JSONObject> response) {

            }

            @Override
            public void onFiled(@NonNull Throwable throwable) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logic.onSaveInstanceState(outState);
    }
}
