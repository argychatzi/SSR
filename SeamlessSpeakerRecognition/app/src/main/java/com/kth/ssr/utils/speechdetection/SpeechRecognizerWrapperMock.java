package com.kth.ssr.utils.speechdetection;

import android.util.Log;

import com.kth.ssr.utils.timer.Timer;
import com.kth.ssr.utils.timer.TimerListener;

/**
 * Created by argychatzi on 9/27/14.
 */
public class SpeechRecognizerWrapperMock implements TimerListener, VoiceActivityDetector {

    private static final String TAG = "SpeechRecognizeWrapperMock";
    private static final int TIMER_EXPIRATION_TIME = 3000;

    private Timer mTimer;
    private VoiceActivityDetectorListener mVoiceActivityDetectorListener;

    public SpeechRecognizerWrapperMock(VoiceActivityDetectorListener voiceListener) {
        mTimer = new Timer(this, Timer.DEFAULT_VOICE_DETECTION_INTERVAL);
        mTimer.setExpiryTime(TIMER_EXPIRATION_TIME);
        mVoiceActivityDetectorListener = voiceListener;
    }

    public void onTimerExpired(Timer timer) {
            mVoiceActivityDetectorListener.onVoiceActivityDetected();
    }

    @Override
    public void startDetectingVoiceActivity() {
        Log.d(TAG, "startListeningForIncomingCalls");
        mTimer.start();
    }

    @Override
    public void stopDetectingVoiceActivity() {
        Log.d(TAG, "startListeningForIncomingCalls");
        mTimer.stop();
    }

    @Override
    public boolean isVoiceActivityDetectionSessionActive() {
        Log.d(TAG, "isVoiceActivityDetectionSessionActive");
        return true;
    }
}
