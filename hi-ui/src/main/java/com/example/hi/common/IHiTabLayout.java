package com.example.hi.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/***
 * @author 栾桂明
 * @date 2021年12月3日
 * @param <Tab>
 * @param <D>
 */
public interface IHiTabLayout<Tab extends ViewGroup, D> {
    /***
     * 查找指定的数据的tab
     * @param data
     * @return
     */
    Tab findTab(@NotNull D data);

    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    /***
     * 默认选中的tab
     * @param defaultInfo
     */
    void defaultSelected(@NotNull D defaultInfo);

    /***
     *  加载要在布局中使用的所有的tab的信息
     * @param infoList
     */
    void inflatedInfo(@NotNull List<D> infoList);

    /***
     * 定义了一个tab切换的时候的监听事件
     * @param <D>
     */
    interface OnTabSelectedListener<D> {
        void onTabSelectedChange(int index, @Nullable D proInfo, @NonNull D nextInfo);
    }
}
