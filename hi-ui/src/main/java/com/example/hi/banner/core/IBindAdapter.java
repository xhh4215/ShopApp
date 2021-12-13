package com.example.hi.banner.core;

/***
 * HiBanner的数据绑定接口，基于该接口实现数据绑定和框架层解耦合
 */
public interface IBindAdapter {
    /***
     * 数据的绑定
     * @param viewHolder
     * @param mo
     * @param position
     */
    void onBind(HiBannerAdapter.HiBannerViewHolder viewHolder, HiBannerMo mo, int position);
}
