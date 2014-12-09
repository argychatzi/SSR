package com.kth.ssr.utils.recorder;

import android.media.AudioFormat;
import android.media.MediaRecorder;

/**
 * Created by argychatzi on 9/28/14.
 */
public class RecorderConfiguration {

    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int OUTPUT_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    public static final int AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;

    public static final int SAMPLE_RATE_IN_HZ = 44100;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public static final int RECORDER_BPP = 16;
}
