package com.example.shopapp.logic;

import android.content.res.Resources;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.example.common.tab.HiFragmentTabView;
import com.example.common.tab.HiTabViewAdapter;
import com.example.hi.bottom.HiTabBottomInfo;
import com.example.hi.bottom.HiTabBottomLayout;
import com.example.hi.common.IHiTabLayout;
import com.example.shopapp.R;
import com.example.shopapp.fragment.CategoryFragment;
import com.example.shopapp.fragment.FavoriteFragment;
import com.example.shopapp.fragment.HomePageFragment;
import com.example.shopapp.fragment.ProfileFragment;
import com.example.shopapp.fragment.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {
    private HiFragmentTabView fragmentTabView;
    private HiTabBottomLayout tabBottomLayout;
    private List<HiTabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;

    public HiFragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public HiTabBottomLayout getTabBottomLayout() {
        return tabBottomLayout;
    }

    public List<HiTabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    public MainActivityLogic(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
        initTabBottom();
    }

    private final static String SAVE_CURRENT_ID = "SAVE_CURRENT_ID";
    private int currentItemPosition;

    private void initTabBottom() {
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        tabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        tabBottomLayout.setAlpha(0.85f);
        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(R.color.tab_bottom_default_color);
        int tintColor = activityProvider.getResources().getColor(R.color.tab_bottom_tint_color);
        HiTabBottomInfo homeInfo = new HiTabBottomInfo(
                "首页",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home),
                null, defaultColor, tintColor
        );
        homeInfo.fragment = HomePageFragment.class;
        HiTabBottomInfo favoriteInfo = new HiTabBottomInfo(
                "收藏",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_favorite),
                null, defaultColor, tintColor
        );
        favoriteInfo.fragment = FavoriteFragment.class;
        HiTabBottomInfo categoryInfo = new HiTabBottomInfo(
                "分类",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_category),
                null, defaultColor, tintColor
        );
        categoryInfo.fragment = CategoryFragment.class;
        HiTabBottomInfo recommendInfo = new HiTabBottomInfo(
                "推荐",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_recommend),
                null, defaultColor, tintColor
        );
        recommendInfo.fragment = RecommendFragment.class;

        HiTabBottomInfo profileInfo = new HiTabBottomInfo(
                "我的",
                "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_profile),
                null, defaultColor, tintColor
        );
        profileInfo.fragment = ProfileFragment.class;
        infoList.add(homeInfo);
        infoList.add(categoryInfo);
        infoList.add(recommendInfo);
        infoList.add(profileInfo);
        infoList.add(favoriteInfo);
        tabBottomLayout.inflatedInfo(infoList);
        initFragmentTabView();
        tabBottomLayout.addTabSelectedChangeListener((index, proInfo, nextInfo) -> {
            fragmentTabView.setCurrentItem(index);
        });
        tabBottomLayout.defaultSelected(homeInfo);

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
