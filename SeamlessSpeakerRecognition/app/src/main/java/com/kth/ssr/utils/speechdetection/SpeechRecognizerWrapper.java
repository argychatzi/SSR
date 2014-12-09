package com.kth.ssr.utils.speechdetection;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;


/**
 *
 * This is a wrapper class for SpeechRecognition. Due to bugs related to RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
 * this class implements an internal mechanism that allows to consider the speech as completed when during a listening interval there was no
 * speech input at all.
 *
 * Implementations of this class, should use the {@link VoiceActivityDetectorListener SpeechRecognitionListener}
 * interface for listening to the beginning and end of speech.
 *
 *
 * Created by georgios.savvidis on 08/09/14.
 */
public class SpeechRecognizerWrapper implements RecognitionListener, VoiceActivityDetector {
    private static final String TAG = SpeechRecognizerWrapper.class.getCanonicalName();

    private SpeechRecognizer mSpeechRecognizer;
    private VoiceActivityDetectorListener mVoiceListener;
    private AudioManager mAudioManager;

    private boolean mIsListening;

    public SpeechRecognizerWrapper(Context context, VoiceActivityDetectorListener voiceListener) {
        mVoiceListener = voiceListener;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mSpeechRecognizer.setRecognitionListener(this);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void startDetectingVoiceActivity(){
        Log.d(TAG, "startDetectingVoiceActivity");
//        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizer.startListening(intent);
    }

    @Override
    public void stopDetectingVoiceActivity() {
        Log.d(TAG, "stopDetectingVoiceActivity");
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.cancel();
        mIsListening = false;
    }

    @Override
    public boolean isVoiceActivityDetectionSessionActive() {
        return mIsListening;
    }

    public boolean isListening(){
        return mIsListening;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "OnReadyForSpeech");

        // Unmute the previously muted stream.
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        mIsListening = true;
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "OnBeginOfSpeech");
        stopDetectingVoiceActivity();
        if(mVoiceListener != null){
            //TODO find a better way for this
            Handler hanlder = new Handler();
            hanlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVoiceListener.onVoiceActivityDetected();
                }
            }, 1000);
        }
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "OnError: " + error);

        if(error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT && mVoiceListener != null){
            mVoiceListener.onNoVoiceActivityDetected();
        }

        mIsListening = false;
    }

    @Override
    public void onResults(Bundle results) {
        Log.d(TAG, "OnResults");
    }


    @Override
    public void onRmsChanged(float rmsdB) {
        //Do nothing
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "OnEndOfSpeech");
        //Do nothing
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "OnBufferReceived");
        //Do nothing
    }
    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "OnPartialResults");
        //Do nothing
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "OnEvent");
        //Do nothing
    }
}