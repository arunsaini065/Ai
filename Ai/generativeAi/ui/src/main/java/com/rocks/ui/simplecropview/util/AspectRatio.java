package com.rocks.ui.simplecropview.util;


import androidx.annotation.IntRange;

/**
 * Created by yarolegovich https://github.com/yarolegovich
 * on 06.02.2017.
 */

public class AspectRatio {

    @SuppressWarnings("Range")
    public static final AspectRatio IMG_SRC = new AspectRatio(-1, -1, "Free");

    private final int width;
    private final int height;

    public String getName() {
        return name;
    }
    private final String name;

    public AspectRatio(@IntRange(from = 1) int w, @IntRange(from = 1) int h, String name) {
        this.width = w;
        this.height = h;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public boolean isSquare() {
        return width == height;
    }

    public int getHeight() {
        return height;
    }

    public float getRatio() {
        return ((float) width) / height;
    }
}
