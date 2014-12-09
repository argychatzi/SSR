package com.kth.ssr.utils.battery;

/**
 * Created by argychatzi on 8/28/14.
 */
public interface BatteryLevelObserver {

    public void onBatteryLow();
    public void onBatteryOkay();

}
