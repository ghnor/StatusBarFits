package com.ghnor.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static com.ghnor.library.StatusBarView.createStatusBarView;
import static com.ghnor.library.StatusBarView.createTranslucentStatusBarView;
import static com.ghnor.library.Utils.getStatusBarHeight;

/**
 * Created by ghnor on 2016/12/18.
 */

public class StatusBarFits {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     */
    public static void setColor(Activity activity) {
        setColor(activity, Utils.getDefaultStatusBarBackground(activity));
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的 activity
     * @param color    状态栏颜色值
     */
    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, 0);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity       需要设置的activity
     * @param color          状态栏颜色值
     * @param statusBarAlpha 状态栏透明度
     */

    public static void setColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Utils.calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (decorView.getChildAt(0) == null) {
            return;
        }
        if (decorView.getChildAt(0) instanceof DrawerLayout) {
            DrawerLayout drawerLayout = (DrawerLayout) decorView.getChildAt(0);
            // 生成一个状态栏大小的矩形
            // 添加 statusBarView 到布局中
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(color);
            } else {
                StatusBarView statusBarView = createStatusBarView(activity, color);
                contentLayout.addView(statusBarView, 0);
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1)
                        .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                                contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }
            // 设置属性
            setDrawerLayoutProperty(drawerLayout, contentLayout);
            addTranslucentView(activity, statusBarAlpha);

        } else if (decorView.getChildAt(0) instanceof CoordinatorLayout) {


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().setStatusBarColor(Utils.calculateStatusColor(color, statusBarAlpha));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                int count = decorView.getChildCount();
                if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                    decorView.getChildAt(count - 1)
                            .setBackgroundColor(Utils.calculateStatusColor(color, statusBarAlpha));
                } else {
                    StatusBarView statusView = createStatusBarView(activity, color, 0);
                    decorView.addView(statusView);
                }
                setFitsSystemWindows(activity);
            }
        }
    }

    /**
     * 为DecorView的子View设置FitsSystemWindows
     * @param activity
     */
    private static void setFitsSystemWindows(Activity activity) {
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        View contentChild = content.getChildAt(0);
        if (contentChild != null) {
            contentChild.setFitsSystemWindows(true);
            if (contentChild instanceof ViewGroup) {
                ((ViewGroup) contentChild).setClipToPadding(true);
            }
        }
    }

    /**
     * 使状态栏半透明
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 使状态栏半透明
     *
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity       需要设置的activity
     * @param statusBarAlpha 状态栏透明度
     */
    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(activity);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setFitsSystemWindows(activity);
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

}
