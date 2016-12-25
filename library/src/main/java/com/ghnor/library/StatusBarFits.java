package com.ghnor.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
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

    private static final String TAG_ADD_TOP = "add_top";
    private static final String TAG_REMOVE_TOP = "remove_top";
    private static final String TAG_NEED_OFFNET = "need_offset";

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
//            activity.getWindow().setStatusBarColor(Utils.calculateStatusColor(color, statusBarAlpha));
//            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildAt(0) == null) {
            return;
        }
        if (contentView.getChildAt(0) instanceof DrawerLayout) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            DrawerLayout drawerLayout = (DrawerLayout) contentView.getChildAt(0);
            // 生成一个状态栏大小的矩形
            // 添加 statusBarView 到布局中
            ViewGroup drawerLayoutContent;
            // 判断内容布局是否为ViewGroup
            if (drawerLayout.getChildAt(0) instanceof ViewGroup) {
                drawerLayoutContent = (ViewGroup) drawerLayout.getChildAt(0);
            } else {
                throw new UnsupportedOperationException("The content layout in DrawerLayout must be ViewGroup.");
            }
            // 如果内容布局的第一个子View就是添加的StatusBarView直接设置颜色，否则先添加
            if (drawerLayoutContent.getChildCount() > 0 && drawerLayoutContent.getChildAt(0) instanceof StatusBarView) {
                drawerLayoutContent.getChildAt(0).setBackgroundColor(color);
            } else {
                StatusBarView statusBarView = createStatusBarView(activity, color);
                drawerLayoutContent.addView(statusBarView, 0);
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (!(drawerLayoutContent instanceof LinearLayout) &&
                    drawerLayoutContent.getChildAt(1) != null) {

                if (drawerLayoutContent.getChildAt(1).getTag(R.id.tag_top) == null ||
                        (drawerLayoutContent.getChildAt(1).getTag(R.id.tag_top) != null &&
                                drawerLayoutContent.getChildAt(1).getTag(R.id.tag_top).equals(TAG_REMOVE_TOP))) {

                    ViewGroup.MarginLayoutParams layoutParams =
                            (ViewGroup.MarginLayoutParams) drawerLayoutContent.getChildAt(1).getLayoutParams();
                    layoutParams.topMargin += getStatusBarHeight(activity);
                    drawerLayoutContent.getChildAt(1).setLayoutParams(layoutParams);

                    drawerLayoutContent.getChildAt(1).setTag(R.id.tag_top, TAG_ADD_TOP);
                }
            }
            // 设置属性
            setDrawerLayoutProperty(drawerLayout, drawerLayoutContent);
            addTranslucentView(activity, statusBarAlpha);

        } else if (contentView.getChildAt(0) instanceof CoordinatorLayout) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(Utils.calculateStatusColor(color, statusBarAlpha));
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                int count = decorView.getChildCount();
                if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                    decorView.getChildAt(count - 1)
                            .setBackgroundColor(Utils.calculateStatusColor(color, statusBarAlpha));
                } else {
                    StatusBarView statusView = createStatusBarView(activity, color, 0);
                    decorView.addView(statusView);
                }
            }

            setFitsSystemWindows(activity, true);

        } else {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1)
                        .setBackgroundColor(Utils.calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(activity, color, 0);
                decorView.addView(statusView);
            }
            setFitsSystemWindows(activity, true);
        }

        findOffsetView(activity, contentView);
    }

    /**
     * 循环遍历找到之前添加了topMargin的View
     * @param context
     * @param contentView
     * @return
     */
    private static View findOffsetView(Context context, ViewGroup contentView) {
        for(int count = 0; count < contentView.getChildCount(); count++) {
            if (contentView.getChildAt(count).getTag(R.id.tag_need_offset) != null &&
                    contentView.getChildAt(count).getTag(R.id.tag_need_offset).equals(TAG_NEED_OFFNET)) {
                removeOffsetMargin(context, contentView.getChildAt(count));
                return contentView.getChildAt(count);
            } else {
                if (contentView.getChildAt(count) instanceof ViewGroup) {
                    findOffsetView(context, (ViewGroup) contentView.getChildAt(count));
                }
            }
        }
        return null;
    }

    /**
     * 去掉该View上的topMargin
     * @param context
     * @param offsetView
     */
    private static void removeOffsetMargin(Context context, View offsetView) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) offsetView.getLayoutParams();
        layoutParams.topMargin -= getStatusBarHeight(context);

        offsetView.setTag(R.id.tag_need_offset, null);
    }

    /**
     * 为DecorView的子View设置FitsSystemWindows
     * @param activity
     */
    private static void setFitsSystemWindows(Activity activity, boolean b) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = contentView.getChildCount(); i < count; i++) {
            View childView = contentView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(b);
                if (b) {
                    ((ViewGroup) childView).setClipToPadding(true);
                }
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
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }

            ViewGroup drawerLayoutContent = (ViewGroup) drawerLayout.getChildAt(0);
            //如果DrawerLayout的内容布局第一个子View是我们自己的StatusBarView就将它reomve。
            if (drawerLayoutContent.getChildAt(0) instanceof StatusBarView) {
//                drawerLayoutContent.getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
                drawerLayoutContent.removeViewAt(0);
            }

            // 内容布局不是 LinearLayout 时,清除多余的StatusBarHeight。
            if (!(drawerLayoutContent instanceof LinearLayout) &&
                    drawerLayoutContent.getChildAt(0) != null &&
                    drawerLayoutContent.getChildAt(0).getTag(R.id.tag_top) != null &&
                    drawerLayoutContent.getChildAt(0).getTag(R.id.tag_top).equals(TAG_ADD_TOP)) {

                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) drawerLayoutContent.getChildAt(0).getLayoutParams();
                layoutParams.topMargin -= getStatusBarHeight(activity);
                drawerLayoutContent.getChildAt(0).setLayoutParams(layoutParams);

                drawerLayoutContent.getChildAt(0).setTag(R.id.tag_top, TAG_REMOVE_TOP);
            }

            ViewCompat.requestApplyInsets(contentView.getChildAt(0));

            // 设置属性
            setDrawerLayoutProperty(drawerLayout, drawerLayoutContent);

        } else if (contentView.getChildAt(0) instanceof CoordinatorLayout) {
            transparentStatusBar(activity);
//            setCoordinatorLayoutProperty((CoordinatorLayout) contentView.getChildAt(0));
            removeStatusBarViewInDecorView(activity);
            setFitsSystemWindows(activity, false);

        } else {
            transparentStatusBar(activity);
//            setFitsSystemWindows(activity);
        }

        if (needOffsetView != null && needOffsetView.getTag(R.id.tag_need_offset) == null) {

            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);

            needOffsetView.setTag(R.id.tag_need_offset, TAG_NEED_OFFNET);
        }
    }

    private static void removeStatusBarViewInDecorView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        for (int index = 0; index < decorView.getChildCount(); index++) {
            if (decorView.getChildAt(index) instanceof StatusBarView) {
                decorView.removeViewAt(index);
            }
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            activity.getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (contentView.getChildCount() > 1) {
                contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
            } else {
                contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
            }
        }
    }

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContent DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContent) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContent.setFitsSystemWindows(false);
        drawerLayoutContent.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    private static void setCoordinatorLayoutProperty(CoordinatorLayout coordinatorLayoutProperty) {
        coordinatorLayoutProperty.setFitsSystemWindows(false);
    }

}
