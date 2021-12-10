package com.example.hi.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.hi.common.IHiTabLayout;
import com.example.library.utils.HiDisplayUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabTopLayout extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {
    private List<OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedListenerLists = new ArrayList<>();
    private HiTabTopInfo<?> selectedInfo;
    private List<HiTabTopInfo<?>> infoList;

    public HiTabTopLayout(Context context) {
        this(context, null);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof HiTabTop) {
                HiTabTop tab = (HiTabTop) child;
                if (tab.getTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedListenerLists.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflatedInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        LinearLayout rootView = getRootLayout(true);
        selectedInfo = null;
        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterator = tabSelectedListenerLists.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabTop) {
                iterator.remove();
            }
        }

        for (int i = 0; i < infoList.size(); i++) {
            HiTabTopInfo<?> info = infoList.get(i);
            HiTabTop tab = new HiTabTop(getContext());
            tabSelectedListenerLists.add(tab);
            tab.setHiTabInfo(info);
            rootView.addView(tab);
            tab.setOnClickListener(v -> onSelected(info));

        }

    }

    private void onSelected(@NotNull HiTabTopInfo<?> nextTabInfo) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> listener : tabSelectedListenerLists) {
            listener.onTabSelectedChange(infoList.indexOf(nextTabInfo), selectedInfo, nextTabInfo);
        }
        this.selectedInfo = nextTabInfo;
        autoScroll(nextTabInfo);
    }

    int tabWidth;

    private void autoScroll(HiTabTopInfo<?> nextTabInfo) {
        HiTabTop tabTop = findTab(nextTabInfo);
        if (tabTop == null) {
            return;
        }
        int index = infoList.indexOf(tabTop);
        int[] loc = new int[2];
        tabTop.getLocationInWindow(loc);
        int scrollWidth;
        if (tabWidth == 0) {
            tabWidth = tabTop.getWidth();
        }
        if ((loc[0] + tabWidth / 2) > HiDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);

    }

    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false);
                } else {
                    scrollWidth += scrollWidth(next, true);

                }
            }
        }
        return scrollWidth;
    }

    /***
     * 指定位置控件可以滚动的距离
     * @param index
     * @param toRight
     * @return
     */
    private int scrollWidth(int index, boolean toRight) {
        HiTabTop tabTop = findTab(infoList.get(index));
        Rect rect = new Rect();
        tabTop.getLocalVisibleRect(rect);
        if (toRight) {//点击屏幕右侧
            if (rect.right > tabWidth) { //控件没有完全的显示
                return tabWidth;
            } else {
                return tabWidth - rect.right;
            }
        } else {
            if (rect.left <= -tabWidth) { //控件没有完全的显示
                return tabWidth;
            } else if (rect.left > 0) {//部分显示
                return rect.left;
            }
        }
        return 0;

    }


    private LinearLayout getRootLayout(boolean isClear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(rootView, layoutParams);

        } else if (isClear) {
            removeAllViews();
        }
        return rootView;
    }
}
