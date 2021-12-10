package com.example.hi.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.example.hi.R;
import com.example.hi.bottom.HiTabBottomInfo;
import com.example.hi.common.IHiTab;

public class HiTabTop extends RelativeLayout implements IHiTab<HiTabTopInfo<?>> {
    //当前tab的使用的数据
    private HiTabTopInfo<?> tabInfo;
    //tab的bitmap图标
    private ImageView tabImageView;
    //当前tab的name
    private TextView tabNameView;

    private View indicator;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HiTabTopInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    private void init() {
        //加载当前的View使用的布局，和初始化内部的view
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        tabNameView = findViewById(R.id.tv_name);
        tabImageView = findViewById(R.id.iv_image);
        indicator = findViewById(R.id.tab_top_indicator);
    }


    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    /***
     * 初始化tab使用的数据
     * @param isSelected
     * @param isInit
     */
    private void inflateInfo(boolean isSelected, boolean isInit) {
        if (tabInfo.tabType == HiTabTopInfo.TabType.TEXT) {
            if (isInit) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (isSelected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabTopInfo.TabType.BITMAP) {
            if (isInit) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
            }
            if (isSelected) {
                tabImageView.setImageBitmap(tabInfo.selectBitmap);
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap);
            }
        }
    }

    /***
     * 获取文本的颜色
     * @param color
     * @return
     */
    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }

    /***
     * 设置tab的高度
     * @param height
     */
    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @NonNull HiTabTopInfo<?> proInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (tabInfo != proInfo && tabInfo != nextInfo || nextInfo == proInfo) {
            return;
        }
        if (proInfo == tabInfo) {
            inflateInfo(false, false);
        } else {
            inflateInfo(true, false);

        }
    }
}
