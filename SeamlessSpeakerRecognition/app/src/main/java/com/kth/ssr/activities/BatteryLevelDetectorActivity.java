package com.kth.ssr.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.kth.ssr.R;
import com.kth.ssr.SSRApplication;
import com.kth.ssr.utils.battery.BatteryLevelObserver;

/**
 * Created by argychatzi on 8/28/14.
 */
public class BatteryLevelDetectorActivity extends Activity implements BatteryLevelObserver {

    private static final String TAG = "BatterLevelDetectorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SSRApplication) getApplication()).addBatteryLevelObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((SSRApplication) getApplication()).removeBatteryLevelObserver(this);
    }

    @Override
    public void onBatteryLow() {
        Log.d(TAG, "onBatteryLow");
    }

    @Override
    public void onBatteryOkay() {
        Log.d(TAG, "onBatteryOkay");
    }

}
