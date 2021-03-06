package com.example.biz_home;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.example.biz_home.category.CategoryFragment;
import com.example.biz_home.favorite.FavoriteFragment;
import com.example.biz_home.home.HomePageFragment;
import com.example.biz_home.profile.ProfileFragment;
import com.example.biz_home.recommend.RecommendFragment;
import com.example.common.tab.HiFragmentTabView;
import com.example.common.tab.HiTabViewAdapter;
import com.example.hi.bottom.HiTabBottomInfo;
import com.example.hi.bottom.HiTabBottomLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {
    private HiFragmentTabView fragmentTabView;
    private HiTabBottomLayout tabBottomLayout;
    private List<HiTabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;

    private final static String SAVE_CURRENT_ID = "SAVE_CURRENT_ID";
    private int currentItemPosition;


    public HiFragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public HiTabBottomLayout getTabBottomLayout() {
        return tabBottomLayout;
    }

    public List<HiTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public MainActivityLogic(ActivityProvider activityProvider, Bundle savedInstanceState) {
        this.activityProvider = activityProvider;
        if (savedInstanceState != null) {
            currentItemPosition = savedInstanceState.getInt(SAVE_CURRENT_ID);
        }
        initTabBottom();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVE_CURRENT_ID, currentItemPosition);
    }

    private void initTabBottom() {
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        tabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        tabBottomLayout.setAlpha(1f);
        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(R.color.tab_bottom_default_color);
        int tintColor = activityProvider.getResources().getColor(R.color.tab_bottom_tint_color);
        HiTabBottomInfo homeInfo = new HiTabBottomInfo(
                "??????",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null, defaultColor, tintColor
        );
        homeInfo.fragment = HomePageFragment.class;
        HiTabBottomInfo favoriteInfo = new HiTabBottomInfo(
                "??????",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null, defaultColor, tintColor
        );
        favoriteInfo.fragment = FavoriteFragment.class;
        HiTabBottomInfo categoryInfo = new HiTabBottomInfo(
                "??????",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null, defaultColor, tintColor
        );
        categoryInfo.fragment = CategoryFragment.class;
        HiTabBottomInfo recommendInfo = new HiTabBottomInfo(
                "??????",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null, defaultColor, tintColor
        );
        recommendInfo.fragment = RecommendFragment.class;

        HiTabBottomInfo profileInfo = new HiTabBottomInfo(
                "??????",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null, defaultColor, tintColor
        );
        profileInfo.fragment = ProfileFragment.class;
        infoList.add(homeInfo);
        infoList.add(categoryInfo);
        infoList.add(favoriteInfo);
        infoList.add(recommendInfo);
        infoList.add(profileInfo);
        tabBottomLayout.inflatedInfo(infoList);
        initFragmentTabView();
        tabBottomLayout.addTabSelectedChangeListener((index, proInfo, nextInfo) -> {
            fragmentTabView.setCurrentItem(index);
            MainActivityLogic.this.currentItemPosition = index;
        });
        tabBottomLayout.defaultSelected(infoList.get(currentItemPosition));

    }

    private void initFragmentTabView() {
        HiTabViewAdapter adapter = new HiTabViewAdapter(infoList, activityProvider.getSupportFragmentManager());
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(adapter);

    }


    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);

    }
}
