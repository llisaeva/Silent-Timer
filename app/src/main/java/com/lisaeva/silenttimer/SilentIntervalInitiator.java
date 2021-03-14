package com.lisaeva.silenttimer;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import com.lisaeva.silenttimer.model.SilentInterval;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
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

public class SilentIntervalInitiator extends BroadcastReceiver {

    public static final String ACTION_START = "com.lisaeva.silenttimer.action_start";
    public static final String ACTION_STOP = "com.lisaeva.silenttimer.action_stop";
    public static final String ACTION_ACTIVATE = "com.lisaeva.silenttimer.action_activate";
    public static final String ACTION_TERMINATE = "com.lisaeva.silenttimer.action_terminate";

    public static final String PREFERENCES_STARTED_IDS_KEY = "com.lisaeva.silenttimer.broadcast_preferences_key";
    public static final String PREFERENCES_STARTED_IDS_TAG = "com.lisaeva.silenttimer.saved_id";
    public static final String PREFERENCES_REQUEST_CODE_TAG = "com.lisaeva.silenttimer.request_code";

    public static final String PREFERENCES_SAVED_HANDLES_KEY = "com.lisaeva.silenttimer.saved_handles_key";

    public static final String EXTRA_REQUEST_CODE = "com.lisaeva.silenttimer.extra_request_code";
    public static final String EXTRA_UUID = "com.lisaeva.silenttimer.alarm_id";
    public static final String EXTRA_REPEAT = "com.lisaeva.silenttimer.alarm_repeat";
    public static final String EXTRA_START_TIME = "com.lisaeva.silenttimer.alarm_start";
    public static final String EXTRA_END_TIME = "com.lisaeva.silenttimer.alarm_end";
    public static final String EXTRA_WEEKDAYS = "com.lisaeva.silenttimer.alarm_repeat_start";

    public static final DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy, e, HH:mm");

    private SharedPreferences idPreferences;
    private SharedPreferences.Editor idPreferenceEditor;
    private Context context;
    private Set<String> ids;

    private Queue<Interval> intervals = new PriorityQueue<Interval>(10, (i1,i2) -> i1.getEnd().compareTo(i2.getEnd()));

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SII event", "onReceive() - alarm received");
        this.context = context.getApplicationContext();
        
        idPreferences = context.getSharedPreferences(PREFERENCES_STARTED_IDS_KEY, Context.MODE_PRIVATE);
        idPreferenceEditor = idPreferences.edit();
        ids = new HashSet<String>();

        for (String id : idPreferences.getStringSet(PREFERENCES_STARTED_IDS_TAG, new HashSet<String>())) {
            ids.add(new String(id));
        }
        
