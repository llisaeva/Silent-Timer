package com.lisaeva.silenttimer.persistence;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SilentAlarmData.class}, version = 1)
public abstract class SilentAlarmDatabase extends RoomDatabase {

    public abstract SilentAlarmDao silentAlarmDao();

}
