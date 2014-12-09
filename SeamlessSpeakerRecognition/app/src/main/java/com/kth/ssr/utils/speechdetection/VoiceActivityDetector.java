package com.kth.ssr.utils.speechdetection;

/**
 * Created by argychatzi on 9/27/14.
 */
public interface VoiceActivityDetector {
    public void startDetectingVoiceActivity();
    public void stopDetectingVoiceActivity();
    public boolean isVoiceActivityDetectionSessionActive();
}
