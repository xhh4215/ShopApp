package com.example.shopapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hi.refresh.HiRefreshLayout;
import com.example.hi.refresh.HiTextOverView;
import com.example.hi.refresh.IHiRefresh;

public class TestRefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);
        HiRefreshLayout refreshLayout = findViewById(R.id.refresh_layout);
        HiTextOverView textOverView = new HiTextOverView(this);
        refreshLayout.setRefreshOverView(textOverView);
        refreshLayout.setRefreshListener(new IHiRefresh.HiRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> refreshLayout.refreshFinished(), 2000);
            }

            @Override
            public boolean enableRefresh() {
                return true;
            }
        });

    }



}