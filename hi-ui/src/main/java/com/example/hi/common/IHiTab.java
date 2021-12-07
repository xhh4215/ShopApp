package com.example.hi.common;

import androidx.annotation.Px;

import org.jetbrains.annotations.NotNull;

/***
 * HiTab对外的接口
 */
public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D> {
    /***
     * 为tab设置数据
     * @param data
     */
    void setHiTabInfo(@NotNull D data);

    /***
     * 设置tab的高度
     * @param height
     */
    void resetHeight(@Px int height);
}
