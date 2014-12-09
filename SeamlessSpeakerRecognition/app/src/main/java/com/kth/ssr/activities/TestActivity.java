package com.kth.ssr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kth.ssr.R;
import com.kth.ssr.SSRApplication;
import com.kth.ssr.services.samplecollection.SampleCollectingService;
import com.kth.ssr.utils.speechdetection.SpeechRecognizerWrapper;
import com.kth.ssr.utils.speechdetection.VoiceActivityDetectorListener;


public class TestActivity extends BatteryLevelDetectorActivity {

    private static final String TAG = TestActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //TODO set the alarm here
//        AlarmScheduler.setNightAlarm(this);

        Log.d(TAG, ((SSRApplication)getApplication()).getLastBatteryState().name());
        //########## Test Service ##########
        final Intent serviceIntent = new Intent(this, SampleCollectingService.class);
        Button button = (Button)findViewById(R.id.start_stop_service_button);
        if(SampleCollectingService.isRunning(getApplicationContext())){
            button.setText("Stop service");
        } else{
            button.setText("Start service");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( SampleCollectingService.isRunning(getApplicationContext()) ){
                    stopService(serviceIntent);
                    ((Button)v).setText("Start service");
                } else{
                    startService(serviceIntent);
                    ((Button)v).setText("Stop service");
                }
            }
        });


        //########## Test Service ##########
        Button button1 = (Button)findViewById(R.id.service_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "IsServiceRunning: " + SampleCollectingService.isRunning(getApplicationContext()));

            }
        });

        //########## Test Recording ##########
        Button button2 = (Button) findViewById(R.id.record_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent launchIntent = ViewRecordingsActivity.getLaunchIntent(MainActivity.this);
                Intent launchIntent = RecordActivity.getDefaultLaunchIntent(TestActivity.this);
                startActivity(launchIntent);
            }
        });

        //########## Test Recording ##########
        Button button3 = (Button) findViewById(R.id.view_recordigns_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = ViewRecordingsActivity.getDefaultLaunchIntent(TestActivity.this);
                startActivity(launchIntent);
            }
        });

        //########## Test Speech Recognition ##########
        final Button button4 = (Button) findViewById(R.id.start_stop_speech_recognition_button);
        final SpeechRecognizerWrapper speechRecognizerWrapper = new SpeechRecognizerWrapper(getApplicationContext(), new VoiceActivityDetectorListener() {
            @Override
            public void onVoiceActivityDetected() {
                Log.d(TAG, "-------> Speech has started");

            }

            @Override
            public void onNoVoiceActivityDetected() {
                Log.d(TAG, "-------> No Speech Detected");
                button4.setText("Start speech recognition");

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( speechRecognizerWrapper.isListening() ){
                    speechRecognizerWrapper.stopDetectingVoiceActivity();
                    ((Button)v).setText("Start speech recognition");
                } else{
                    speechRecognizerWrapper.startDetectingVoiceActivity();
                    ((Button)v).setText("Stop speech recognition");
                }
            }
        });
    }

    @Override
    public void onBatteryLow() {
        //TODO interact with the service
        super.onBatteryLow();
    }

    @Override
    public void onBatteryOkay() {
        //TODO interact with the service
        super.onBatteryOkay();
    }
}
