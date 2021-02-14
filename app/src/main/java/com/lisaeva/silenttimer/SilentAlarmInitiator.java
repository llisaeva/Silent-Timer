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
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SilentAlarmInitiator extends BroadcastReceiver {
    public static final String ACTION_START = "com.lisaeva.silenttimer.action_start";
    public static final String ACTION_STOP = "com.lisaeva.silenttimer.action_stop";
    public static final String ACTION_ACTIVATE = "com.lisaeva.silenttimer.action_activate";
    public static final String ACTION_DEACTIVATE = "com.lisaeva.silenttimer.action_deactivate";
    private static final String PREFERENCES_BROADCAST_KEY = "com.lisaeva.silenttimer.broadcast_preferences_key";
    private static final String PREFERENCES_SAVED_ID = "com.lisaeva.silenttimer.saved_id";
    private static final String PREFERENCES_DEFAULT_ID = "com.lisaeva.silenttimer.default_id";
    private static final String PREFERENCES_DEFAULT_REQUEST_CODE = "com.lisaeva.silenttimer.request_code";
    private static final String PREFERENCES_DEACTIVATE_LIST = "com.lisaeva.silenttimer.deactivate_list";

    private static final String EXTRA_REQUEST_CODE = "com.lisaeva.silenttimer.extra_request_code";
    public static final String EXTRA_UUID = "com.lisaeva.silenttimer.alarm_id";
    public static final String EXTRA_REPEAT = "com.lisaeva.silenttimer.alarm_repeat";
    public static final String EXTRA_ACTIVATE = "com.lisaeva.silenttimer.alarm_active";
    public static final String EXTRA_STARTED = "com.lisaeva.silenttimer.alarm_started";
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
        this.context = context;
        
        String action = intent.getAction();
        String id = intent.getStringExtra(EXTRA_UUID);
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);

        preferences = context.getSharedPreferences(PREFERENCES_BROADCAST_KEY, Context.MODE_PRIVATE);
        preferenceEditor = preferences.edit();
        String savedID = preferences.getString(PREFERENCES_SAVED_ID, PREFERENCES_DEFAULT_ID);

        if (action.equals(ACTION_START)) {
            turnOffAudio(context);
            long millis = start(intent);
            send(intent, context, millis);
            preferenceEditor.putString(PREFERENCES_SAVED_ID, id);
        } else if (action.equals(ACTION_STOP)) {
            if (id.equals(savedID) || savedID.equals(PREFERENCES_DEFAULT_ID)) {
                turnOnAudio(context);
                long millis = stop(intent);
                if (millis > 0)send(intent, context, millis);
                preferenceEditor.putString(PREFERENCES_SAVED_ID, PREFERENCES_DEFAULT_ID);
            } else {
                if(repeat) {
                    long millis = activate(intent);
                    send(intent, context, millis);
                    boolean started = intent.getBooleanExtra(EXTRA_STARTED, false);
                    if (started) {
                        turnOffAudio(context);
                        preferenceEditor.putString(PREFERENCES_SAVED_ID, id);
                    }
                } else {
                    // TODO: Deactivate the actual alarm in db: active = false (when app starts)
                    // turnOff(intent);
                    try {
                        deactivateSilentAlarm(intent, context);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        // TODO: something or swallow
                    }
                    // clean up prefs -- here or ^ NULL REF RISK
                    preferenceEditor.remove(intent.getStringExtra(EXTRA_UUID));
                }
            }
        } else if (action.equals(ACTION_ACTIVATE)) {
            long millis = activate(intent);
            send(intent, context, millis);
            boolean started = intent.getBooleanExtra(EXTRA_STARTED, false);
            if (started) {
                turnOffAudio(context);
                preferenceEditor.putString(PREFERENCES_SAVED_ID, id);
            }
        } else if (action.equals(ACTION_DEACTIVATE)) {
            try {
                deactivateSilentAlarm(intent, context);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            // TODO: throw exception or swallow
        }
        preferenceEditor.commit();
    }

    private long activate(Intent intent) {
        Log.d("activate()", " : started");

        boolean active = true;
        boolean started = false;
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);

        if (repeat)intent = roll(intent);

        String startCode = intent.getStringExtra(EXTRA_START_TIME);
        String endCode = intent.getStringExtra(EXTRA_END_TIME);

        DateTime startTime = formatter.parseDateTime(startCode);
        DateTime endTime = formatter.parseDateTime(endCode);

        Duration duration = null;

        if (startTime.isAfterNow()) {
            duration = new Interval(DateTime.now(), startTime).toDuration();
            intent.setAction(ACTION_START);
        } else if (startTime.isBeforeNow() && endTime.isAfterNow()) {
            duration = new Interval(DateTime.now(), endTime).toDuration();
            if (repeat)intent.setAction(ACTION_STOP);
            else intent.setAction(ACTION_DEACTIVATE);
            started = true;
        } else if (startTime.isBeforeNow() && endTime.isBeforeNow()) {
            startTime = startTime.plusDays(1);
            endTime = endTime.plusDays(1);
            intent.putExtra(EXTRA_START_TIME, formatter.print(startTime));
            intent.putExtra(EXTRA_END_TIME, formatter.print(endTime));
            intent.setAction(ACTION_START);
            duration = new Interval(DateTime.now(), startTime).toDuration();
        } else {
            //  TODO: THROW EXCEPTION
        }

        long millis = duration.getMillis();

        intent.putExtra(EXTRA_ACTIVATE, active)
                .putExtra(EXTRA_STARTED, started);

        Log.d("activate()", " : ended: " + millis);

        return millis;
    }

    public long start(Intent intent) {
        Log.d("start()", " : started");

        boolean started = true;
        DateTime end = formatter.parseDateTime(intent.getStringExtra(EXTRA_END_TIME));
        Duration duration = new Interval(DateTime.now(), end).toDuration();
        long millis = duration.getMillis();

        intent.setAction(ACTION_STOP);
        intent.putExtra(EXTRA_STARTED, started);

        Log.d("start()", " : ended: " + millis);

        return millis;
    }

    public long stop(Intent intent) {
        boolean active = false;
        boolean started = false;
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);
        long millis = 0;

        if (repeat) {
            intent = roll(intent);
            active = true;

            String startCode = intent.getStringExtra(EXTRA_START_TIME);
            Duration duration = new Interval(DateTime.now(), formatter.parseDateTime(startCode)).toDuration();
            millis = duration.getMillis();
            intent.setAction(ACTION_START);
        }
        intent.putExtra(EXTRA_STARTED, started)
                .putExtra(EXTRA_ACTIVATE, active);
        return millis;
    }

    private void turnOnAudio(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    private void turnOffAudio(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    private void deactivateSilentAlarm(Intent intent, Context context) throws URISyntaxException {
        // TODO: CANCEL PENDINGINTENT

        Log.d("deactivate()", " : started");

        String id = intent.getStringExtra(EXTRA_UUID);
        String uri = preferences.getString(id,null);
        if (uri != null) {
            Intent recovered = Intent.parseUri(uri,0);
            int requestCode = recovered.getIntExtra(EXTRA_REQUEST_CODE, -1);  // TODO if -1 -> exception
            PendingIntent pIntent = PendingIntent.getActivity(context, requestCode, recovered, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pIntent);
            preferenceEditor.remove(id);
            Set<String> markedToDeactivate = preferences.getStringSet(PREFERENCES_DEACTIVATE_LIST, new HashSet<String>());
            markedToDeactivate.add(id);
            preferenceEditor.putStringSet(PREFERENCES_DEACTIVATE_LIST, markedToDeactivate);
        }

        Log.d("deactivate()", " : ended");

        preferenceEditor.commit();
    }

    private void send(Intent intent, Context context, long millis) {
        millis += System.currentTimeMillis();

        context = context.getApplicationContext();
        intent.setClass(context, SilentAlarmInitiator.class);
        int requestCode = preferences.getInt(PREFERENCES_DEFAULT_REQUEST_CODE, -1);
        intent.putExtra(EXTRA_REQUEST_CODE, ++requestCode);

        String uri = intent.toUri(0);
        String id = intent.getStringExtra(EXTRA_UUID);

        preferenceEditor.putInt(PREFERENCES_DEFAULT_REQUEST_CODE, requestCode);
        preferenceEditor.putString(id,uri);

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
}