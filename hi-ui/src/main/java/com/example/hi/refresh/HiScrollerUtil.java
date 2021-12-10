package com.example.hi.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class HiScrollerUtil {
    /***
     * 判断child是否发生滚动
     * @return true 发生滚动
     */
    public static boolean childScrolled(@NotNull View child) {
        if (child instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) child;
            if (adapterView.getFirstVisiblePosition() != 0 ||
                    adapterView.getFirstVisiblePosition() == 0 && adapterView.getChildAt(0) != null
                            && adapterView.getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (child.getScrollY() > 0) {
            return true;
        }
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            View view = recyclerView.getChildAt(0);
            int firstPosition = recyclerView.getChildAdapterPosition(view);
            return firstPosition != 0 || view.getTop() != 0;
        }
        return false;
    }

    /***
     * 查找可滚动的child
     * @param viewGroup
     * @return 可以滚动的child
     */
    public static View findScrollableChild(@NotNull ViewGroup viewGroup) {
        View child = viewGroup.getChildAt(1);
        if (child instanceof RecyclerView || child instanceof AdapterView) {
            return child;
        }
        if (child instanceof ViewGroup) {
            View tempChild = ((ViewGroup) child).getChildAt(0);
            if (tempChild instanceof RecyclerView || tempChild instanceof AdapterView) {
                child = tempChild;
            }
        }
        return child;
    }
}
