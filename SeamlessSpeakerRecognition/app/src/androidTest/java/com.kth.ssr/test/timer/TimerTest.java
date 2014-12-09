package com.kth.ssr.tests;

import com.kth.ssr.utils.time.Timestamp;
import com.kth.ssr.utils.timer.Timer;
import com.kth.ssr.utils.timer.TimerListener;

import junit.framework.TestCase;

import org.junit.Test;

public class TimerTest extends TestCase {

    private static final String TAG = TimerTest.class.getCanonicalName();
    private Timer mTimer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        TimerListener holder = new TimerListener() {
            public static final String TAG = "TimerHolder";

            @Override
            public void onTimerExpired() {
                System.out.println("timerExpired! at" + Timestamp.nowAsTrimmedString());
            }
        };
        mTimer = new Timer(holder);
    }

    @Test
    public void testSetExpiryTimeForZeroDuration() throws Exception {
        try {
            mTimer.setExpiryTime(0);
        } catch (IllegalArgumentException e) {
            if (e instanceof Exception) {

            }
        }
    }

    @Test
    public void testSetExpiryTimeForNegativeDuration() throws Exception {
        try {
            mTimer.setExpiryTime(-1);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {

            }
        }
    }


    @Test
    public void testSetExpiryTimeNormal() throws Exception {
        mTimer.setExpiryTime(1000);
    }

    public void testTimerStartedWithoutHavingSetDuration() throws Exception {
        try {
            mTimer.start();
        } catch (Exception e) {
            if (e instanceof IllegalStateException) {

            }
        }
    }

    @Test
    public void testStart() throws Exception {
        mTimer.setExpiryTime(1000);
        mTimer.start();
        System.out.println("starting at " + Timestamp.nowAsTrimmedString());
    }

    @Test
    public void testSetExpiryTimeAndStart() throws Exception {

    }

    @Test
    public void testResetTimer() throws Exception {

    }

    @Test
    public void testHandleMessage() throws Exception {

    }
}