package com.lisaeva.silenttimer.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SilentIntervalData.class}, version = 1)
public abstract class SilentIntervalDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "silent_timer_database";

    public abstract SilentIntervalDao silentIntervalDao();

}
