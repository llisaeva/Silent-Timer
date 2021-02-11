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
    private SilentAlarmDao silentAlarmDao;
    private List<SilentAlarmData> alarms;


    private SilentAlarmData tempAlarm;

    // Singleton
    public static AlarmList get(Context context) {
        if (alarmList == null) {
            alarmList = new AlarmList(context);
        } return alarmList;
    }

    // Private constructor
    private AlarmList(Context context) {
        SilentAlarmDatabase database = Room.databaseBuilder(context,
                SilentAlarmDatabase.class, databaseName).allowMainThreadQueries().build();
        silentAlarmDao = database.silentAlarmDao();
        alarms = silentAlarmDao.getAll();
        tempAlarm = new SilentAlarmData();
    }

    public SilentAlarmData getTempAlarm() {
        return tempAlarm;
    }

    public void clearTempAlarm() {
        tempAlarm = new SilentAlarmData();
    }

    public void setTempAlarm(SilentAlarmData alarm) {
        tempAlarm = SilentAlarmManager.copySilentAlarm(alarm);
    }

    public void add(SilentAlarmData alarm) {
        silentAlarmDao.insert(alarm);
        alarms.add(alarm);
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

    public int size() {
        if (alarms == null)return 0;
        return alarms.size();
    }


    // AlarmListFragment.AlarmHolder ---------------------------------------------------------------
    public int getPosition (SilentAlarmData alarm) {
        return alarms.indexOf(alarm);
    }
}
