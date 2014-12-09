package com.kth.ssr.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.kth.ssr.R;
import com.kth.ssr.services.samplecollection.SampleCollectingService;
import com.kth.ssr.utils.alarm.AlarmScheduler;
import com.kth.ssr.utils.battery.BatteryLevelDetector;

public class ApplicationSettingsActivity extends PreferenceActivity {

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (getString(R.string.sharedPreferencesStartTime).equals(key)) {
                AlarmScheduler.setStartAlarm(ApplicationSettingsActivity.this);
            } else if (getString(R.string.sharedPreferencesStopTime).equals(key)) {
                AlarmScheduler.setStopAlarm(ApplicationSettingsActivity.this);
            } else if (getString(R.string.sharedPreferencesMicOpeningFrequency).equals(key)){
                SampleCollectingService.notifyMicOpeningFrequencyChanged(ApplicationSettingsActivity.this);
            } else if (getString(R.string.sharedPreferencesBatteryThreshold).equals(key)){
                BatteryLevelDetector.notifyThresholdChanged(ApplicationSettingsActivity.this);
            }
//            else if (getString(R.string.sharedPreferencesVoiceActivityDetectionDuration).equals(key)){
//                BatteryLevelDetector.notifyThresholdChanged(ApplicationSettingsActivity.this);
//            }
        }
    };

    public static Intent getLaunchIntent(Activity activity) {
        return new Intent(activity, ApplicationSettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.app_preferences);
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}