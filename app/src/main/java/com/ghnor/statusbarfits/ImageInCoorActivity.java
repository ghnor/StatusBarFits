package com.ghnor.statusbarfits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.ghnor.library.StatusBarFits;

/**
 * Created by ghnor on 2016/12/24.
 */

public class ImageInCoorActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_in_coor);

        StatusBarFits.setTransparent(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
}
