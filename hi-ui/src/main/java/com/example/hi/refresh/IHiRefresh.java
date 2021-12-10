package com.example.hi.refresh;

/***
 * @author 栾桂明
 * @desc 下拉刷新组件的通用接口
 *
 */
public interface IHiRefresh {
    /***
     * 刷新的时候是否禁止滚动
     * @param disableRefreshScroll 是否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /***
     * 刷新完成
     */
    void refreshFinished();

    /***
     * 设置下拉刷新监听
     * @param hiRefreshListener 刷新的监听器
     */
    void setRefreshListener(HiRefreshListener hiRefreshListener);

    /***
     * 设置下拉刷新的视图
     * @param hiOverView
     */
    void setRefreshOverView(HiOverView hiOverView);

    interface HiRefreshListener {
        void onRefresh();

        boolean enableRefresh();
    }


}

