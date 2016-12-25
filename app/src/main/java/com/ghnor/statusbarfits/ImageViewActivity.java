package com.ghnor.statusbarfits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 16/12/12.
 */
public class ImageViewActivity extends BaseActivity {
    private Toolbar mToolbar;
    private View mViewNeedOffset;
    private SeekBar mSbChangeAlpha;
    private TextView mTvStatusAlpha;

    private int mAlpha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        // 设置右滑动返回
//        Slidr.attach(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewNeedOffset = findViewById(R.id.view_need_offset);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
        mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSbChangeAlpha.setMax(255);
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
//                StatusBarFits.setTranslucentForImageView(ImageViewActivity.this, mAlpha, mViewNeedOffset);
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
        mViewNeedOffset = findViewById(R.id.view_need_offset);
//        StatusBarFits.setTransparent(this, mViewNeedOffset);
//        StatusBarFits.setTransparent(this);
//        StatusBarFits.setTranslucentForImageView(this, mViewNeedOffset);

        StatusBarFits.setTranslucent(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
