package com.lisaeva.silenttimer;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * Receives broadcast intents that represent SilentIntervals. Intents contain the information
 * necessary to calculate the time to mute or un-mute audio. Incoming intents have one of four
 * actions: ACTION_START, ACTION_STOP, ACTION_ACTIVATE, ACTION_DEACTIVATE, which identify the
 * stage of a silent interval.
 */
public class SilentIntervalReceiver extends BroadcastReceiver {

    public static final String ACTION_START = "com.lisaeva.silenttimer.action_start";
    public static final String ACTION_STOP = "com.lisaeva.silenttimer.action_stop";
    public static final String ACTION_ACTIVATE = "com.lisaeva.silenttimer.action_activate";
    public static final String ACTION_DEACTIVATE = "com.lisaeva.silenttimer.action_deactivate";

    public static final String EXTRA_REQUEST_CODE = "com.lisaeva.silenttimer.extra_request_code";
    public static final String EXTRA_UUID = "com.lisaeva.silenttimer.extra_uuid";
    private static final String EXTRA_START = "com.lisaeva.silenttimer.extra_start";
    private static final String EXTRA_END = "com.lisaeva.silenttimer.extra_end";
    private static final String EXTRA_REPEAT = "com.lisaeva.silenttimer.extra_repeat";
    private static final String EXTRA_WEEKDAYS = "com.lisaeva.silenttimer.extra_weekdays";

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy, e, HH:mm");
    private SharedPreferenceUtil mSharedPreferenceUtil;
    private Context mContext;
    private Set<String> mStartedUUIDs;
    private Queue<Interval> mIntervalQueue = new PriorityQueue<Interval>(10, (i1, i2) -> i1.getEnd().compareTo(i2.getEnd()));

    /**
     * Creates a Bundle from a SilentInterval, that can be attached to an intent, and received by
     * this class when sent. The bundle contains: the SilentInterval's UUID, start-time, end-time,
     * repeat flag, and (if repeating) a string array of weekday dates.
     */
    public static Bundle bundle(SilentInterval interval) {
        String startString = interval.getStartTime();
        String endString = interval.getEndTime();
        String weekCode = interval.getWeekdays();
        if (startString == null || endString == null || weekCode == null) return null;

        Bundle bundle = new Bundle();
        DateTime startDate = DateTime.now();
        DateTime endDate = DateTime.now();
        DateTime dif;

        startDate = startDate.withFields(SilentInterval.FORMATTER.parseLocalTime(startString));
        endDate = endDate.withFields(SilentInterval.FORMATTER.parseLocalTime(endString));

        int offset = 0;
        if (startDate.isAfter(endDate)) {
            offset = 1;
            endDate = endDate.plusDays(offset);
        }

        dif = endDate.minus(startDate.getMillis());

        bundle.putString(EXTRA_UUID, interval.getUuid());
        bundle.putBoolean(EXTRA_REPEAT, interval.isRepeat());

        if (interval.isRepeat()) {
            List<SilentInterval.Code> repeatDays = SilentInterval.Code.parseWeekString(weekCode);
            ArrayList<String> repeatCodes = new ArrayList<>(repeatDays.size());

            for (SilentInterval.Code weekday : repeatDays) {
                DateTime temp = startDate.withDayOfWeek(weekday.jodaTimeConstant());
                String weekdayCode = formatInterval(temp, temp.plus(dif.getMillis()));
                repeatCodes.add(weekdayCode);
            }
            bundle.putStringArrayList(EXTRA_WEEKDAYS, repeatCodes);
        } else {
            bundle.putString(EXTRA_START, FORMATTER.print(startDate));
            bundle.putString(EXTRA_END, FORMATTER.print(endDate));
        }
        return bundle;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context.getApplicationContext();
        this.mSharedPreferenceUtil = new SharedPreferenceUtil(this.mContext);
        this.mStartedUUIDs = mSharedPreferenceUtil.getStartedIntervalUUIDs();
        route(intent);
    }

    /**
     * Directs an incoming intent based on its action.
     */
    private void route(Intent intent) {
        String action = intent.getAction();

        if      (action.equals(ACTION_START))      start(intent);
        else if (action.equals(ACTION_STOP))       stop(intent);
        else if (action.equals(ACTION_ACTIVATE))   setup(intent);
        else if (action.equals(ACTION_DEACTIVATE)) deactivate(intent);
        else return;
    }

