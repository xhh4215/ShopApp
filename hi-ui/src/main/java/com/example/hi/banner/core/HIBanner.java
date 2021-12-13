package com.example.hi.banner.core;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.hi.R;
import com.example.hi.banner.core.HIBannerDelegate;
import com.example.hi.banner.core.HiBannerMo;
import com.example.hi.banner.core.IBindAdapter;
import com.example.hi.banner.core.IHiBanner;
import com.example.hi.banner.indicator.HiIndicator;

import java.util.List;

/****
 * 核心问题
 *  1 如何实现UI的高度定制？
 *  2 作为有限的item如何实现无限轮播
 *  3 banner需要显示网络图片 ，如何和网络库姐耦合
 *  4 如何定制指示器样式
 *  5 如何设置ViewPager的滚动速度
 */
public class HIBanner extends FrameLayout implements IHiBanner {
    //代理HIBanner实现业务逻辑的代理类
    private HIBannerDelegate delegate;

    public HIBanner(@NonNull Context context) {
        this(context, null);
    }

    public HIBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HIBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new HIBannerDelegate(context, this);
        initCustomAttrs(context, attrs);
    }

    /***
     * 初始化xml属性
     * @param context
     * @param attributeSet
     */
    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.HIBanner);
        boolean autoPlay = typedArray.getBoolean(R.styleable.HIBanner_autoPlay, true);
        boolean loop = typedArray.getBoolean(R.styleable.HIBanner_loop, true);
        int intervalTime = typedArray.getInteger(R.styleable.HIBanner_intervalTime, -1);
        setAutoPlay(autoPlay);
        setLoop(loop);
        setIntervalTime(intervalTime);
        typedArray.recycle();
    }

    /***
     * 设置Banner绑定的数据
     * @param layoutId 当前Banner使用的布局
     * @param models Banner使用的数据
     */
    @Override
    public void setBannerData(int layoutId, @NonNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(layoutId, models);

    }

    /***
     * 设置Banner绑定的数据
     * @param models Banner使用的数据 布局使用的是默认的布局
     *
     */
    @Override
    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        delegate.setBannerData(R.layout.hi_banner_item_image, models);
    }

    /***
     * 设置指示器
     * @param indicator
     */
    @Override
    public void setHiIndicator(HiIndicator<?> indicator) {
        delegate.setHiIndicator(indicator);

    }

    /***
     * 设置是否自动切换
     * @param autoPlay
     */
    @Override
    public void setAutoPlay(boolean autoPlay) {
        delegate.setAutoPlay(autoPlay);

    }

    /***
     * 设置是否循环切换
     * @param loop
     */
    @Override
    public void setLoop(boolean loop) {
        delegate.setLoop(loop);

    }

    /***
     * 设置ViewPager切换的时间间隔
     * @param intervalTime
     */
    @Override
    public void setIntervalTime(int intervalTime) {
        delegate.setIntervalTime(intervalTime);

    }

    /***
     * 设置当前的Banner绑定使用的Adapter
     * @param adapter
     */
    @Override
    public void setBindAdapter(IBindAdapter adapter) {
        delegate.setBindAdapter(adapter);

    }

    /***
     * 设置ViewPager的页面切换的监听
     * @param listener
     */
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        delegate.setOnPageChangeListener(listener);

    }

    /***
     * 设置ViewPager的Banner的点击事件
     * @param listener
     */
    @Override
    public void setOnBannerClickListener(OnBannerClickListener listener) {
        delegate.setOnBannerClickListener(listener);

    }

    @Override
    public void setScrollerDuration(int duration) {
        delegate.setScrollerDuration(duration);
    }
}
