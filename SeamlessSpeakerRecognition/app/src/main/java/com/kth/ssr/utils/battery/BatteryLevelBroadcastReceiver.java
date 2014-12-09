package com.kth.ssr.utils.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class that receives updates from the system about battery changes
 * We are currently interested only in battery ok and battery low
 * system messages
 *
 * Created by argychatzi on 8/27/14.
 */
public class BatteryLevelBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BatteryLevelDetector.updateBatteryStatusIntent(intent);
    }
}
