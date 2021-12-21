package com.example.shopapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.ui.component.HiBaseFragment;
import com.example.library.executor.HiExecutor;
import com.example.shopapp.R;

public class HomePageFragment extends HiBaseFragment {
    private Boolean pasuse = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //按优先级执行任务
        layoutView.findViewById(R.id.buttonOne).setOnClickListener(v -> {
            for (int priority = 0; priority < 10; priority++) {
                int finalPriority = priority;
                HiExecutor.INSTANCE.execute(priority, () -> {
                    try {
                        Thread.sleep(1000 - finalPriority * 100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        layoutView.findViewById(R.id.buttonTwo).setOnClickListener(v -> {
            if (pasuse) {
                HiExecutor.INSTANCE.resume();
            } else {
                HiExecutor.INSTANCE.pause();
            }
            pasuse = !pasuse;
        });
        layoutView.findViewById(R.id.buttonThree).setOnClickListener(v -> {
            HiExecutor.INSTANCE.execute(new HiExecutor.Callable<String>() {
                @Override
                public String onBackground() {
                    return "我是异步执行的结果";
                }

                @Override
                public void onComplete(String string) {
                    Log.e("MainActivity", "onComplete-当前线程是" + Thread.currentThread().getName());
                    Log.e("MainActivity", "onComplete-任务执行的结果" + string);
                }
            });
        });
    }
}

