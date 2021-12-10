package com.example.hi.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.library.utils.HiDisplayUtil;

/***
 * @author 栾桂明
 * @desc 扩展下拉刷新的头部的抽象类
 */
public abstract class HiOverView extends FrameLayout {

    public enum HiRefreshState {
        /***
         * 初始状态
         */
        STATE_INIT,
        /***
         * head展示状态
         */
        STATE_VISIBLE,
        /***
         *  刷新中
         */
        STATE_REFRESH,
        /***
         * 超出可刷新距离
         */
        STATE_OVER,
        /***
         * 超出刷新位置松开手的状态
         */
        STATE_OVER_RELEASE
    }

    /**
     * 触发下拉刷新 需要的最小高度
     */    public int mPullRefreshHeight;
    /***
     * 最小阻尼
     */
    public float minDamp = 1.6f;
    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;

    // 当前状态
    protected HiRefreshState mState = HiRefreshState.STATE_INIT;

    public HiOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    protected void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(88f, getResources());
        init();
    }

    public abstract void init();

    /***
     * 头部滚动的时候的触发方法
     * @param scrollY
     * @param pullRefreshHeight
     */
    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /****
     * 显示出OverView触发的方法
     */
    protected abstract void onVisible();

    /***
     * 超过OverView，释放就会加载
     */
    public abstract void onOver();

    /***
     * 开始刷新
     */
    public abstract void onRefresh();

    /***
     * 刷新完成
     */
    public abstract void onFinish();

    /***
     * 设置状态
     * @param state
     */
    public void setState(HiRefreshState state) {
        this.mState = state;
    }

    /***
     * 获取状态
     * @return
     */
    public HiRefreshState getState() {
        return mState;
    }

}
