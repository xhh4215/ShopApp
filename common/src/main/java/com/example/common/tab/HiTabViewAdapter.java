package com.example.common.tab;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hi.bottom.HiTabBottomInfo;

import java.util.List;

public class HiTabViewAdapter {
    private List<HiTabBottomInfo<?>> mInfoList;

    private Fragment currentFragment;

    private FragmentManager mFragmentManager;

    public HiTabViewAdapter(List<HiTabBottomInfo<?>> infoList, FragmentManager fragmentManager) {
        mInfoList = infoList;
        mFragmentManager = fragmentManager;
    }

    /***
     *  实例化和显示指定位置的fragment
     * @param container
     * @param position
     */
    public void instantiateItem(View container, int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        String name = container.getId() + ":" + position;
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = getItem(position);
            if (!fragment.isAdded()) {
                transaction.add(container.getId(), fragment, name);
            }
        }
        currentFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public Fragment getItem(int position) {
        try {
            return mInfoList.get(position).fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }
}
