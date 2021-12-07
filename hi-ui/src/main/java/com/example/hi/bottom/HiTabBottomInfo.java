package com.example.hi.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class HiTabBottomInfo<Color> {
    public enum TabType {
        BITMAP, ICON
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectBitmap;
    public String iconFont;
    public String defaultIconName;
    public String selectIconName;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    public HiTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectBitmap = selectBitmap;
        this.tabType = TabType.BITMAP;
    }

    public HiTabBottomInfo(String name, String iconFont, String defaultIconName, String selectIconName, Color tintColor, Color defaultColor) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultColor = defaultColor;
        this.defaultIconName = defaultIconName;
        this.selectIconName = selectIconName;
        this.tintColor = tintColor;
        this.tabType = TabType.ICON;
    }
}
