package com.example.common.ui.component;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class HiBaseFragment<T extends ViewDataBinding> extends Fragment {
    //fragment的根布局
    protected View layoutView;


    public T dataBinding;

    @LayoutRes
    public abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        layoutView = dataBinding.getRoot();
        return layoutView;
    }


    public boolean isAlive() {
        if (isRemoving() || isDetached() || getActivity() == null) {
            return false;
        }
        return true;
    }
}
