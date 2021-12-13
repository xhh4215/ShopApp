package com.example.hi.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.hi.banner.indicator.HiIndicator;

import java.util.List;

/***
 * @author 栾桂明
 * @desc 遵循面向接口编程的思想 定义banner组件的的行为
 */
public interface IHiBanner {
    /***
     * 设置banner绑定的数据
     * @param layoutId 当前的banner的布局
     * @param models 当前的banner的数据集合
     */
    void setBannerData(@LayoutRes int layoutId, @NonNull List<? extends HiBannerMo> models);

    /****
     * 设置banner使用默认的布局绑定数据
     * @param models 当前的banner的数据集合
     */
    void setBannerData(@NonNull List<? extends HiBannerMo> models);

    /****
     * 设置指示器
     * @param indicator
     */
    void setHiIndicator(HiIndicator<?> indicator);

    /***
     * 设置是否自动播放
     * @param autoPlay
     */
    void setAutoPlay(boolean autoPlay);

    /***
     * 设置循环播放
     * @param loop
     */
    void setLoop(boolean loop);

    /***
     * 设置切换的间隔时间
     * @param intervalTime
     */
    void setIntervalTime(int intervalTime);

    /***
     * 设置数据绑定的时候要使用的adapter
     * @param adapter
     */
    void setBindAdapter(IBindAdapter adapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

    void setOnBannerClickListener(OnBannerClickListener listener);

    void setScrollerDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull HiBannerAdapter.HiBannerViewHolder viewHolder, @NonNull HiBannerMo hiBannerMo, int position);

    }

}
