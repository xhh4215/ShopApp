package com.example.hi.banner.indicator;

import android.view.View;

/***
 * 实现这个接口定义指定样式的指示器
 * @param <T>
 */
public interface HiIndicator<T extends View> {

    T get();

    /***
     * 初始化 indicator
     * @param count  幻灯片的数量
     */
    void onInflate(int count);

    /***
     * 幻灯片切换回调
     * @param current 切换到幻灯片的位置
     * @param count 幻灯片的数量
     */
    void onPointChange(int current, int count);
}
