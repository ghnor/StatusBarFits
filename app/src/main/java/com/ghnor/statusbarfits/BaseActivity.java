package com.ghnor.statusbarfits;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by ghnor on 16/12/14.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