    /**
     * Starts a silent interval by turning off audio. Records that the silent interval was started
     * in a file by calling changeStartState(). Calculates the milliseconds until the interval
     * ends, and sets the action to the next action (which is either stop or deactivate, depending
     * on whether the interval repeats).
     */
    public void start(Intent intent) {
        turnOffAudio();
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);
        changeStartState(intent.getStringExtra(EXTRA_UUID), true);
        DateTime endTime = FORMATTER.parseDateTime(intent.getStringExtra(EXTRA_END));
        long millis = new Interval(DateTime.now(), endTime).toDurationMillis();

        if (repeat)intent.setAction(ACTION_STOP);
        else intent.setAction(ACTION_DEACTIVATE);

        send(intent, millis);
    }

    /**
     * Stops a repeating silent interval, and turns on audio if the stopped interval was the only
     * one that was muting the system. Records that the silent interval was stopped in a file by
     * calling changeStartState(). Calls roll() to set up the time to the next start action.
     */
    private void stop(Intent intent) {
        String id = intent.getStringExtra(EXTRA_UUID);
        if (mStartedUUIDs.contains(id) && mStartedUUIDs.size() == 1)turnOnAudio();
        changeStartState(intent.getStringExtra(EXTRA_UUID), false);
        roll(intent);
        send(intent, getMillisBeforeStart(intent));
    }

    /**
     * Sets up a silent interval that was activated. There can be three scenarios: the interval's
     * start-time and end-time are before the current time, after the current time, or in between
     * (start-time is before the current time, and end-time is after). If the interval was in
     * between, it starts immediately. Otherwise, the interval is started on the next clock
     * start-time.
     */
    private void setup(Intent intent) {
        roll(intent);
        long millis = getMillisBeforeStart(intent);
        if (millis == 0)start(intent);
        else send(intent, millis);
    }

    /**
     * Deactivates either a non-repeating silent interval after it is complete, or an interval
     * that was turned off by the user. Turns audio on if the interval was the only one that
     * was muting the system. Records that the silent interval was stopped in a file by calling
     * changeStartState(). Cancels the PendingIntent associated with the silent interval.
     */
    private void deactivate(Intent intent) {
        String id = intent.getStringExtra(EXTRA_UUID);
        if(mStartedUUIDs.contains(id) && mStartedUUIDs.size() == 1)turnOnAudio();
        changeStartState(id, false);
        cancelPendingIntent(intent);
    }

    /**
     * Registers a PendingIntent associated with this interval in the system (via AlarmManager),
     * to be broadcast on the calculated millisecond of the system clock.
     */
    private void send(Intent intent, long millis) {
        millis += DateTimeUtils.currentTimeMillis();
        PendingIntent pendingIntent = createPendingIntent(intent);

        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }
    }

    /**
     * Calculates and returns the time until the next start-time (in milliseconds) of the
     * parameter intent. Sets the action to ACTION_START.
     */
    public long getMillisBeforeStart(Intent intent) {
        long millis = 0;
        DateTime startTime = FORMATTER.parseDateTime(intent.getStringExtra(EXTRA_START));
        if (startTime.isAfter(DateTime.now()))
            millis = new Interval(DateTime.now(), startTime).toDurationMillis();
        intent.setAction(ACTION_START);
        return millis;
    }

    /**
     * Calculates the start and end dates, based on an array of weekdays (for repeating intervals),
     * or the start-time and end-time (for non repeating intervals).
     */
    private Intent roll(Intent intent) {
        boolean repeat = intent.getBooleanExtra(EXTRA_REPEAT, false);
        DateTime startTime;
        DateTime endTime;

        if (repeat) {
            mIntervalQueue.clear();
            ArrayList<String> weekdaysList = intent.getStringArrayListExtra(EXTRA_WEEKDAYS);

            for (Iterator<String> i = weekdaysList.iterator(); i.hasNext();) {
                mIntervalQueue.add(parseInterval(i.next()));
            }

            for (int i = 0; i< weekdaysList.size(); i++) {
                Log.d("weekdaysList", weekdaysList.get(i));
            }

            while (mIntervalQueue.peek().getEnd().isBeforeNow()) {
                Interval interval = mIntervalQueue.poll();
                Interval forward = new Interval (interval.getStart().plusDays(7), interval.getEnd().plusDays(7));
                weekdaysList.remove(formatInterval(interval));
                weekdaysList.add(formatInterval(forward));
                mIntervalQueue.add(forward);
            }

            startTime = mIntervalQueue.peek().getStart();
            endTime = mIntervalQueue.peek().getEnd();

            Log.d("startTime", FORMATTER.print(startTime));
            Log.d("endTime", FORMATTER.print(endTime));

            for (int i = 0; i< weekdaysList.size(); i++) {
                Log.d("weekdaysList", weekdaysList.get(i));
            }

            intent.putExtra(EXTRA_WEEKDAYS, weekdaysList);
        } else {
            startTime = FORMATTER.parseDateTime(intent.getStringExtra(EXTRA_START));
            endTime = FORMATTER.parseDateTime(intent.getStringExtra(EXTRA_END));
            while (endTime.isBeforeNow()) {
                startTime = startTime.plusDays(1);
                endTime = endTime.plusDays(1);
            }
        }


        intent.putExtra(EXTRA_START, FORMATTER.print(startTime))
                .putExtra(EXTRA_END, FORMATTER.print(endTime));
        return intent;
    }

    /**
     * Turns on audio.
     */
    private void turnOnAudio() {
        if (checkPermissions()) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_UNMUTE, 0);
            }
        }
    }

    /**
     * Turns off audio.
     */
    private void turnOffAudio() {
        if (checkPermissions()) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_MUTE, 0);
            }
        }
    }

    /**
     * Creates a PendingIntent associated with the parameter intent.
     * First saves a string to a file that can be recreated into a reference to the PendingIntent.
     * If the previous operation is successful, returns the created PendingIntent.
     * The reference needs to be persisted, since the PendingIntent will be registered outside
     * of this application. Without the reference, access to the PendingIntent (from this
     * application) is lost.
     */
    private PendingIntent createPendingIntent(Intent intent) {
        String uuid = intent.getStringExtra(EXTRA_UUID);
        if (mSharedPreferenceUtil.isActivePendingIntent(uuid))cancelPendingIntent(intent);

        int requestCode = mSharedPreferenceUtil.generateRequestCode();
        Intent handle = new Intent(intent.getAction());
        handle.setClass(mContext, SilentIntervalReceiver.class);
        handle.putExtra(EXTRA_REQUEST_CODE, requestCode);
        String uri = handle.toUri(0);

        if (mSharedPreferenceUtil.setPendingIntentHandle(uuid, uri))
            return PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return null;
    }

    /**
     * Cancels an active PendingIntent. Then, removes it's reference from a file.
     */
    private void cancelPendingIntent(Intent intent) {
        String uuid = intent.getStringExtra(EXTRA_UUID);
        String uri = mSharedPreferenceUtil.getPendingIntentHandle(uuid);

        if (uri != null) {
            Intent handle = null;
            try { handle = Intent.parseUri(uri, 0); }
            catch (URISyntaxException e) { e.printStackTrace(); }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, handle.getIntExtra(EXTRA_REQUEST_CODE, -1), handle, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
            mSharedPreferenceUtil.removePendingIntentHandle(uuid);
        }
    }

    /**
     * Records started silent intervals in a file (via SharedPreferenceUtil).
     */
    private void changeStartState(String uuid, boolean flag) {
        if (flag) mStartedUUIDs.add(uuid);
        else mStartedUUIDs.remove(uuid);
        mSharedPreferenceUtil.setStartedIntervalUUIDs(mStartedUUIDs);
    }

    /**
     * Checks if the permissions required by this application are granted. Without permissions,
     * this app cannot autonomously mute and un-mute audio.
     */
    private boolean checkPermissions(){
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            return false;
        } return true;
    }

    /**
     * Returns a string that represents an org.joda.time.Interval.
     */
    private static String formatInterval(DateTime startDate, DateTime endDate) {
        String startString = FORMATTER.print(startDate);
        String endString = FORMATTER.print(endDate);
        return startString + "|" + endString;
    }

    /**
     * Returns a string that represents an org.joda.time.Interval.
     */
    private static String formatInterval(Interval interval) {
        return formatInterval(interval.getStart(), interval.getEnd());
    }

    /**
     * Creates a org.joda.time.Interval from a string.
     */
    private static Interval parseInterval(String pair) {
        String[] times = pair.split("\\|",2);
        return new Interval(FORMATTER.parseDateTime(times[0]), FORMATTER.parseDateTime(times[1]));
    }
}