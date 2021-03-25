package com.lisaeva.silenttimer.localdata;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.lisaeva.silenttimer.SilentIntervalReceiver;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SilentIntervalSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a list of persisted SilentIntervals.
 */
public class SilentIntervalList implements Iterable<SilentInterval> {
    private boolean mLoaded = false;
    private Context mContext;
    private SilentInterval mTempSilentInterval;
    private List<ChangeListener> mListeners;
    private Map<Integer, SilentInterval> mPositionMap;
    private Map<String, SilentInterval> mUUIDMap;
    private SilentIntervalSource mDataSource;

    public SilentIntervalList(Context c) {
        this.mContext = c.getApplicationContext();
        mListeners = new ArrayList<>();
        mDataSource = new SilentIntervalSource(c);
        mTempSilentInterval = new SilentInterval();
        mPositionMap = new HashMap<>();
        mUUIDMap = new HashMap<>();
    }

    // Temporary alarm storage for SilentIntervalFragment ------------------------------------------

    public SilentInterval getTempSilentInterval() { return mTempSilentInterval; }

    public void setTempSilentInterval(SilentInterval interval) { mTempSilentInterval.copy(interval); }

    public void clearTempSilentInterval() { mTempSilentInterval = new SilentInterval(); }

    // ---------------------------------------------------------------------------------------------

    /**
     * Loads persisted SilentIntervals from a database, if it hasn't already.
     */
    public synchronized void loadList() {
        if (!mLoaded) {
            new Thread(() -> {
                for (SilentInterval interval : mDataSource.getAll()) {
                    mUUIDMap.put(interval.getUuid(), interval);
                    mPositionMap.put(interval.getPosition(), interval);
                }
                mLoaded = true;
                notifyListChanged();
            }).start();
        }
    }

    /**
     * Adds a SilentInterval to the list, and saves it in the database.
     */
    public synchronized void add(SilentInterval interval) {
        interval.setPosition(mPositionMap.size());
        new Thread(() -> mDataSource.insert(interval)).start();
        mPositionMap.put(interval.getPosition(), interval);
        mUUIDMap.put(interval.getUuid(), interval);
        notifyItemInserted(interval.getPosition());
        activate(interval);
    }

    /**
     * Updates an existing SilentInterval in this list, and saves the change to the database.
     */
    public synchronized void update(SilentInterval interval) {
        deactivate(interval);
        new Thread(() -> mDataSource.update(interval)).start();
        mPositionMap.put(interval.getPosition(), interval);
        mUUIDMap.put(interval.getUuid(), interval);
        notifyItemChanged(interval.getPosition());
        activate(interval);
    }

    /**
     * Removes a SilentInterval from this list, deletes it from the database.
     */
    public synchronized void remove(SilentInterval interval) {
        deactivate(interval);
        Set<SilentInterval> moved = new HashSet<>();
        int position = interval.getPosition();
        int index = position+1;
        SilentInterval move = mPositionMap.get(index);
        while (move != null) {
            move.setPosition(index-1);
            mPositionMap.put(index-1, move);
            moved.add(move);
            index++;
            move = mPositionMap.get(index);
        }
        notifyRangeChanged(position, mPositionMap.size()-position);
        mPositionMap.remove(index-1);
        mUUIDMap.remove(interval.getUuid());
        notifyItemRemoved(index-1);
        new Thread(() -> {
            mDataSource.delete(interval);
            for (SilentInterval si : moved)mDataSource.update(si);
        }).start();
    }

    /**
     * Deletes all SilentIntervals in this list. Clears the database.
     */
    public synchronized void clear() {
        new Thread(() -> mDataSource.deleteAll());
        mPositionMap.clear();
        mUUIDMap.clear();
        notifyListChanged();
    }

    /**
     * Returns the SilentInterval at the position specified by the parameter.
     * @param index position of the SilentInterval in the list, based on the order that the user
     *              specified.
     * @return SilentInterval with the parameter index.
     */
    public SilentInterval get(int index) {
        return mPositionMap.get(index);
    }

