package com.ghnor.statusbarfits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;
import com.jaeger.library.StatusBarUtil;

/**
 * Created by ghnor on 16/12/14.
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private CheckBox mChbTranslucent;
    private Button mBtnSetColor;
    private Button mBtnSetTransparent;
    private Button mBtnSetTranslucent;
    private Button mBtnSetForImageView;
    private Button mBtnSetImageInCoor;
    private Button mBtnUseInFragment;
    private Button mBtnSetColorForSwipeBack;

    private ViewGroup contentLayout;
    private SeekBar mSbChangeAlpha;
    private TextView mTvStatusAlpha;

    private int mStatusBarColor;
    private int mAlpha = StatusBarFits.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        StatusBarFits.setTransparent(this, toolbar);
//        StatusBarFits.setTransparent(this);
        StatusBarUtil.setTransparentForDrawerLayout(this, drawerLayout);
//        StatusBarUtil.setTransparentForImageView(this, toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        contentLayout = (ViewGroup) findViewById(R.id.main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mChbTranslucent = (CheckBox) findViewById(R.id.chb_translucent);
        mBtnSetColor = (Button) findViewById(R.id.btn_set_color);
        mBtnSetTransparent = (Button) findViewById(R.id.btn_set_transparent);
        mBtnSetTranslucent = (Button) findViewById(R.id.btn_set_translucent);
        mBtnSetForImageView = (Button) findViewById(R.id.btn_set_for_image_view);
        mBtnSetImageInCoor = (Button) findViewById(R.id.btn_set_image_in_coor);
        mBtnUseInFragment = (Button) findViewById(R.id.btn_use_in_fragment);
        mBtnSetColorForSwipeBack = (Button) findViewById(R.id.btn_set_color_for_swipe_back);
        mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mBtnSetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ColorStatusBarActivity.class);
                startActivity(intent);
            }
        });

        mBtnSetTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, true);
                startActivity(intent);
            }
        });

        mBtnSetTranslucent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageStatusBarActivity.class);
                intent.putExtra(ImageStatusBarActivity.EXTRA_IS_TRANSPARENT, false);
                startActivity(intent);
            }
        });

        mBtnSetForImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
                startActivity(intent);
            }
        });

        mBtnSetImageInCoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImageInCoorActivity.class);
                startActivity(intent);
            }
        });

        mBtnUseInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UseInFragmentActivity.class);
                startActivity(intent);
            }
        });

        mBtnSetColorForSwipeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SwipeBackActivity.class);
                startActivity(intent);
            }
        });

        mChbTranslucent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChbTranslucent.isChecked()) {
                    contentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_monkey));
                    StatusBarFits.setTranslucent(MainActivity.this, mAlpha);
                    mToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else {
                    contentLayout.setBackgroundDrawable(null);
                    mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    StatusBarFits.setColor(MainActivity.this,
                        getResources().getColor(R.color.colorPrimary), mAlpha);
                }
            }
        });

        mSbChangeAlpha.setMax(255);
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                if (mChbTranslucent.isChecked()) {
                    StatusBarFits.setTranslucent(MainActivity.this, mAlpha);
                } else {
//                    StatusBarFits.setColorFor(MainActivity.this, mStatusBarColor, mAlpha);
                }
                mTvStatusAlpha.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbChangeAlpha.setProgress(StatusBarFits.DEFAULT_STATUS_BAR_ALPHA);
    }

    @Override
    protected void setStatusBar() {
//        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
//        StatusBarFits.setColor(this, mStatusBarColor, mAlpha);
    }
}
