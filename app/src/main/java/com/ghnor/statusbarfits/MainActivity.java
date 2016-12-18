package com.ghnor.statusbarfits;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ghnor.library.StatusBarFits;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarFits.setColor(this);
    }
}
