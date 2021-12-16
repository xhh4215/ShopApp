package com.example.shopapp;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.ui.component.HiBaseActivity;
import com.example.shopapp.logic.MainActivityLogic;

public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic logic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         logic = new MainActivityLogic(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logic.onSaveInstanceState(outState);
    }
}
