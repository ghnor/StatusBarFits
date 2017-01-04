package com.ghnor.statusbarfits;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 2016/12/24.
 */

public class CoordinatorLayoutActivity extends BaseActivity {

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

        mStatusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);

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
                } else {
                    StatusBarFits.setColor(activity, mStatusBarColor, mStatusBarAlpha);
                }
            }
        });

        mChangeAlpha.setMax(255);
        mChangeAlpha.setProgress(StatusBarFits.DEFAULT_STATUS_BAR_ALPHA);
        mChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mStatusBarAlpha = progress;
                if (mChangeTranslucent.isChecked()) {
                    StatusBarFits.setTranslucent(activity, mStatusBarAlpha, mToolbar);
                } else {
                    StatusBarFits.setColor(activity, mStatusBarColor, mStatusBarAlpha);
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

        mTvStatusAlpha.setText(String.valueOf(mStatusBarAlpha));

        StatusBarFits.setColor(activity, mStatusBarColor, mStatusBarAlpha);
    }
}
