package com.ghnor.statusbarfits;

import android.support.v7.app.AppCompatActivity;

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
//        StatusBarFits.setColor(this);
    }
}
