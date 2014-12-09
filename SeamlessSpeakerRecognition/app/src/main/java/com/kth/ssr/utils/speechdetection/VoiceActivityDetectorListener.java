package com.kth.ssr.utils.speechdetection;

/**
 * Created by georgios.savvidis on 09/09/14.
 */
public interface VoiceActivityDetectorListener {
    void onVoiceActivityDetected();
    void onNoVoiceActivityDetected();
}
