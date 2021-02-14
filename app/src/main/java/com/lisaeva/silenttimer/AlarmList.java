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

    public void setTempAlarm(SilentAlarm alarm) { tempAlarm.copyContent(alarm); }

    public void clearTempAlarm() {
        tempAlarm = new SilentAlarm();
    }

    public void add(SilentAlarm alarm) {
        alarm.setActive(true);
        silentAlarmDao.insert(alarm);
        alarms.add(alarm);

        Intent intent = new Intent(context, SilentAlarmInitiator.class);
        intent.setAction(SilentAlarmInitiator.ACTION_ACTIVATE);
        intent.replaceExtras(alarm.toBundle());
        context.sendBroadcast(intent);
    }

    public void update(SilentAlarm alarm) {

        if (alarm.isActive()) {
            Intent toDelete = new Intent(context, SilentAlarmInitiator.class);
            toDelete.setAction(SilentAlarmInitiator.ACTION_TERMINATE);
            toDelete.replaceExtras(alarm.toBundle());
            context.sendBroadcast(toDelete);
        }

        alarm.setActive(true);
        alarm.setStarted(false);
        alarm.copyContent(tempAlarm);
        silentAlarmDao.update(alarm);

        Intent toCreate = new Intent(context, SilentAlarmInitiator.class);
        toCreate.setAction(SilentAlarmInitiator.ACTION_ACTIVATE);
        toCreate.replaceExtras(alarm.toBundle());
        context.sendBroadcast(toCreate);
    }

    public void remove(int index) {
        SilentAlarm alarm = alarms.get(index);

        if (alarm.isActive()) {
            Intent toDelete = new Intent(context, SilentAlarmInitiator.class);
            toDelete.setAction(SilentAlarmInitiator.ACTION_TERMINATE);
            toDelete.replaceExtras(alarm.toBundle());
            context.sendBroadcast(toDelete);
        }

        silentAlarmDao.delete(alarm);
        alarms.remove(index);
    }

    public SilentAlarm get(int index) {
        return alarms.get(index);
    }

    public SilentAlarm find(String uuid) {
        for (SilentAlarm alarm : alarms)
            if (alarm.getUuid().equals(uuid))
                return alarm;
        return null;
    }

    public int getPosition (SilentAlarm alarm) {
        return alarms.indexOf(alarm);
    }

    public int size() {
        if (alarms == null)return 0;
        return alarms.size();
    }

    public void saveHandle(String uuid, String uri) {
        SilentAlarm alarm = find(uuid);
        if (alarm != null) {
            alarm.setHandle(uri);
            silentAlarmDao.update(alarm);
        }
    }

    public String getHandleAndDeactivate(String uuid) {
        SilentAlarm alarm = find(uuid);
        String uri = null;
        if (alarm != null) {
            uri = alarm.getHandle();
            alarm.setActive(false);
            alarm.setStarted(false);
            silentAlarmDao.update(alarm);
        }
        return uri;
    }

    public void deactivate(String uuid) {
        SilentAlarm alarm = find(uuid);
        if (alarm != null) {
            alarm.setActive(false);
            alarm.setStarted(false);
            silentAlarmDao.update(alarm);
        }
    }

    public void setStarted(String uuid, boolean flag) {
        SilentAlarm alarm = find(uuid);
        if (alarm != null) {
            alarm.setStarted(flag);
            silentAlarmDao.update(alarm);
        }
    }

}