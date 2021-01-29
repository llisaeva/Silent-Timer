package com.lisaeva.silenttimer.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SilentAlarmData.class}, version = 1)
public abstract class SilentAlarmDatabase extends RoomDatabase {

    public static SilentAlarmDatabase INSTANCE;

    public abstract SilentAlarmDao silentAlarmDao();

    public SilentAlarmDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SilentAlarmDatabase.class, "silent_alarm_database").build();
        }
        return INSTANCE;
    }
}
