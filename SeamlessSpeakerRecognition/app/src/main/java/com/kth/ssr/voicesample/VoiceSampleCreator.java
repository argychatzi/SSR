package com.kth.ssr.voicesample;

import com.kth.ssr.voicesample.models.VoiceSample;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by argychatzi on 3/29/14.
 */
public class VoiceSampleCreator {

    public static VoiceSample createVoiceSample(File f, byte b) {
        VoiceSample result = null;

        try {
            if (f.canWrite()) {
                OutputStream outStream = new FileOutputStream(f);
                DataOutputStream dOutStream = new DataOutputStream(outStream);
                dOutStream.write(b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}