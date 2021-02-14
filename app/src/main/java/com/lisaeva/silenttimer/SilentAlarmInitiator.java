package com.lisaeva.silenttimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

public class SilentAlarmInitiator extends BroadcastReceiver {
    public static final String ACTION_START = "com.lisaeva.silenttimer.action_start";
    public static final String ACTION_STOP = "com.lisaeva.silenttimer.action_stop";
    public static final String ACTION_ACTIVATE = "com.lisaeva.silenttimer.action_activate";
    public static final String ACTION_TERMINATE = "com.lisaeva.silenttimer.action_terminate";

    private static final String PREFERENCES_BROADCAST_KEY = "com.lisaeva.silenttimer.broadcast_preferences_key";
    private static final String PREFERENCES_SAVED_ID = "com.lisaeva.silenttimer.saved_id";
    private static final String PREFERENCES_DEFAULT_ID = "com.lisaeva.silenttimer.default_id";
    private static final String PREFERENCES_REQUEST_CODE = "com.lisaeva.silenttimer.request_code";

    private static final String EXTRA_REQUEST_CODE = "com.lisaeva.silenttimer.extra_request_code";
    public static final String EXTRA_UUID = "com.lisaeva.silenttimer.alarm_id";
    public static final String EXTRA_REPEAT = "com.lisaeva.silenttimer.alarm_repeat";
    public static final String EXTRA_START_TIME = "com.lisaeva.silenttimer.alarm_start";
    public static final String EXTRA_END_TIME = "com.lisaeva.silenttimer.alarm_end";
    public static final String EXTRA_START_TIMES = "com.lisaeva.silenttimer.alarm_repeat_start";
    public static final String EXTRA_END_TIMES = "com.lisaeva.silenttimer.alarm_repeat_start";

    public static final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy, e, HH:mm");

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceEditor;
    private Context context;

    private Queue<Interval> intervals = new PriorityQueue<Interval>(10, (i1,i2) -> i1.getEnd().compareTo(i2.getEnd()));

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context.getApplicationContext();

        String action = intent.getAction();
        String id = intent.getStringExtra(EXTRA_UUID);

        preferences = context.getSharedPreferences(PREFERENCES_BROADCAST_KEY, Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
        String savedID = preferences.getString(PREFERENCES_SAVED_ID, PREFERENCES_DEFAULT_ID);

        if (action.equals(ACTION_START)) {
            start(intent);
        } else if (action.equals(ACTION_STOP)) {
            if (id.equals(savedID) || savedID.equals(PREFERENCES_DEFAULT_ID)) {
                stop(intent, true);
            } else stop(intent, false);
        } else if (action.equals(ACTION_ACTIVATE)) {
            setup(intent);
        } else if (action.equals(ACTION_TERMINATE)){
            terminate(intent);
        }
    }

    private void setup(Intent intent) {
        Log.d("setup()", " : started");

        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);

        if (repeat)roll(intent);

