package com.example.library.log.printer;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.library.utils.HiDisplayUtil;

public class HiViewPrinterProvider {
    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";
    private FrameLayout rootView;
    private View floatingView;
    private FrameLayout logView;
    private boolean isOpen;
    private RecyclerView recyclerView;

    public HiViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        this.rootView = rootView;
        this.recyclerView = recyclerView;
    }

    public void showFloatingView() {
        if (rootView.findViewWithTag(TAG_FLOATING_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        View floatingView = genFloatView();
        floatingView.setTag(TAG_FLOATING_VIEW);
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        layoutParams.bottomMargin = HiDisplayUtil.dp2px(80f, rootView.getResources());
        rootView.addView(floatingView, layoutParams);
    }

    public void closeFloatingView() {
        rootView.removeView(genFloatView());
    }

    private View genFloatView() {
        if (floatingView != null) {
            return floatingView;
        }
        TextView textView = new TextView(recyclerView.getContext());
        textView.setOnClickListener(v -> {
            if (!isOpen) {
                showLogView();
            }
        });
        textView.setText("HiLog");
        textView.setTextColor(Color.GRAY);
        return floatingView = textView;
    }

    private void showLogView() {
        if (rootView.findViewWithTag(TAG_LOG_VIEW) != null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                HiDisplayUtil.dp2px(240f, rootView.getResources())
        );
        layoutParams.gravity = Gravity.BOTTOM;
        View logView = getLogView();
        logView.setTag(TAG_LOG_VIEW);
        rootView.addView(logView, layoutParams);
        isOpen = true;
    }

    private View getLogView() {
        if (logView != null) {
            return logView;
        }
        FrameLayout logView = new FrameLayout(rootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(recyclerView);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
        TextView closeView = new TextView(rootView.getContext());
        closeView.setOnClickListener(v -> closeLogView());
        closeView.setText("close");
        logView.addView(closeView, layoutParams);
        return this.logView = logView;
    }

    private void closeLogView() {
        isOpen = false;
        rootView.removeView(getLogView());
    }
}
