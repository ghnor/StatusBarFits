package com.ghnor.statusbarfits;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 2016/12/24.
 */

public class CoordinatorLayoutActivity extends AppCompatActivity {

    private Activity activity;

    private Toolbar mToolbar;
    private CheckBox mChangeTranslucent;
    private SeekBar mChangeAlpha;
    private TextView mTvStatusAlpha;

    private int mStatusBarColor;
    private int mStatusBarAlpha = StatusBarFits.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        activity = this;

        mStatusBarColor = ContextCompat.getColor(this, R.color.colorAccent);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mChangeTranslucent = (CheckBox) findViewById(R.id.change_translucent);
        mChangeAlpha = (SeekBar) findViewById(R.id.change_alpha);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);

        mChangeTranslucent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeTranslucent.isChecked()) {
                    StatusBarFits.setTranslucent(activity, mToolbar);
//                    StatusBarFits.setColor(activity, mStatusBarColor);
//                    StatusBarFits.setTransparent(activity, mToolbar);
//                    StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, mDrawerLayout);
                } else {
                    StatusBarFits.setColor(activity, mStatusBarColor);
//                    StatusBarFits.setTransparent(activity, mToolbar);
//                    StatusBarUtil.setColorForDrawerLayout(MainActivity.this, mDrawerLayout, StatusBarFits.DEFAULT_STATUS_BAR_ALPHA);
                }
            }
        });

        mChangeAlpha.setMax(255);
        mChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mStatusBarAlpha = progress;
                if (mChangeTranslucent.isChecked()) {
//                    StatusBarFits.setTranslucent(activity, mStatusBarAlpha, mToolbar);
                } else {
//                    StatusBarFits.setColor(activity, mStatusBarColor, mStatusBarAlpha);
                }
                mTvStatusAlpha.setText(String.valueOf(mStatusBarAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mChangeAlpha.setProgress(StatusBarFits.DEFAULT_STATUS_BAR_ALPHA);

//        StatusBarFits.setTransparent(activity, mToolbar);
        StatusBarFits.setColor(activity, mStatusBarColor);
    }
}
