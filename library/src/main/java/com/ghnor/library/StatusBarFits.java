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
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
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
                contentLayout.getChildAt(1).setPadding(
                        contentLayout.getPaddingLeft(),
                        getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                        contentLayout.getPaddingRight(),
                        contentLayout.getPaddingBottom());
            }
            // 设置属性
            setDrawerLayoutProperty(drawerLayout, contentLayout);
            addTranslucentView(activity, statusBarAlpha);

        } else if (decorView.getChildAt(0) instanceof CoordinatorLayout) {


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = contentView.getChildCount(); i < count; i++) {
            View childView = contentView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
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

    public static void setTranslucent(Activity activity, View needOffsetView) {
        setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
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
        setTranslucent(activity, statusBarAlpha, null);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(activity, needOffsetView);
        addTranslucentView(activity, statusBarAlpha);
    }

    /**
     * 设置状态栏全透明
     *
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity) {
        setTransparent(activity, null);
    }

    public static void setTransparent(Activity activity, View needOffsetView) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildAt(0) == null) {
            return;
        }
        if (contentView.getChildAt(0) instanceof DrawerLayout) {
            // 让DrawerLayout中的布局内容可以延伸到状态栏
            // 为了实现上述效果，设置DrawerLayout以及两个子View的fitsSystemWindows为false
            // 这个带来了一个问题：
            // DrawerLayout的内容布局会从屏幕最上方开始绘制，
            // 所以需要下移避免被状态栏遮挡的布局，手动设置marginTop。
            DrawerLayout drawerLayout = (DrawerLayout) contentView.getChildAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                activity.getWindow().getDecorView().setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//                activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            // 内容布局不是 LinearLayout 时,设置padding top
//            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
//                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
//            }

            // 设置属性
            setDrawerLayoutProperty(drawerLayout, contentLayout);

        } else if (contentView.getChildAt(0) instanceof CoordinatorLayout) {
            transparentStatusBar(activity);
//            setCoordinatorLayoutProperty((CoordinatorLayout) contentView.getChildAt(0));

        } else {
            transparentStatusBar(activity);
//            setFitsSystemWindows(activity);
        }

        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        addTranslucentView(activity, statusBarAlpha, contentView);

    }

    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     * @param contentView    需要添加半透明遮罩的 ViewGroup
     */
    private static void addTranslucentView(Activity activity, int statusBarAlpha, ViewGroup contentView) {
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

    private static void setCoordinatorLayoutProperty(CoordinatorLayout coordinatorLayoutProperty) {
        coordinatorLayoutProperty.setFitsSystemWindows(false);
    }

}
