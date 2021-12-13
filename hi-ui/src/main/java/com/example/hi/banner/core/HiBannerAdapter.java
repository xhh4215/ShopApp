package com.example.hi.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/***
 *
 */
public class HiBannerAdapter extends PagerAdapter {
    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCacheViews = new SparseArray<>();

    private IHiBanner.OnBannerClickListener onBannerClickListener;

    private IBindAdapter adapter;
    private List<? extends HiBannerMo> models;
    /***
     * 是否开启自动轮播
     */
    private boolean mAutoPlay = true;

    /***
     * 非自动轮播情况是是否循环切换
     */
    private boolean mLoop = true;


    private int mLayoutResId = -1;

    public HiBannerAdapter(Context context) {
        this.mContext = context;
    }

    public void setBannerData(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        //初始化数据
        initCacheView();
        notifyDataSetChanged();
    }

    public void setOnBannerClickListener(IHiBanner.OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public void setBindAdapter(IBindAdapter adapter) {
        this.adapter = adapter;
    }

    public void setLayoutResId(@LayoutRes int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }

    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
    }

    public void setLoop(boolean mLoop) {
        this.mLoop = mLoop;
    }

    @Override
    public int getCount() {
        //无限轮播的关键点
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    /***
     * 获取初次显示的item的位置
     * @return
     */
    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    /***
     * 获取真是的页面的数量
     * @return
     */
    public int getRealCount() {
        return models == null ? 0 : models.size();
    }


    private void initCacheView() {
        mCacheViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerAdapter.HiBannerViewHolder(createRootView(LayoutInflater.from(mContext), null));
            mCacheViews.put(i, viewHolder);
        }

    }

    private View createRootView(LayoutInflater layoutInflater, ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set layoutid first");
        }

        return layoutInflater.inflate(mLayoutResId, parent, false);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }
        HiBannerViewHolder viewHolder = mCacheViews.get(realPosition);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        //数据绑定
        onBind(viewHolder, models.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都刷新
        return POSITION_NONE;
    }

    protected void onBind(@NonNull final HiBannerAdapter.HiBannerViewHolder viewHolder, @NonNull final HiBannerMo mo, final int position) {
        viewHolder.rootView.setOnClickListener(v -> {
            if (onBannerClickListener != null) {
                onBannerClickListener.onBannerClick(viewHolder, mo, position);
            }
        });

        if (adapter != null) {
            adapter.onBind(viewHolder, mo, position);
        }
    }


    public static class HiBannerViewHolder {
        private SparseArray<View> viewSparseArray;
        View rootView;

        public View getRootView() {
            return rootView;
        }

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }

            if (this.viewSparseArray == null) {
                viewSparseArray = new SparseArray<>(1);
            }
            V childView = (V) viewSparseArray.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                this.viewSparseArray.put(id, childView);
            }

            return childView;
        }
    }
}
