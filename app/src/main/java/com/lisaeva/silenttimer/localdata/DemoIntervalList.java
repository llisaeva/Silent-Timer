package com.lisaeva.silenttimer.localdata;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * List of SilentIntervals used for Demo Mode. The list items are stored in a xml resource file.
 */
public class DemoIntervalList extends SilentIntervalList {
    private boolean mLoaded = false;
    private Context mContext;
    private Map<Integer, SilentInterval> mPositionMap;
    private Map<String, SilentInterval> mUUIDMap;
    private SharedPreferenceUtil mSharedPreferenceUtil;


    public DemoIntervalList(Context c) {
        super(c);
        this.mContext = c;
        mPositionMap = new HashMap<>();
        mUUIDMap = new HashMap<>();
        mSharedPreferenceUtil = new SharedPreferenceUtil(mContext);
    }

    @Override
    public void loadList() {
        if (!mLoaded) {
            new Thread(() -> {
                Resources resources = mContext.getResources();
                String[] intervalStrings = resources.getStringArray(R.array.dummy_titles);
                SilentInterval interval;
                int index = 0;
                for (String string : intervalStrings) {
                    interval = parseIntervalString(string);
                    interval.setPosition(index);
                    mPositionMap.put(index, interval);
                    mUUIDMap.put(interval.getUuid(), interval);
                    index++;
                }
                mLoaded = true;
                notifyListChanged();
            }).start();
        }
    }

    @Override
    public synchronized void add(SilentInterval interval) {
        int index = mPositionMap.size();
        interval.setPosition(index);
        mPositionMap.put(index, interval);
        mUUIDMap.put(interval.getUuid(), interval);
        mSharedPreferenceUtil.activateDummy(interval.getUuid());
        notifyItemInserted(index);
    }

    @Override
    public synchronized void update(SilentInterval interval) {
        mPositionMap.put(interval.getPosition(), interval);
        mUUIDMap.put(interval.getUuid(), interval);
        mSharedPreferenceUtil.activateDummy(interval.getUuid());
        notifyItemChanged(interval.getPosition());
    }

    @Override
    public synchronized void remove(SilentInterval interval) {
        deactivate(interval);
        int position = interval.getPosition();
        int index = position+1;
        SilentInterval move = mPositionMap.get(index);
        while (move != null) {
            move.setPosition(index-1);
            mPositionMap.put(index-1, move);
            index++;
            move = mPositionMap.get(index);
        }
        notifyRangeChanged(position, mPositionMap.size()-position);
        mPositionMap.remove(index-1);
        mUUIDMap.remove(interval.getUuid());
        notifyItemRemoved(index-1);
    }

    @Override
    public synchronized void clear() {
        mPositionMap.clear();
        mUUIDMap.clear();
        mSharedPreferenceUtil.clearDummies();
        notifyListChanged();
    }

    @Override public SilentInterval get(int index) { return mPositionMap.get(index); }

    @Override public SilentInterval get(String uuid) { return mUUIDMap.get(uuid); }

    @Override
    public void swap(int fromPosition, int toPosition) {
        SilentInterval fromInterval = mPositionMap.get(fromPosition);
        SilentInterval toInterval = mPositionMap.get(toPosition);
        fromInterval.setPosition(toPosition);
        toInterval.setPosition(fromPosition);
        mPositionMap.put(fromPosition, toInterval);
        mPositionMap.put(toPosition, fromInterval);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override public int size() { return mPositionMap.size(); }

    @Override
    public void activate(SilentInterval interval) {
        mSharedPreferenceUtil.activateDummy(interval.getUuid());
    }

    @Override
    public void deactivate(SilentInterval interval) {
        mSharedPreferenceUtil.deactivateDummy(interval.getUuid());
    }

    private SilentInterval parseIntervalString(String intervalString) {
        String[] parsed = intervalString.split("\\|");
        SilentIntervalData data = new SilentIntervalData(parsed[0].trim(),
                parsed[1].trim(),
                parsed[2].trim(),
                parsed[3].trim(),
                parsed[4].trim(),
                parsed[5].trim(),
                Integer.parseInt(parsed[6].trim()),
                Integer.parseInt(parsed[7].trim()),
                Integer.parseInt(parsed[8].trim()));
        return new SilentInterval(data);
    }

    @NonNull
    @Override
    public Iterator<SilentInterval> iterator() {
        return mPositionMap.values().iterator();
    }
}
