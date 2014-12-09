package com.kth.ssr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kth.ssr.R;
import com.kth.ssr.utils.recorder.AudioSoundRecorder;
import com.kth.ssr.utils.recorder.SoundRecorder;

/**
 * Created by argychatzi on 4/6/14.
 */
public class RecordActivity extends Activity {

    private static final String TAG = "RecordingActivity";

    private SoundRecorder mRecorder = null;
    private boolean mIsRecording = false;


    public static Intent getDefaultLaunchIntent(Activity context) {
        return getLaunchIntent(context);
    }

    public static Intent getLaunchIntent(Activity activity) {
        Intent intent = new Intent(activity, RecordActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Bundle extras = getIntent().getExtras();

        Button recordButton = (Button) findViewById(R.id.record_btn);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsRecording) {
                    startRecording();
                } else {
                    stopRecording();
                }
                mIsRecording = !mIsRecording;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder = null;
        }
    }


    private void startRecording() {
//        mRecorder = MediaRecorderWrapper.getInstance();
        mRecorder = new AudioSoundRecorder();
        mRecorder.start();
    }


    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }
}
