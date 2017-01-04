package com.ghnor.statusbarfits;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 16/12/12.
 */
public class OrdinaryLayoutActivity extends BaseActivity {

    private Activity activity;

    private Toolbar mToolbar;
    private SeekBar mSbChangeAlpha;
    private TextView mTvStatusAlpha;
    private CheckBox mChangeTranslucent;

    private int mStatusBarColor;
    private int mAlpha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordinary_layout);

        activity = this;

        mStatusBarColor = ContextCompat.getColor(this, R.color.colorAccent);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
        mSbChangeAlpha = (SeekBar) findViewById(R.id.change_alpha);
        mChangeTranslucent = (CheckBox) findViewById(R.id.change_translucent);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mChangeTranslucent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeTranslucent.isChecked()) {

//                    StatusBarFits.setColor(activity, mStatusBarColor);
                    StatusBarFits.setTranslucent(activity, mToolbar);
//                    StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, mDrawerLayout);
                } else {
//                    StatusBarFits.setTransparent(activity, mToolbar);
                    StatusBarFits.setColor(activity, mStatusBarColor);
//                    StatusBarUtil.setColorForDrawerLayout(MainActivity.this, mDrawerLayout, StatusBarFits.DEFAULT_STATUS_BAR_ALPHA);
                }
            }
        });

        mSbChangeAlpha.setMax(255);
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
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

//        StatusBarFits.setTransparent(activity, mToolbar);
        StatusBarFits.setColor(activity, mStatusBarColor);
//        StatusBarFits.setTranslucent(this, mToolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
