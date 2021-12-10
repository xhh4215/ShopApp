package com.example.hi.top;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class HiTabTopInfo<Color> {
    public enum TabType {
        BITMAP, TEXT
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectBitmap;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabTopInfo(String name, Bitmap defaultBitmap, Bitmap selectBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectBitmap = selectBitmap;
        this.tabType = TabType.BITMAP;
    }

    public HiTabTopInfo(String name, Color tintColor, Color defaultColor) {
        this.name = name;

        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.tabType = TabType.TEXT;
    }
}
