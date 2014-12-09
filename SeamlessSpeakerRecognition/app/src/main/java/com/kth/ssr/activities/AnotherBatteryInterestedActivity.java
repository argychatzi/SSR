package com.kth.ssr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.kth.ssr.R;

public class AnotherBatteryInterestedActivity extends BatteryLevelDetectorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_battery_interested);
    }

}
