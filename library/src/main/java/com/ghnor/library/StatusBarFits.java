package com.ghnor.library;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import static com.ghnor.library.StatusBarView.createStatusBarView;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Utils.calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
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

}
