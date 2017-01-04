package com.ghnor.statusbarfits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 16/12/14.
 */
public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private CheckBox mChangeTranslucent;
    private SeekBar mChangeAlpha;
    private TextView mTvStatusAlpha;

    private int mStatusBarColor;
    private int mStatusBarAlpha = StatusBarFits.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mChangeTranslucent = (CheckBox) findViewById(R.id.change_translucent);
        mChangeAlpha = (SeekBar) findViewById(R.id.change_alpha);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.imgv_ordinary_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdinaryLayoutActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imgv_coordinator_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CoordinatorLayoutActivity.class);
                startActivity(intent);
            }
        });

        mChangeTranslucent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChangeTranslucent.isChecked()) {
                    StatusBarFits.setTranslucent(MainActivity.this, mToolbar);
                } else {
                    StatusBarFits.setColor(MainActivity.this, mStatusBarColor, mStatusBarAlpha);
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
                    StatusBarFits.setTranslucent(MainActivity.this, mStatusBarAlpha);
                } else {
                    StatusBarFits.setColor(MainActivity.this, mStatusBarColor, mStatusBarAlpha);
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

        StatusBarFits.setColor(this, mStatusBarColor, mStatusBarAlpha);
    }
}
