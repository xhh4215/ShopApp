package com.example.shopapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.ui.component.HiBaseActivity;
import com.example.library.restful.HiCallBack;
import com.example.library.restful.HiResponse;
import com.example.shopapp.biz.LoginActivity;
import com.example.shopapp.http.ApiFactory;
import com.example.shopapp.http.AccountApi;
import com.example.shopapp.logic.MainActivityLogic;

import org.json.JSONObject;

public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic logic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logic = new MainActivityLogic(this, savedInstanceState);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logic.onSaveInstanceState(outState);
    }
}
