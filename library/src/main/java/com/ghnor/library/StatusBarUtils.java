package com.ghnor.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;

/**
 * Created by ghnor on 2016/12/18.
 * ghnor.me@gmail.com
 */

public class StatusBarUtils {

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 检查是否有底部导航栏
     *
     * @param context
     * @return
     */
    public static boolean hasNavigationBar(Context context) {
        int resIdShow = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNavigationBar = false;
        if(resIdShow > 0){
            hasNavigationBar = context.getResources().getBoolean(resIdShow);
        }
        return hasNavigationBar;
    }

    /**
     * 获取底部导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if(hasNavigationBar(context)) {
            int resIdNavigationBar = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            int navigationbarHeight = 0;
            if (resIdNavigationBar > 0) {
                navigationbarHeight = context.getResources().getDimensionPixelSize(resIdNavigationBar);
            }
            return navigationbarHeight;
        }
        return -1;
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    public static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 减去额外的alpha获得真正的颜色
     * @return
     */
    public static int calculateStatusColorSub(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red / a + 0.5);
        green = (int) (green / a + 0.5);
        blue = (int) (blue / a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 获取状态栏默认的颜色
     * 1. 在5.0及以下即colorPrimaryDark
     * 2. 在4.4及以下默认黑色
     */
    private static final int[] THEME_ATTRS = {
            android.R.attr.colorPrimaryDark
    };

    public static int getDefaultStatusBarBackground(Context context) {
        final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
        try {
            return a.getColor(0, 0xff000000);
        } finally {
            a.recycle();
        }
    }

}
