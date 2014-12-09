package com.kth.ssr.utils.recordingcontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kth.ssr.R;
import com.kth.ssr.utils.phonecall.PhoneCallListener;
import com.kth.ssr.utils.phonecall.PhoneCallManager;
import com.kth.ssr.utils.recorder.AudioSoundRecorder;
import com.kth.ssr.utils.recorder.SoundRecorder;
import com.kth.ssr.utils.speechdetection.SpeechRecognizerWrapper;
import com.kth.ssr.utils.speechdetection.VoiceActivityDetector;
import com.kth.ssr.utils.speechdetection.VoiceActivityDetectorListener;
import com.kth.ssr.utils.timer.Timer;
import com.kth.ssr.utils.timer.TimerListener;

/**
 * This class is responsible for starting the recording when:
 * <p/>
 * 1. A phone call has been initiated
 * 2. The timer has elapsed and there is voice detected by the {@link com.kth.ssr.utils.speechdetection.SpeechRecognizerWrapper} class.
 * <p/>
 * Accordingly, it is, also, responsible for stopping the recording when:
 * <p/>
 * 1. A phone call has been terminated
 * 2. The {@link com.kth.ssr.utils.speechdetection.SpeechRecognizerWrapper} class has detected that there is no input speech anymore.
 * <p/>
 * Implementations of this class should call {@link #startDetectingVoiceActivity()}/{@link #stopDetectingVoiceActivity()} to begin and terminate its operation, respectively.
 * <p/>
 * Created by georgios.savvidis on 14/09/14.
 */
public class RecordingCollectorFacade implements PhoneCallListener, VoiceActivityDetectorListener, TimerListener, VoiceActivityDetector {
    private static final String TAG = RecordingCollectorFacade.class.getCanonicalName();

    private static final int ACTION_TRIGGERED_BY_PHONE_CALL_MANAGER = 0;
    private static final int ACTION_TRIGGERED_BY_STANDARD_RECORDING = 1;

    private SoundRecorder mSoundRecorder;

    private SpeechRecognizerWrapper mSpeechRecognizerWrapper;

    private PhoneCallManager mPhoneCallManager;
    private Timer mMicOpeningTimer;
    private Timer mRecorderDurationTimer;

    private boolean mIsActive;
    private int mRecordingStartedBy;

    public RecordingCollectorFacade(Context context) {
        Log.d(TAG, "RecordingCollectorFacade created.");

        mSoundRecorder = new AudioSoundRecorder();

        mSpeechRecognizerWrapper = new SpeechRecognizerWrapper(context, this);
        mPhoneCallManager = new PhoneCallManager(context, this);

        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sharedPreferencesFrequencyKey = context.getString(R.string.sharedPreferencesMicOpeningFrequency);
        int micOpeningFrequency = defaultPreferences.getInt(sharedPreferencesFrequencyKey, Timer.DEFAULT_VOICE_DETECTION_INTERVAL);

        mMicOpeningTimer = new Timer(this, micOpeningFrequency);
        mRecorderDurationTimer = new Timer(this, Timer.DEFAULT_RECORDING_DURATION);
    }

    public void reloadMicOpeningFrequency(Context context) {
        SharedPreferences defaultPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sharedPreferencesFrequencyKey = context.getString(R.string.sharedPreferencesMicOpeningFrequency);
        int micOpeningFrequency = defaultPreferences.getInt(sharedPreferencesFrequencyKey, Timer.DEFAULT_VOICE_DETECTION_INTERVAL);

        if(mMicOpeningTimer.isStarted()){
            mMicOpeningTimer.setExpiryTimeAndStart(micOpeningFrequency);
        } else {
            mMicOpeningTimer.setExpiryTime(micOpeningFrequency);
        }
    }

    public void startDetectingVoiceActivity() {
        Log.d(TAG, "startDetectingVoiceActivity()");

        mMicOpeningTimer.start();
        mPhoneCallManager.startListeningForIncomingCalls();
        mSpeechRecognizerWrapper.startDetectingVoiceActivity();

        mIsActive = true;
    }

    public boolean isVoiceActivityDetectionSessionActive() {
        return mIsActive;
    }

    public void stopDetectingVoiceActivity() {
        Log.d(TAG, "stopDetectingVoiceActivity()");

        mMicOpeningTimer.stop();
        mPhoneCallManager.stopListeningForIncomingCalls();
        mSpeechRecognizerWrapper.stopDetectingVoiceActivity();

        //Stop any ongoing recording.
        mSoundRecorder.stop();

        mIsActive = false;
    }

    private void startRecording(int triggeredBy) {
        Log.d(TAG, "start(). TriggeredBy: " + triggeredBy);
//        if( !mSoundRecorder.isRecording() ){
        Log.d(TAG, "It's not recording, so start it.");
        mSoundRecorder.start();
        mRecordingStartedBy = triggeredBy;
//        }

        if (triggeredBy == ACTION_TRIGGERED_BY_PHONE_CALL_MANAGER) {
            //Even if the recording was already triggered by the SpeechRecognizer, we want to change
            //the value of the mRecordingStartedBy, so that later the recording will be stopped when the stop()
            //is triggered by the PhoneCallListener and not by the SpeechRecognizer.
            mRecordingStartedBy = triggeredBy;
            Log.d(TAG, "Update triggeredBy: " + mRecordingStartedBy);

        } else {
            //If the this was not triggered by the PhoneCallManager, start mRecorderDurationTimer in order to stop the recording when the
            //timer expires.
            mRecorderDurationTimer.restart();
        }

        //Stop the timer and start it again when the recording ends.
        mMicOpeningTimer.stop();
    }

    private void stopRecording(int triggeredBy) {
        Log.d(TAG, "stop(): " + triggeredBy);

        if (triggeredBy == mRecordingStartedBy) {
            Log.d(TAG, "triggeredBy == mRecordingStartedBy. Stop recording and start timer.");

            mSoundRecorder.stop();

            //At this point, it's safer to call mMicOpeningTimer.restart() than mMicOpeningTimer.start().
            mMicOpeningTimer.restart();
        }
    }

    @Override
    public void onPhoneCallStarted() {
        Log.d(TAG, "onPhoneCallStarted()");
        startRecording(ACTION_TRIGGERED_BY_PHONE_CALL_MANAGER);
    }

    @Override
    public void onPhoneCallEnded() {
        Log.d(TAG, "onPhoneCallEnded()");
        stopRecording(ACTION_TRIGGERED_BY_PHONE_CALL_MANAGER);
    }

    @Override
    public void onTimerExpired(Timer timer) {
        Log.d(TAG, "OnTimerExpired.");

        if (timer.equals(mMicOpeningTimer) && !mSpeechRecognizerWrapper.isListening()) {
            Log.d(TAG, "SpeechRecognizer timer expired. Start listening for speech");
            mSpeechRecognizerWrapper.startDetectingVoiceActivity();

        } else if (timer.equals(mRecorderDurationTimer)) {
            Log.d(TAG, "VoiceRecorder timer expired. stop recording now");
            stopRecording(ACTION_TRIGGERED_BY_STANDARD_RECORDING);
        }
    }

    @Override
    public void onVoiceActivityDetected() {
        Log.d(TAG, "onVoiceDetected()");
        startRecording(ACTION_TRIGGERED_BY_STANDARD_RECORDING);
    }

    @Override
    public void onNoVoiceActivityDetected() {
        Log.d(TAG, "onNoVoiceDetected(). Restart timer.");
        //At this point, it's safer to call mMicOpeningTimer.restart() than mMicOpeningTimer.start().
        mMicOpeningTimer.restart();
    }
}