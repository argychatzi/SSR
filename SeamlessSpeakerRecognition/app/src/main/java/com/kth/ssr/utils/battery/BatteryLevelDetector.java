package com.kth.ssr.utils.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kth.ssr.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Class  that notifies its observers about battery changes, currently
 * we are interested only in Battery okay and battery low events. To
 * test: am broadcast -a android.intent.action.BATTERY_OKAY
 * am broadcast -a android.intent.action.BATTERY_LOW
 * <p/>
 * Created by argychatzi on 9/5/14.
 */
public class BatteryLevelDetector {

    private static final String TAG = "BatteryLevelDetector";
    public static final float DEFAULT_BATTERY_LOW_THRESHOLD = 0.05f;

    private static Intent mLastBatteryStatusIntent;
    private static List<WeakReference<BatteryLevelObserver>> mObservers;
    private static float mBatteryThreshold;

    public BatteryLevelDetector(Context context) {
        mObservers = new ArrayList<WeakReference<BatteryLevelObserver>>();
        mLastBatteryStatusIntent = generateLastKnownBatteryLevelIntent(context);

        mBatteryThreshold = readThresholdFromPreferences(context);
    }

    private Intent generateLastKnownBatteryLevelIntent(Context context) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent resultIntent = context.registerReceiver(null, intentFilter);

        int level = resultIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = resultIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;

        if (batteryPct > mBatteryThreshold) {
            resultIntent.setAction(Intent.ACTION_BATTERY_OKAY);
        } else {
            resultIntent.setAction(Intent.ACTION_BATTERY_LOW);
        }
        return resultIntent;
    }

    private static float readThresholdFromPreferences(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String batteryThresholdKey = context.getString(R.string.sharedPreferencesBatteryThreshold);
        return Float.valueOf( preferences.getInt(batteryThresholdKey, 0)/100 );
    }

    public static void notifyThresholdChanged(Context context) {
        mBatteryThreshold = readThresholdFromPreferences(context);
    }

    public static void updateBatteryStatusIntent(Intent newIntent) {
        mLastBatteryStatusIntent = newIntent;
        if (isBatteryLow()) {
            Log.v(TAG, "BATTERY LOW");
            for (WeakReference<BatteryLevelObserver> weakReferenceToObserver : mObservers) {
                BatteryLevelObserver observer = weakReferenceToObserver.get();
                if (observer != null) {
                    observer.onBatteryLow();
                }
            }
        } else if (isBatteryOkay()) {
            Log.v(TAG, "BATTERY OK");
            for (WeakReference<BatteryLevelObserver> weakReferenceToObserver : mObservers) {
                BatteryLevelObserver observer = weakReferenceToObserver.get();
                if (observer != null) {
                    observer.onBatteryOkay();
                }
            }
        }
    }

    /**
     * Register a BatteryLevelObserver for updates on the battery status
     *
     * @param observer
     */
    public void registerObserver(BatteryLevelObserver observer) {
        if (observer != null) {
            mObservers.add(new WeakReference<BatteryLevelObserver>(observer));
        } else {
            Log.e(TAG, "cannot register a null observer");
        }
    }

    /**
     * UnRegister a BatteryLevelObserver for updates on the battery status
     *
     * @param observerToBeUnRegistered
     */
    public synchronized void unregisterObserver(BatteryLevelObserver observerToBeUnRegistered) {
        if (observerToBeUnRegistered != null) {

            for (WeakReference<BatteryLevelObserver> batteryLevelObserverWeakReference : mObservers) {
                BatteryLevelObserver observer = batteryLevelObserverWeakReference.get();
                if (observerToBeUnRegistered.equals(observer)) {
                    mObservers.remove(batteryLevelObserverWeakReference);
                    break;
                }
            }

        } else {
            Log.e(TAG, "cannot unregister a null observer");
        }
    }

    public static boolean isBatteryOkay() {
        return Intent.ACTION_BATTERY_OKAY.equals(mLastBatteryStatusIntent.getAction());
    }

    public static boolean isBatteryLow() {
        return Intent.ACTION_BATTERY_LOW.equals(mLastBatteryStatusIntent.getAction());
    }

    public BatteryState getLastBatteryState() {
        BatteryState result = BatteryState.BATTERY_LOW;
        if (isBatteryOkay()) {
            result = BatteryState.BATTERY_OKAY;
        }
        return result;
    }
}
