package com.kth.ssr.voicesample;

import com.kth.ssr.voicesample.models.VoiceSample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by argychatzi on 3/23/14.
 */
public class VoiceSampleFinder {

    private static final String TAG = VoiceSampleFinder.class.getCanonicalName();

//    public VoiceSampleFinder(String pathToLookForSamples) {
//        super(pathToLookForSamples);
//    }

    public static List<VoiceSample> getSamples(String path) {
        ArrayList<VoiceSample> result = new ArrayList<VoiceSample>();

        File file = new File(path);

        File[] list = file.listFiles();

        if (list != null) {
            for (File f : list) {
                result.add(new VoiceSample(f));
            }
        }

        return result;
    }
}
