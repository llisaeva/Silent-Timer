package com.lisaeva.silenttimer.persistence;

import android.content.Context;
import androidx.room.Room;
import com.lisaeva.silenttimer.model.SilentInterval;
import java.util.ArrayList;
import java.util.List;

public class SilentIntervalSource {
    private static SilentIntervalDatabase DATABASE;
    private static SilentIntervalDao DAO;
    private Context context;

    public SilentIntervalSource(Context context) {
        this.context = context;
    }

    public synchronized void initDatabase() {
        if (DATABASE == null) {
            DATABASE = Room.databaseBuilder(context, SilentIntervalDatabase.class, SilentIntervalDatabase.DATABASE_NAME).build();
            DAO = DATABASE.silentIntervalDao();
        }
    }

    public synchronized SilentIntervalDatabase getDatabase() {
        return DATABASE;
    }

    public synchronized void insert(SilentInterval interval) {
        if (DAO == null)initDatabase();
        SilentIntervalData data = (SilentIntervalData) interval;
        DAO.insert(data);

    }

    public synchronized void update(SilentInterval interval) {
        if (DAO == null)initDatabase();
        SilentIntervalData data = (SilentIntervalData) interval;
        DAO.update(data);

    }

    public synchronized void delete(SilentInterval interval) {
        if (DAO == null)initDatabase();
        SilentIntervalData data = (SilentIntervalData) interval;
        DAO.delete(data);

    }

    public synchronized List<SilentInterval> getAll() {
        if (DAO == null)initDatabase();
        List<SilentIntervalData> list = DAO.getAll();
        List<SilentInterval> result = new ArrayList<>();
        for (SilentIntervalData data : list) {
            result.add(new SilentInterval(data));
        }
        return result;
    }
}
