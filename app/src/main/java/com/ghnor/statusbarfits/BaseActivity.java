package com.ghnor.statusbarfits;

import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

/**
 * Created by ghnor on 16/12/14.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }
}
