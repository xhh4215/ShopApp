package com.example.hi.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi.R;
import com.example.hi.common.IHiTabLayout;
import com.example.library.utils.HiDisplayUtil;
import com.example.library.utils.HiViewUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedListenerList = new ArrayList<>();
    private HiTabBottomInfo<?> selectedInfo;
    private float alpha = 1f;
    private float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#FF000000";
    private List<HiTabBottomInfo<?>> infoList;

    public HiTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> info) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof HiTabBottom) {
                HiTabBottom tab = (HiTabBottom) child;
                if (tab.getTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
        tabSelectedListenerList.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setTabBottomHeight(float tabBottomHeight) {
        this.tabBottomHeight = tabBottomHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Override
    public void inflatedInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        for (int i = getChildCount() - 1; i > 0; i++) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedListenerList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottom) {
                iterator.remove();
            }
        }
        int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(TAG_TAB_BOTTOM);
        int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        for (
                int i = 0; i < infoList.size(); i++) {
            HiTabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            HiTabBottom tabBottom = new HiTabBottom(getContext());
            tabSelectedListenerList.add(tabBottom);
            tabBottom.setHiTabInfo(info);
            ll.addView(tabBottom, params);
            tabBottom.setOnClickListener(v -> onSelected(info));
        }

        LayoutParams fLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        fLayoutParam.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, fLayoutParam);
        fixContentView();

    }

    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = HiViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = HiViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }

    private void addBottomLine() {
        View bottomLineView = new View(getContext());
        bottomLineView.setBackgroundColor(Color.parseColor(bottomLineColor));
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(bottomLineHeight, getResources()));
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLineView, layoutParams);
        bottomLineView.setAlpha(alpha);
    }

    private void onSelected(@NotNull HiTabBottomInfo<?> nextTabInfo) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> listener : tabSelectedListenerList) {
            listener.onTabSelectedChange(infoList.indexOf(nextTabInfo), selectedInfo, nextTabInfo);
        }
        this.selectedInfo = nextTabInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(alpha);
    }
}
