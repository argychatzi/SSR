package com.kth.ssr.services.samplecollection;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kth.ssr.BuildConfig;
import com.kth.ssr.R;
import com.kth.ssr.SSRApplication;
import com.kth.ssr.utils.battery.BatteryLevelObserver;
import com.kth.ssr.utils.recordingcontroller.RecordingCollectorFacade;


/**
 * Created by georgios.savvidis on 27/08/14.
 */
public class SampleCollectingService extends Service implements BatteryLevelObserver{
    private static final String TAG = SampleCollectingService.class.getCanonicalName();

    private static final int NOTIFICATION_ID = 123456;

    private NotificationManager mNotificationManager;
    private static RecordingCollectorFacade mRecordingCollectorFacade;

    public static void notifyMicOpeningFrequencyChanged(Context context){
        mRecordingCollectorFacade.reloadMicOpeningFrequency(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");

        mRecordingCollectorFacade = new RecordingCollectorFacade(this);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        ((SSRApplication) getApplication()).addBatteryLevelObserver(this);
        mRecordingCollectorFacade.startDetectingVoiceActivity();
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "OnStartCommand");

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        //TODO Check if this is called when the alarm notifies the Service to stop
        super.onDestroy();

        hideNotification();

        ((SSRApplication) getApplication()).removeBatteryLevelObserver(this);

        if(mRecordingCollectorFacade.isVoiceActivityDetectionSessionActive()){
            mRecordingCollectorFacade.stopDetectingVoiceActivity();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBatteryLow() {
        if(mRecordingCollectorFacade.isVoiceActivityDetectionSessionActive()){
            mRecordingCollectorFacade.stopDetectingVoiceActivity();
        }
    }

    @Override
    public void onBatteryOkay() {
        if(!mRecordingCollectorFacade.isVoiceActivityDetectionSessionActive()){
            mRecordingCollectorFacade.startDetectingVoiceActivity();
        }
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.bg_service_message))
                .setOngoing(true);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void hideNotification(){
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    /*
    //  Method used for testing whether the Service is running.
     */
    public static boolean isRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if ( SampleCollectingService.class.getName().equals( service.service.getClassName() )
                    && service.process.equals(BuildConfig.PACKAGE_NAME ) ) {
                return true;
            }
        }
        return false;
    }
}