        route(intent);
    }

    private void route(Intent intent) {
        String action = intent.getAction();

        if(action.equals(ACTION_START)) {
            start(intent);
        } else if (action.equals(ACTION_STOP)) {
            stop(intent);
        } else if (action.equals(ACTION_ACTIVATE)) {
            setup(intent);
        } else if (action.equals(ACTION_TERMINATE)) {
            terminate(intent);
        }
    }

    // start (turn audio off)
    public void start(Intent intent) {
        Log.d("SII event", "start() started");

        turnOffAudio();

        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);

        changeStartState(intent.getStringExtra(EXTRA_UUID), true);

        DateTime endTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_END_TIME));
        long millis = new Interval(DateTime.now(), endTime).toDurationMillis();

        if (repeat) {
            intent.setAction(ACTION_STOP);
        } else {
            intent.setAction(ACTION_TERMINATE);
        }

        send(intent, millis);

        Log.d("SII event", "start() ended");
    }

    // stop (turn audio back on, if this was the only active silent interval)
    // a repeating silent interval until next start-time
    private void stop(Intent intent) {
        Log.d("SII event", "stop() started");

        String id = intent.getStringExtra(EXTRA_UUID);

        if (ids.contains(id) && ids.size() == 1) {
            turnOnAudio();
        }

        changeStartState(intent.getStringExtra(EXTRA_UUID), false);

        roll(intent);
        send(intent, getMillisBeforeStart(intent));

        Log.d("SII event", "stop() ended");
    }

    private void setup(Intent intent) {
        Log.d("SII event", "setup() started");
        roll(intent);
        
        long millis = getMillisBeforeStart(intent);
        
        if (millis == 0) {
            start(intent);
        } else {
            send(intent, millis);
        }
        Log.d("SII event", "setup ended()" + millis);
    }

    // stop an active PendingIntent
    private void terminate(Intent intent) {
        Log.d("SII event", "terminate() started");
        String id = intent.getStringExtra(EXTRA_UUID);

        if(ids.contains(id) && ids.size() == 1) {
            turnOnAudio();
        }

        changeStartState(id, false);
        cancelPendingIntent(intent);

        Log.d("SII event", "terminate() ended");
    }

    // create and send a PendingIntent
    private void send(Intent intent, long millis) {
        Log.d("SII event", "send() start");
        millis += DateTimeUtils.currentTimeMillis();

        PendingIntent pIntent = createPendingIntent(intent);

        if (pIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pIntent);
            }
        }
        Log.d("SII event", "send() ended");
    }

    // returns the time until next start-time
    // zero if start time is in the past
    public long getMillisBeforeStart(Intent intent) {
        Log.d("SII event", "wait() started");

        long millis = 0;

        DateTime startTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_START_TIME));

        Log.d("wait()", "startTime = " + intent.getStringExtra(EXTRA_START_TIME));

        if (startTime.isAfter(DateTime.now())) {
            millis = new Interval(DateTime.now(), startTime).toDurationMillis();
        }

        intent.setAction(ACTION_START);
        Log.d("SII event", "wait() ended");
        return millis;
    }

    // identifies the start and end times, based on an array of weekday dates
    private Intent roll(Intent intent) {
        Log.d("roll()", "started");
        
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);
        DateTime startTime;
        DateTime endTime;

        if (repeat) {
            intervals.clear();
            ArrayList<String> weekdaysList = intent.getStringArrayListExtra(EXTRA_WEEKDAYS);

            for (Iterator<String> i = weekdaysList.iterator(); i.hasNext();) {
                intervals.add(SilentInterval.parseInterval(i.next()));
            }

            while (intervals.peek().getEnd().isBeforeNow()) {
                Interval interval = intervals.poll();
                Interval forward = new Interval (interval.getStart().plusDays(7), interval.getEnd().plusDays(7));
                weekdaysList.remove(SilentInterval.formatInterval(interval));
                weekdaysList.add(SilentInterval.formatInterval(forward));
                intervals.add(forward);
            }

            startTime = intervals.peek().getStart();
            endTime = intervals.peek().getEnd();

            intent.putExtra(EXTRA_WEEKDAYS, weekdaysList);
        } else {
            startTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_START_TIME));
            endTime = formatter.parseDateTime(intent.getStringExtra(EXTRA_END_TIME));
            
            if (endTime.isBefore(DateTime.now())) {
                startTime = startTime.plusDays(1);
                endTime = endTime.plusDays(1);
            }
        }
        
        intent.putExtra(EXTRA_START_TIME, formatter.print(startTime))
                .putExtra(EXTRA_END_TIME, formatter.print(endTime));
        Log.d("roll()", "ended");
        return intent;
    }

    private void turnOnAudio() {
        if (checkPermissions()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Log.d("turnOnAudio()", "audio turned on");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_UNMUTE, 0);
            }

        }
    }

    private void turnOffAudio() {
        if (checkPermissions()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Log.d("turnOffAudio()", "audio turned off");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_MUTE, 0);
            }
        }
    }

    private PendingIntent createPendingIntent(Intent intent) {
        SharedPreferences handlesPreferences = context.getSharedPreferences(PREFERENCES_SAVED_HANDLES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor handlesPreferenceEditor = handlesPreferences.edit();

        String uuid = intent.getStringExtra(EXTRA_UUID);
        if (handlesPreferences.contains(uuid))cancelPendingIntent(intent);

        int requestCode = generateRequestCode();

        Intent handle = new Intent(intent.getAction());
        handle.setClass(context, SilentIntervalInitiator.class);
        handle.putExtra(EXTRA_REQUEST_CODE, requestCode);
        String uri = handle.toUri(0);

        handlesPreferenceEditor.putString(uuid, uri).commit();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    private void cancelPendingIntent(Intent intent) {
        String uuid = intent.getStringExtra(EXTRA_UUID);
        SharedPreferences handlesPreferences = context.getSharedPreferences(PREFERENCES_SAVED_HANDLES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor handlesPreferenceEditor = handlesPreferences.edit();

        String uri = handlesPreferences.getString(uuid, null);

        if (uri != null) {
            Intent handle = null;
            try { handle = Intent.parseUri(uri, 0); }
            catch (URISyntaxException e) { e.printStackTrace(); }
            PendingIntent pIntent = PendingIntent.getBroadcast(context, handle.getIntExtra(EXTRA_REQUEST_CODE, -1), handle, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pIntent);
            pIntent.cancel();
        }
        handlesPreferenceEditor.remove(uuid).commit();
    }

    private void changeStartState(String uuid, boolean flag) {

        if (flag)ids.add(uuid);
        else ids.remove(uuid);
        
        idPreferenceEditor.putStringSet(PREFERENCES_STARTED_IDS_TAG, ids).commit();
    }

    private boolean checkPermissions(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            return false;
        } return true;
    }

    private int generateRequestCode() {
        int requestCode = idPreferences.getInt(PREFERENCES_REQUEST_CODE_TAG, -1);
        requestCode++;
        idPreferenceEditor.putInt(PREFERENCES_REQUEST_CODE_TAG, requestCode).commit();
        return requestCode;
    }

}