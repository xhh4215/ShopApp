package com.example.shopapp.demo;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hi.banner.core.HIBanner;
import com.example.hi.banner.core.HiBannerMo;
import com.example.hi.banner.indicator.HiCircleIndicator;
import com.example.shopapp.R;

import java.util.ArrayList;

public class TestRefreshActivity extends AppCompatActivity {

    private HIBanner banner;

    private ArrayList<HiBannerMo> listMo;

    private String[] urls = new String[]{
            "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);
        initView(new HiCircleIndicator(this));

    }

    private void initView(HiCircleIndicator hiCircleIndicator) {
        banner = findViewById(R.id.banner);
        listMo = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BannerMo bannerMo = new BannerMo();
            bannerMo.url = urls[i % urls.length];
            listMo.add(bannerMo);
        }
        banner.setHiIndicator(hiCircleIndicator);
        banner.setAutoPlay(true);
        banner.setIntervalTime(2000);
        banner.setBannerData(R.layout.banner_item_layout,listMo);
        banner.setBindAdapter((viewHolder, mo, position) -> {
            ImageView imageView = viewHolder.findViewById(R.id.iv_image);
            Glide.with(this).load(mo.url).into(imageView);
        });


    }


}