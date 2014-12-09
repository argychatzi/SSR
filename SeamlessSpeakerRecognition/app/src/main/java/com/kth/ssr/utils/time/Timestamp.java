package com.kth.ssr.utils.time;

import android.text.format.Time;
import android.util.Log;
import android.util.TimeUtils;

import java.util.Calendar;

/**
 * Created by argychatzi on 9/5/14.
 */
public class Timestamp {

    private static final String TAG = "Timestamp";

    public static String nowAsTrimmedString(){
        String untrimmedText = Calendar.getInstance().getTime().toString();
        return untrimmedText.replace(" ", "");
    }
}
