package com.example.biz_home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.common.ui.component.HiBaseActivity;
import com.example.library.utils.HiStatusBar;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic logic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HiStatusBar.INSTANCE.setStatusBar(this, true, Color.TRANSPARENT, false);
        logic = new MainActivityLogic(this, savedInstanceState);
    }

    @Override
    public int layoutIdRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        logic.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (BuildConfig.DEBUG) {
                try {
                    Class<?> clazz = Class.forName("com.example.debug_tools.DebugToolDialogFragment");
                    DialogFragment target = (DialogFragment) clazz.getConstructor().newInstance();
                    target.show(getSupportFragmentManager(), "debug_tools");
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
