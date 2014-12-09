package com.kth.ssr;


import android.app.Application;

import com.kth.ssr.utils.battery.BatteryLevelDetector;
import com.kth.ssr.utils.battery.BatteryLevelObserver;
import com.kth.ssr.utils.battery.BatteryState;

/**
 * Created by argychatzi on 8/28/14.
 */
public class SSRApplication extends Application {

    private BatteryLevelDetector mBatteryLevelDetector;

    @Override
    public void onCreate() {
        super.onCreate();
        mBatteryLevelDetector = new BatteryLevelDetector(this);
    }

    public void addBatteryLevelObserver(BatteryLevelObserver observer) {
        if (observer != null) {
            mBatteryLevelDetector.registerObserver(observer);
        }
    }

    public void removeBatteryLevelObserver(BatteryLevelObserver observer){
        if (observer != null) {
            mBatteryLevelDetector.unregisterObserver(observer);
        }
    }

    public BatteryState getLastBatteryState(){
        return mBatteryLevelDetector.getLastBatteryState();
    }
}
