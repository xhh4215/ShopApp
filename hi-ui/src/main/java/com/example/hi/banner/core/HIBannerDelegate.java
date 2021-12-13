package com.example.hi.banner.core;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.hi.R;
import com.example.hi.banner.indicator.HiCircleIndicator;
import com.example.hi.banner.indicator.HiIndicator;

import java.util.List;

/***
 * HiBanner的控制器
 * 辅助HiBanner完成各种功能
 * 将HiBanner的功能内聚到这里  保证 HiBanner的干净整洁
 */
public class HIBannerDelegate implements IHiBanner, ViewPager.OnPageChangeListener {
    private Context mContext;
    private HIBanner mBanner;
    private HiBannerAdapter mAdapter;
    private HiIndicator mIndicator;
    private boolean isAutoPlay;
    private boolean isLoop;
    private List<? extends HiBannerMo> mBannerMos;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mIntervalTime = 5000;
    private IHiBanner.OnBannerClickListener mBannerClickListener;
    private HiViewPager mViewPager;
    private int mScrollerDuration = -1;

    public HIBannerDelegate(Context context, HIBanner hiBanner) {
        mContext = context;
        mBanner = hiBanner;
    }

    @Override
    public void setBannerData(int layoutId, @NonNull List<? extends HiBannerMo> models) {
        mBannerMos = models;
        init(layoutId);
    }


    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        setBannerData(R.layout.hi_banner_item_image, models);
    }

    @Override
    public void setHiIndicator(HiIndicator<?> indicator) {
        this.mIndicator = indicator;
    }

    @Override
    public void setAutoPlay(boolean autoPlay) {
        this.isAutoPlay = autoPlay;
        if (mAdapter != null) {
            mAdapter.setAutoPlay(isAutoPlay);
        }
        if (mViewPager != null) {
            mViewPager.setAutoPlay(isAutoPlay);
        }

    }

    @Override
    public void setLoop(boolean loop) {
        this.isLoop = loop;

    }

    @Override
    public void setIntervalTime(int intervalTime) {
        if (intervalTime > 0) {
            this.mIntervalTime = intervalTime;
        }
    }

    @Override
    public void setBindAdapter(IBindAdapter adapter) {
        mAdapter.setBindAdapter(adapter);

    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mPageChangeListener = listener;
    }

    @Override
    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.mBannerClickListener = listener;
    }

    @Override
    public void setScrollerDuration(int duration) {
        mScrollerDuration = duration;
        if (mViewPager != null && duration > 0) {
            mViewPager.setScrollerDuration(duration);
        }
    }

    private void init(int layoutId) {
        if (mAdapter == null) {
            mAdapter = new HiBannerAdapter(mContext);
        }
        if (mIndicator == null) {
            mIndicator = new HiCircleIndicator(mContext);
        }

        mIndicator.onInflate(mBannerMos.size());
        mAdapter.setLayoutResId(layoutId);
        mAdapter.setBannerData(mBannerMos);
        mAdapter.setAutoPlay(isAutoPlay);
        mAdapter.setLoop(isLoop);
        mAdapter.setOnBannerClickListener(mBannerClickListener);
        mViewPager = new HiViewPager(mContext);
        mViewPager.setIntervalTime(mIntervalTime);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAutoPlay(isAutoPlay);
        mViewPager.setAdapter(mAdapter);
        if (mScrollerDuration>0){
            mViewPager.setScrollerDuration(mScrollerDuration);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        if ((isAutoPlay | isLoop) && mAdapter.getRealCount() != 0) {
            //无限轮播的关键点，使第一张反向u滑动到最后一张
            int firstItem = mAdapter.getFirstItem();
            mViewPager.setCurrentItem(firstItem, false);
        }
        //清除所有的View
        mBanner.removeAllViews();
        mBanner.addView(mViewPager, layoutParams);
        mBanner.addView(mIndicator.get(), layoutParams);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mPageChangeListener && mAdapter.getRealCount() != 0) {
            mPageChangeListener.onPageScrolled(position % mAdapter.getRealCount(), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter.getRealCount() == 0) {
            return;
        }
        position = position % mAdapter.getRealCount();
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageSelected(position);
        }
        if (mIndicator != null) {
            mIndicator.onPointChange(position, mAdapter.getRealCount());
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageScrollStateChanged(state);
        }

    }
}
