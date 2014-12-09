package com.kth.ssr.utils.recorder;

import android.media.AudioRecord;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by argychatzi on 9/28/14.
 */
public class AudioSoundRecorder implements SoundRecorder {

    private static final String AUDIO_RECORDER_FOLDER = "/SSR";
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    public static final int TMP_BUFFER_SIZE = 2048;

    private AudioRecord mAudioRecord;
    private Thread recordingThread = null;
    private String mLastStoredFile;

    public AudioSoundRecorder() {
        initAudioRecord();
    }

    private void initAudioRecord() {
        int audioSource = RecorderConfiguration.AUDIO_SOURCE;
        int sampleRateInHz = RecorderConfiguration.SAMPLE_RATE_IN_HZ;
        int channelConfig = RecorderConfiguration.CHANNEL_CONFIG;
        int audioFormat = RecorderConfiguration.AUDIO_FORMAT;

        int bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);

        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
    }

    @Override
    public void start() {
        initAudioRecord();
        mAudioRecord.startRecording();

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String tempFileName = getTempFilename();
                writeAudioDataToFile(tempFileName);
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    @Override
    public void stop() {
        if (null != mAudioRecord) {

            if (mAudioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING) {
                mAudioRecord.stop();
            }

            mAudioRecord.release();

            mAudioRecord = null;
            recordingThread = null;
        }

        String tempFileName = getTempFilename();
        String fileName = getFilename();

        copyWaveFile(tempFileName, fileName);
        deleteTempFile(tempFileName);
    }

    @Override
    public boolean isRecording() {
        boolean isRecording = false;

        if(mAudioRecord != null){
            isRecording = mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING;
        }

        return isRecording;
    }

    private String getFilename() {
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        mLastStoredFile = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
        return mLastStoredFile;
    }

    public String getLastStoredFile(){
//        return Environment.getExternalStorageDirectory().getPath() + "/adora_svitak";
        return mLastStoredFile;
    }

    private String getTempFilename() {
        String filePath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filePath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        File tempFile = new File(filePath, AUDIO_RECORDER_TEMP_FILE);

        if (tempFile.exists()) {
            tempFile.delete();
        }

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void writeAudioDataToFile(String tempFileName) {
        byte data[] = new byte[TMP_BUFFER_SIZE];
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(tempFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;

        if (null != os) {
            while (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                read = mAudioRecord.read(data, 0, TMP_BUFFER_SIZE);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTempFile(String tempFileName) {
        File file = new File((tempFileName));
        file.delete();
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RecorderConfiguration.SAMPLE_RATE_IN_HZ;
        int channels = 2;
        long byteRate = RecorderConfiguration.RECORDER_BPP * RecorderConfiguration.SAMPLE_RATE_IN_HZ * channels / 8;

        byte[] data = new byte[TMP_BUFFER_SIZE];

        try {
            FileInputStream in = new FileInputStream(inFilename);
            FileOutputStream out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            writeWaveFileHeaderToStream(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeWaveFileHeaderToStream(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = 16;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

}
