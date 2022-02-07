package com.example.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

public class HiViewUtil {
    public static <T> T findTypeView(@Nullable ViewGroup viewGroup, Class<T> cls) {
        if (viewGroup == null) {
            return null;
        }
        Deque<View> deque = new ArrayDeque<>();
        deque.add(viewGroup);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();
            if (cls.isInstance(node)) {
                return cls.cast(node);
            } else if (node instanceof ViewGroup) {
                ViewGroup container = (ViewGroup) node;
                for (int i = 0, count = container.getChildCount(); i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }

    public static boolean isActivityDestroyed(Context context) {
        Activity activity = findActivity(context);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return activity.isDestroyed() || activity.isFinishing();
            }

            return activity.isFinishing();
        }

        return true;
    }

    public static Activity findActivity(Context context) {
        //怎么判断context 是不是activity 类型的
        if (context instanceof Activity) return (Activity) context;
        else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
