package com.kth.ssr.utils.timer;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by argychatzi on 9/8/14.
 */
public final class Timer extends Handler {

    public static final int DEFAULT_RECORDING_DURATION = 30000; //30"
    public static final int DEFAULT_VOICE_DETECTION_INTERVAL = 600000; //10'

    private static final int MSG_TIMER_EXPIRED = 1;
    private final WeakReference<TimerListener> mTimerWeakReference;
    private int mDurationInMillis;

    public Timer(TimerListener listener, int time) {
        mTimerWeakReference = new WeakReference<TimerListener>(listener);
        mDurationInMillis = time;
    }

    /**
     * Stops any ongoing timer, sets the expiration time of the timer
     *
     * @param durationInMillis, in milliseconds
     *                          throws @exception java.lang.IllegalArgumentException if time set is below 0.
     */
    public void setExpiryTime(int durationInMillis) {
        if (durationInMillis <= 0) {
            throw new IllegalArgumentException("timer duration can't be <= 0");
        }
        removeMessages(MSG_TIMER_EXPIRED);
        mDurationInMillis = durationInMillis;
    }

    /**
     * Starts an already configured timer.
     * If no duration is set, the default duration is used.
     */
    public void start() {
        sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, mDurationInMillis);
    }

    /**
     * Stops the timer
     */
    public void stop() {
        removeMessages(MSG_TIMER_EXPIRED);
    }

    /**
     * Restarts the timer
     */
    public void restart() {
        removeMessages(MSG_TIMER_EXPIRED);
        start();
    }

    /**
     * Stops any ongoing timer, sets the expiration time and starts the timer
     *
     * @param durationInMillis, in milliseconds
     *                          throws  @exception java.lang.IllegalArgumentException if time set is below 0.
     */
    public void setExpiryTimeAndStart(int durationInMillis) {
        setExpiryTime(durationInMillis);
        start();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if (msg.what == MSG_TIMER_EXPIRED) {
            TimerListener timerListener = mTimerWeakReference.get();
            if (timerListener != null) {
                timerListener.onTimerExpired(this);
            }
        }
    }

    public boolean isStarted() {
        return hasMessages(MSG_TIMER_EXPIRED);
    }
}
