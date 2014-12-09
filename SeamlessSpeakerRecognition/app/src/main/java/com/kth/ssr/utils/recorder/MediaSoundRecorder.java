package com.kth.ssr.utils.recorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.kth.ssr.utils.time.Timestamp;
import com.kth.ssr.voicesample.VoiceSampleFinder;
import com.kth.ssr.voicesample.models.VoiceSample;

import java.io.IOException;
import java.util.List;

/**
 * Created by georgios.savvidis on 28/08/14.
 */
public class MediaSoundRecorder implements SoundRecorder {
    private static final String TAG = MediaSoundRecorder.class.getName();

    private final static String DEFAULT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString();// + "/SSR";
    private MediaRecorder mRecorder = null;
    private String mOutputPath;
    private boolean mIsRecording;

    private static MediaSoundRecorder sInstance;

    private MediaSoundRecorder() {
        Log.d(TAG, "VoiceRecorder created.");
        mOutputPath = DEFAULT_PATH;
        initRecorder();
    }

    public static MediaSoundRecorder getInstance() {
        if (sInstance == null) {
            sInstance = new MediaSoundRecorder();
        }
        return sInstance;
    }

    private void initRecorder() {
        Log.d(TAG, "InitRecorder()");

        mRecorder = new MediaRecorder();
        mIsRecording = false;

        try {
            mRecorder.setAudioSource(RecorderConfiguration.AUDIO_SOURCE);
            mRecorder.setOutputFormat(RecorderConfiguration.OUTPUT_FORMAT);
            mRecorder.setOutputFile(mOutputPath + "/" + Timestamp.nowAsTrimmedString());
            mRecorder.setAudioEncoder(RecorderConfiguration.AUDIO_ENCODER);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "preparing recorder...");
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Log.d(TAG, "start()");

        //Since the recorder is released when the stop() method is called,
        //we have to make sure that it's initialized again if necessary.
        if (mRecorder == null) {
            initRecorder();
        }

        Log.d(TAG, "starting recorder...");
        mRecorder.start();
        mIsRecording = true;
    }

    public void stop() {
        Log.d(TAG, "stop()");

        //For our project it's better if we release the recorder every time the stop() method is called,
        //because the next time the start() method will be called might be after several minutes. This way,
        //we make sure that in the meantime the recorder won't degrade the performance.
        if (mRecorder != null) {
            try {
                Log.d(TAG, "stopping recorder...");
                mRecorder.stop();
            } catch (IllegalStateException e) {
                //Do nothing. If it gets here, it means that stop() was called before start().
            }

            Log.d(TAG, "releasing recorder...");
            mRecorder.release();
            mRecorder = null;
        }

        mIsRecording = false;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public List<VoiceSample> getRecordings() {
        return VoiceSampleFinder.getSamples(mOutputPath);
    }
}