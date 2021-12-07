package com.example.hi.bottom;

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
import com.example.hi.common.IHiTab;

import org.w3c.dom.Text;

public class HiTabBottom extends RelativeLayout implements IHiTab<HiTabBottomInfo<?>> {
    //当前tab的使用的数据
    private HiTabBottomInfo<?> tabInfo;
    //tab的bitmap图标
    private ImageView tabImageView;
    //tab的IconFont图标
    private TextView tabIconView;
    //当前tab的name
    private TextView tabNameView;

    public HiTabBottom(Context context) {
        this(context, null);
    }

    public HiTabBottom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HiTabBottomInfo<?> getTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabIconView() {
        return tabIconView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    private void init() {
        //加载当前的View使用的布局，和初始化内部的view
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this);
        tabIconView = findViewById(R.id.tv_icon);
        tabNameView = findViewById(R.id.tv_name);
        tabImageView = findViewById(R.id.iv_image);
    }


    @Override
    public void setHiTabInfo(@NonNull HiTabBottomInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    /***
     * 初始化tab使用的数据
     * @param isSelected
     * @param isInit
     */
    private void inflateInfo(boolean isSelected, boolean isInit) {
        if (tabInfo.tabType == HiTabBottomInfo.TabType.ICON) {
            if (isInit) {
                tabImageView.setVisibility(GONE);
                tabIconView.setVisibility(VISIBLE);
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
                tabIconView.setTypeface(typeface);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
            }
            if (isSelected) {
                tabIconView.setText(TextUtils.isEmpty(tabInfo.selectIconName) ? tabInfo.defaultIconName : tabInfo.selectIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.tintColor));
                tabNameView.setTextColor(getTextColor(tabInfo.tintColor));
            } else {
                tabIconView.setText(tabInfo.defaultIconName);
                tabIconView.setTextColor(getTextColor(tabInfo.defaultColor));
                tabNameView.setTextColor(getTextColor(tabInfo.defaultColor));
            }
        } else if (tabInfo.tabType == HiTabBottomInfo.TabType.BITMAP) {
            if (isInit) {
                tabImageView.setVisibility(VISIBLE);
                tabIconView.setVisibility(GONE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.setText(tabInfo.name);
                }
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
    public void onTabSelectedChange(int index, @NonNull HiTabBottomInfo<?> proInfo, @NonNull HiTabBottomInfo<?> nextInfo) {
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
