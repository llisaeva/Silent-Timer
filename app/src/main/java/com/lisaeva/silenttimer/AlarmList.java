package com.lisaeva.silenttimer;

import android.content.Context;
import android.util.Log;
import androidx.room.Room;
import com.lisaeva.silenttimer.persistence.SilentAlarmDao;
import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import com.lisaeva.silenttimer.persistence.SilentAlarmDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlarmList {
    private static AlarmList alarmList;
    private String databaseName = "silent_timer_database";
    private SilentAlarmDatabase database;
    private SilentAlarmDao silentAlarmDao;
    private List<SilentAlarmData> alarms;

    Observable<SilentAlarmData> observableList;

    private SilentAlarmData tempAlarm;

    // Singleton
    public static AlarmList get(Context context) {
        if (alarmList == null) {
            alarmList = new AlarmList(context);
        } return alarmList;
    }

    public static SilentAlarmData createSilentAlarm() {
        SilentAlarmData alarm = new SilentAlarmData();
        alarm.setUuid(UUID.randomUUID().toString());
        return alarm;
    }

    // Private constructor
    private AlarmList(Context context) {
        silentAlarmDao = database.silentAlarmDao();
        alarms = new ArrayList<SilentAlarmData>();











        tempAlarm = createSilentAlarm();
    }

    public SilentAlarmData getTempAlarm() {
        return tempAlarm;
    }
    public void clearTempAlarm() {
        tempAlarm = createSilentAlarm();
    }

    public void setTempAlarm(SilentAlarmData alarm) {
        tempAlarm = SilentAlarmManager.copySilentAlarm(alarm);
    }

    public void update(SilentAlarmData alarm) {
        SilentAlarmManager.updateSilentAlarm(alarm, tempAlarm);
        silentAlarmDao.update(alarm);
    }

    public void remove(int index) {
        SilentAlarmData alarm = alarms.get(index);
        silentAlarmDao.delete(alarm);
        alarms.remove(index);
    }


    // AlarmListFragment.AlarmAdapter --------------------------------------------------------------
    public SilentAlarmData get(int index) {
        return alarms.get(index);
    }
    public int size() { return alarms.size(); }


    // AlarmListFragment.AlarmHolder ---------------------------------------------------------------
    public int getPosition (SilentAlarmData alarm) {
        return alarms.indexOf(alarm);
    }

    // AlarmFragment -------------------------------------------------------------------------------
    public void add(SilentAlarmData alarm) {
        silentAlarmDao.insert(alarm);
        alarms.add(alarm);
    }

    private interface DatabaseResultCallback {

    }

    private class DatabaseAccess {

    }
}
