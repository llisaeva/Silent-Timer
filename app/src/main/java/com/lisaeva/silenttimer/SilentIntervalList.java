package com.lisaeva.silenttimer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Room;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SilentIntervalDao;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import com.lisaeva.silenttimer.persistence.SilentIntervalDatabase;
import com.lisaeva.silenttimer.persistence.SilentIntervalSource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SilentIntervalList implements Iterable<SilentInterval> {
    private List<SilentInterval> mSilentIntervals;
    private SilentInterval mTempSilentInterval;
    private List<SilentIntervalListListener> listeners;
    private SilentIntervalSource source;
    private Context context;

    public SilentIntervalList(Context c) {
        this.context = c.getApplicationContext();
        listeners = new ArrayList<SilentIntervalListListener>();
        source = new SilentIntervalSource(c);
        mTempSilentInterval = new SilentInterval();
    }

    // Temporary alarm storage for SilentIntervalFragment ------------------------------------------

    public SilentInterval getTempSilentInterval() { return mTempSilentInterval; }

    public void setTempSilentInterval(SilentInterval interval) { mTempSilentInterval.copy(interval); }

    public void clearTempSilentInterval() { mTempSilentInterval = new SilentInterval(); }

    // ---------------------------------------------------------------------------------------------

    public synchronized void loadList() {
        if (mSilentIntervals == null) {
            new Thread(() -> {
                mSilentIntervals = source.getAll();
                notifyListeners();
                Log.d("List loaded", String.valueOf(mSilentIntervals.size()));
            }).start();
        }
    }

    public synchronized void add(SilentInterval interval) {
        new Thread(() -> source.insert(interval)).start();
        mSilentIntervals.add(interval);
        notifyListeners();

        activate(interval);
    }

    public synchronized void update(SilentInterval interval) {
        terminate(interval);
        new Thread(() -> source.update(interval)).start();

        mSilentIntervals.remove(interval);
        mSilentIntervals.add(interval);
        notifyListeners();

        activate(interval);
    }

    public synchronized void remove(SilentInterval interval) {
        terminate(interval);
        new Thread(() -> source.delete(interval)).start();
        mSilentIntervals.remove(interval);
        notifyListeners();
    }

    public SilentInterval get(int index) {
        return mSilentIntervals.get(index);
    }

    public SilentInterval find(String uuid) {
        for (SilentInterval interval : mSilentIntervals)
            if (interval.getUuid().equals(uuid))
                return interval;
        return null;
    }

    public int size() {
        if (mSilentIntervals == null)return 0;
        return mSilentIntervals.size();
    }

    public void activate(SilentInterval interval) {
        new Thread(() -> {
            Intent toCreate = new Intent(context, SilentIntervalInitiator.class);
            toCreate.setAction(SilentIntervalInitiator.ACTION_ACTIVATE);
            toCreate.replaceExtras(interval.toBundle());
            context.sendBroadcast(toCreate);
        }).start();

    }

    public void terminate(SilentInterval interval) {
        new Thread(() -> {
            Intent toDelete = new Intent(context, SilentIntervalInitiator.class);
            toDelete.setAction(SilentIntervalInitiator.ACTION_TERMINATE);
            toDelete.replaceExtras(interval.toBundle());
            context.sendBroadcast(toDelete);
        }).start();
    }

    public void registerListListener(SilentIntervalListListener listener) {
        loadList();
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (SilentIntervalListListener listener : listeners) {
            listener.onListChanged();
        }
    }

    @NonNull
    @Override
    public Iterator<SilentInterval> iterator() {
        return mSilentIntervals.iterator();
    }
}