        DateTime startTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_START_TIME));
        DateTime endTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_END_TIME));

        if (endTime.isBefore(DateTime.now())) {
            startTime = startTime.plusDays(1);
            endTime = endTime.plusDays(1);

            intent.putExtra(EXTRA_START_TIME, formatter.print(startTime));
            intent.putExtra(EXTRA_END_TIME, formatter.print(endTime));
        }

        long millis = wait(intent);
        if (millis == 0) {
            start(intent);
        } else {
            send(intent, millis);
        }
        Log.d("setup()", " : ended: " + millis);
    }

    public void start(Intent intent) {
        Log.d("start()", " : started");

        turnOffAudio();
        preferenceEditor.putString(PREFERENCES_SAVED_ID, intent.getStringExtra(EXTRA_UUID)).commit();
        changeStartState(intent.getStringExtra(EXTRA_UUID), true);

        DateTime endTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_END_TIME));
        long millis = new Interval(DateTime.now(), endTime).toDurationMillis();

        intent.setAction(ACTION_STOP);
        send(intent, millis);

        Log.d("start()", " : ended: " + millis);
    }

    public long wait(Intent intent) {
        Log.d("wait()", " : started");
        boolean started = false;
        long millis = 0;

        DateTime startTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_START_TIME));

        if (startTime.isAfter(DateTime.now())) {
            millis = new Interval(DateTime.now(), startTime).toDurationMillis();
            started = true;
        }

        Log.d("wait()", " : ended: " + millis);

        changeStartState(intent.getStringExtra(EXTRA_UUID), started);
        intent.setAction(ACTION_START);
        return millis;
    }

    private void stop(Intent intent, boolean turnOnAudio) {
        Log.d("stop()", " : started");

        if (turnOnAudio) {
            turnOnAudio();
            preferenceEditor.putString(PREFERENCES_SAVED_ID, PREFERENCES_DEFAULT_ID).commit();
        }

        changeStartState(intent.getStringExtra(EXTRA_UUID), false);

        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);

        if (repeat) {
            roll(intent);
            send(intent, wait(intent));
        } else {
            deactivate(intent);
        }

        Log.d("stop()", " : ended");
    }

    private void deactivate(Intent intent) {
        // turn active to false.
        AlarmList alarmList = AlarmList.get(context);
        alarmList.deactivate(intent.getStringExtra(EXTRA_UUID));
    }

    private void terminate(Intent intent) {
        String uri = getHandleAndDeactivate(intent);
        if (uri != null) {
            Intent handle = null;
            try { handle = Intent.parseUri(uri, 0); }
            catch (URISyntaxException e) { e.printStackTrace(); }
            PendingIntent pIntent = PendingIntent.getBroadcast(context, handle.getIntExtra(EXTRA_REQUEST_CODE, -1), handle, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pIntent);
        }
    }

    private void send(Intent intent, long millis) {
        millis += System.currentTimeMillis();
        int requestCode = generateRequestCode();

        intent.setClass(context, SilentAlarmInitiator.class);
        intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
        saveHandle(intent);

        PendingIntent pIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pIntent);
        }
    }

    private Intent roll(Intent intent) {
        DateTime startTime;
        DateTime endTime;

        ArrayList<String> startTimesList = intent.getStringArrayListExtra(EXTRA_START_TIMES);
        ArrayList<String> endTimesList = intent.getStringArrayListExtra(EXTRA_END_TIMES);

        for (Iterator<String> i = startTimesList.iterator(), j = endTimesList.iterator(); i.hasNext();) {
            intervals.add(new Interval (formatter.parseDateTime(i.next()), formatter.parseDateTime(j.next())));
        }

        while (intervals.peek().getEnd().isBeforeNow()) {
            Interval interval = intervals.poll();
            Interval forward = new Interval (interval.getStart().plusDays(7), interval.getEnd().plusDays(7));
            startTimesList.remove(formatter.print(interval.getStart()));
            endTimesList.remove(formatter.print(interval.getEnd()));
            startTimesList.add(formatter.print(forward.getStart()));
            endTimesList.add(formatter.print(forward.getEnd()));
            intervals.add(forward);
        }

        startTime = intervals.peek().getStart();
        endTime = intervals.peek().getEnd();

        intent.putExtra(EXTRA_START_TIMES, startTimesList)
                .putExtra(EXTRA_END_TIMES, endTimesList)
                .putExtra(EXTRA_START_TIME, formatter.print(startTime))
                .putExtra(EXTRA_END_TIME, formatter.print(endTime));

        return intent;
    }

    private void turnOnAudio() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    private void turnOffAudio() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    private int generateRequestCode() {
        int requestCode = preferences.getInt(PREFERENCES_REQUEST_CODE, -1);
        requestCode++;
        preferenceEditor.putInt(PREFERENCES_REQUEST_CODE, requestCode);
        preferenceEditor.commit();
        return requestCode;
    }

    // diff thread
    private void saveHandle(Intent intent) {
        String uuid = intent.getStringExtra(EXTRA_UUID);
        String uri = intent.toUri(0);
        AlarmList alarmList = AlarmList.get(context);
        alarmList.saveHandle(uuid, uri);
    }

    private String getHandleAndDeactivate(Intent intent) {
        AlarmList alarmList = AlarmList.get(context);
        return alarmList.getHandleAndDeactivate(intent.getStringExtra(EXTRA_UUID));
    }

    private void changeStartState(String uuid, boolean flag) {
        AlarmList alarmList = AlarmList.get(context);
        alarmList.setStarted(uuid, flag);
    }

}