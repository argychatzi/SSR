package com.kth.ssr.utils.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kth.ssr.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Class that creates an alarm that will wake up and shut down
 * the sample collecting service. Note that due to android
 * restrictions around battery consumption and wake locks, the
 * pending intent associated with the alarm manager may take
 * some time before it gets received by the BroadcastReceiver
 * (AlarmReceiver)
 * <p/>
 *
 * Created by georgios.savvidis on 28/08/14.
 */
public class AlarmScheduler {
    private static final int DEFAULT_ALARM_TIME_STOP_HOUR = 23;
    private static final int DEFAULT_ALARM_TIME_STOP_MIN = 00;
    private static final int DEFAULT_ALARM_TIME_START_HOUR = 8;
    private static final int DEFAULT_ALARM_TIME_START_MIN = 0;

    private static final int ALARM_REPEAT_INTERVAL_IN_HOURS = 24;

    private static final String ALARM_INTENT_ACTION_VALUE = "com.kth.ssr.alarm";
    private static final int START_ALARM_ID = 10;
    private static final int STOP_ALARM_ID = 20;


    private enum AlarmType {
        START, STOP;
    }

    /**
     * This alarm is used to notify the app to stop listening for voice input.
     */
    public static void setStopAlarm(Context context) {
        setAlarm(context, AlarmType.STOP);
    }

    /**
     * This alarm is used to notify the app to start listening for voice input again.
     */
    public static void setStartAlarm(Context context) {
        setAlarm(context, AlarmType.START);
    }

    private static void setAlarm(Context context, AlarmType alarmType ){

        Time alarmTime = getTime(context, alarmType);
        setRepeatingAlarm(context, alarmTime, AlarmType.START);
    }

    private static Time getTime(Context context, AlarmType alarmType){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String defaultTime;
        String timePreferenceKey;

        if(alarmType == AlarmType.STOP){
            defaultTime = Time.getAsString(DEFAULT_ALARM_TIME_STOP_HOUR, DEFAULT_ALARM_TIME_STOP_MIN);
            timePreferenceKey = context.getString(R.string.sharedPreferencesStopTime);
        } else {
            defaultTime = Time.getAsString(DEFAULT_ALARM_TIME_START_HOUR, DEFAULT_ALARM_TIME_START_MIN);
            timePreferenceKey = context.getString(R.string.sharedPreferencesStartTime);
        }

        String timePreferenceValue = preferences.getString(timePreferenceKey,defaultTime);

        return new Time(timePreferenceValue);
    }

    private static void setRepeatingAlarm(Context context, Time time, AlarmType alarmType) {
        if (time.getHourAsInt() < 0 || time.getMinAsInt() < 0) {
            throw new IllegalArgumentException("Time " + time.getHourAsInt() + ":" + time.getMinAsInt() + "is an invalid alarm hour!");
        } else {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            int type = AlarmManager.RTC_WAKEUP;
            long alarmTimeInMillis = createAlarmTimeInMillis(time);
            long alarmRepeatInterval = TimeUnit.HOURS.toMillis(ALARM_REPEAT_INTERVAL_IN_HOURS);
            PendingIntent alarmPendingIntent = createAlarmAwakeningPendingIntent(context, alarmType);

            alarmManager.setRepeating(type, alarmTimeInMillis, alarmRepeatInterval, alarmPendingIntent);
        }
    }

    private static long createAlarmTimeInMillis(Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHourAsInt());
        calendar.set(Calendar.MINUTE, time.getMinAsInt());
        return calendar.getTimeInMillis();
    }

    private static PendingIntent createAlarmAwakeningPendingIntent(Context context, AlarmType alarmType) {
        Intent intent = new Intent();
        intent.setAction(ALARM_INTENT_ACTION_VALUE);

        if (alarmType == AlarmType.START) {
            intent.setFlags(START_ALARM_ID);
        } else{
            intent.setFlags(STOP_ALARM_ID);
        }

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static String getStartAlarmFormatted(Context context){
        Time time = getTime(context, AlarmType.START);
        return getAlarmFormatted(time);
    }

    public static String getStopAlarmFormatted(Context context){
        Time time = getTime(context, AlarmType.STOP);
        return getAlarmFormatted(time);
    }

    private static String getAlarmFormatted(Time time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHourAsInt());
        calendar.set(Calendar.MINUTE, time.getMinAsInt());

        return formatter.format(calendar.getTime());
    }

    private static class Time {

        private int hour;
        private int min;

        public Time(String stringRepresentation){
            String[] strings = stringRepresentation.split(":");
            hour = Integer.parseInt(strings[0]);
            min = Integer.parseInt(strings[1]);
        }

        public int getHourAsInt() {
            return hour;
        }

        public int getMinAsInt() {
            return min;
        }

        public static String getAsString(int hour, int min){
            return String.valueOf(hour) + ":" + String.valueOf(min);
        }

    }

}