    /**
     * Returns the SilentInterval with the UUID specified by the parameter.
     * @param uuid unique UUID string (created when a SilentInterval is instantiated).
     * @return the SilentInterval with the specified UUID, null if it is absent in this list.
     */
    public SilentInterval get(String uuid) { return mUUIDMap.get(uuid); }

    /**
     * Swaps adjacent SilentIntervals. The parameter indexes must have a difference of 1.
     */
    public void swap(int fromPosition, int toPosition) {
        if(Math.abs(fromPosition - toPosition) > 1)throw new IllegalArgumentException("attempted to swap non-adjacent SilentIntervals.");
        SilentInterval fromInterval = mPositionMap.get(fromPosition);
        SilentInterval toInterval = mPositionMap.get(toPosition);
        fromInterval.setPosition(toPosition);
        toInterval.setPosition(fromPosition);
        mPositionMap.put(toPosition, fromInterval);
        mPositionMap.put(fromPosition, toInterval);
        notifyItemMoved(fromPosition, toPosition);

        new Thread(() -> {
            mDataSource.update(fromInterval);
            mDataSource.update(toInterval);
        }).start();
    }

    /**
     * @return size of this list.
     */
    public int size() {
        if (mPositionMap == null)return 0;
        return mPositionMap.size();
    }

    public boolean isEmpty() { return mPositionMap == null? true : mPositionMap.isEmpty(); }

    /**
     * Activates a SilentInterval by broadcasting an intent that represents it.
     */
    public void activate(SilentInterval interval) {
        new Thread(() -> {
            Intent toCreate = new Intent(mContext, SilentIntervalReceiver.class);
            toCreate.setAction(SilentIntervalReceiver.ACTION_ACTIVATE);
            toCreate.replaceExtras(SilentIntervalReceiver.bundle(interval));
            mContext.sendBroadcast(toCreate);
        }).start();
    }

    /**
     * Deactivates a SilentInterval by broadcasting an intent that represents it.
     */
    public void deactivate(SilentInterval interval) {
        new Thread(() -> {
            Intent toDelete = new Intent(mContext, SilentIntervalReceiver.class);
            toDelete.setAction(SilentIntervalReceiver.ACTION_DEACTIVATE);
            toDelete.replaceExtras(SilentIntervalReceiver.bundle(interval));
            mContext.sendBroadcast(toDelete);
        }).start();
    }

    @NonNull
    @Override
    public Iterator<SilentInterval> iterator() {
        return mPositionMap.values().iterator();
    }

    /**
     * Interface used to notify of changes in this list's data.
     */
    public interface ChangeListener {
        void onListChanged();
        void onItemInserted(int position);
        void onItemRemoved(int position);
        void onItemChanged(int position);
        void onItemMoved(int fromPostion, int toPosition);
        void onRangeChanged(int positionStart, int itemCount);
    }

    /**
     * Registers a class to receive information on data changes in this list.
     */
    public void registerListListener(ChangeListener listener) { if (!mListeners.contains(listener))mListeners.add(listener); }
    public void removeListListener(ChangeListener listener) { mListeners.remove(listener); }

    public void notifyListChanged() { for(ChangeListener listener : mListeners)listener.onListChanged();}
    public void notifyItemInserted(int position) { for(ChangeListener listener : mListeners)listener.onItemInserted(position);}
    public void notifyItemRemoved(int position) { for(ChangeListener listener : mListeners)listener.onItemRemoved(position); }
    public void notifyItemChanged(int position) { for(ChangeListener listener : mListeners)listener.onItemChanged(position); }
    public void notifyItemMoved(int fromPosition, int toPosition) { for(ChangeListener listener : mListeners)listener.onItemMoved(fromPosition, toPosition); }
    public void notifyRangeChanged(int positionStart, int itemCount) { for(ChangeListener listener : mListeners)listener.onRangeChanged(positionStart, itemCount); }
}