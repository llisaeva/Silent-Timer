package com.lisaeva.silenttimer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.room.Room;
import com.lisaeva.silenttimer.model.SilentAlarm;
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
    private List<SilentAlarm> alarms;
    private Context context;
    private SilentAlarm tempAlarm;

    // Singleton
    public static AlarmList get(Context context) {
        if (alarmList == null) {
            alarmList = new AlarmList(context);
        } return alarmList;
    }

    // Private constructor
    private AlarmList(Context c) {
        this.context = c.getApplicationContext();
        alarms = new ArrayList<SilentAlarm>();
        SilentAlarmDatabase database = Room.databaseBuilder(context,
                SilentAlarmDatabase.class, databaseName).
                allowMainThreadQueries().build();

        silentAlarmDao = database.silentAlarmDao();
        for (SilentAlarmData alarm : silentAlarmDao.getAll()) {
            alarms.add(new SilentAlarm(alarm));
        }
        tempAlarm = new SilentAlarm();
    }

    public SilentAlarm getTempAlarm() {
        return tempAlarm;
    }
    public void clearTempAlarm() {
        tempAlarm = new SilentAlarm();
    }
    public void setTempAlarm(SilentAlarm alarm) { tempAlarm.copyContent(alarm); }

    public void add(SilentAlarm alarm) {
        silentAlarmDao.insert(alarm);
        Log.e("ALARM IN ALARMLIST", alarm.getTitle());
        alarms.add(alarm);
        Intent intent = new Intent(context, SilentAlarmInitiator.class);
        intent.setAction(SilentAlarmInitiator.ACTION_ACTIVATE);
        intent.replaceExtras(alarm.toBundle());
        context.sendBroadcast(intent);
    }

    public void update(SilentAlarm alarm) {
        Intent toDelete = new Intent(context, SilentAlarmInitiator.class);
        toDelete.setAction(SilentAlarmInitiator.ACTION_DEACTIVATE);
        toDelete.replaceExtras(alarm.toBundle());
        context.sendBroadcast(toDelete);

        alarm.copyContent(tempAlarm);
        silentAlarmDao.update(alarm);

        Intent toCreate = new Intent(context, SilentAlarmInitiator.class);
        toCreate.setAction(SilentAlarmInitiator.ACTION_ACTIVATE);
        toCreate.replaceExtras(alarm.toBundle());
        context.sendBroadcast(toCreate);
    }

    public void remove(int index) {
        SilentAlarm alarm = alarms.get(index);

        Intent toDelete = new Intent(context, SilentAlarmInitiator.class);
        toDelete.setAction(SilentAlarmInitiator.ACTION_DEACTIVATE);
        toDelete.replaceExtras(alarm.toBundle());
        context.sendBroadcast(toDelete);

        silentAlarmDao.delete(alarm);
        alarms.remove(index);
    }

    // AlarmListFragment.AlarmAdapter --------------------------------------------------------------
    public SilentAlarm get(int index) {
        return alarms.get(index);
    }

    public int size() {
        if (alarms == null)return 0;
        return alarms.size();
    }

    // AlarmListFragment.AlarmHolder ---------------------------------------------------------------
    public int getPosition (SilentAlarm alarm) {
        return alarms.indexOf(alarm);
    }
}
