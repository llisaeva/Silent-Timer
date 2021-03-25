package com.lisaeva.silenttimer.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.model.SilentInterval;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class that accesses this applications preference data.
 */
public class SharedPreferenceUtil {
    private static String THEME_TAG;
    private static String STARTED_IDS_TAG;
    private static String REQUEST_CODE_TAG;
    private static String DEMO_MODE_TAG;
    private static String TIME_ONE_TAG;
    private static String TIME_TWO_TAG;
    private static String TIME_THREE_TAG;
    private static String TIME_FOUR_TAG;
    private static String DEMO_MODE_ACTIVE_IDS;

    private Context mContext;
    private SharedPreferences mActivePendingIntentsFile;
    private SharedPreferences mDefaultSharedPreferences;

    public SharedPreferenceUtil(Context context) {
        this.mContext = context;
        Resources resources = mContext.getResources();

        THEME_TAG = resources.getString(R.string.preference_key_theme);
        STARTED_IDS_TAG = resources.getString(R.string.preference_key_started_uuids);
        REQUEST_CODE_TAG = resources.getString(R.string.preference_key_request_code);
        DEMO_MODE_TAG = resources.getString(R.string.preference_key_demo);
        TIME_ONE_TAG = resources.getString(R.string.preference_key_increment_1);
        TIME_TWO_TAG = resources.getString(R.string.preference_key_increment_2);
        TIME_THREE_TAG = resources.getString(R.string.preference_key_increment_3);
        TIME_FOUR_TAG = resources.getString(R.string.preference_key_increment_4);
        DEMO_MODE_ACTIVE_IDS = resources.getString(R.string.preference_key_demo_mode_active_ids);
        mActivePendingIntentsFile = context.getSharedPreferences(resources.getString(R.string.preference_key_active_pi_file), Context.MODE_PRIVATE);
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int setAppTheme(Activity activity) {
        Resources resources = mContext.getResources();
        int themeID = -1;

        String themeName = mDefaultSharedPreferences.getString(THEME_TAG, resources.getString(R.string.dark_theme));
        if (themeName.equals(resources.getString(R.string.dark_theme)))themeID = R.style.Theme_SilentTimer;
        else if (themeName.equals(resources.getString(R.string.light_theme)))themeID = R.style.Theme_SilentTimer_Light;

        activity.setTheme(themeID);
        return themeID;
    }


    public int getAppTheme() {
        Resources resources = mContext.getResources();

        String themeName = mDefaultSharedPreferences.getString(THEME_TAG, resources.getString(R.string.dark_theme));
        if (themeName.equals(resources.getString(R.string.dark_theme))) return R.style.Theme_SilentTimer;
        else if (themeName.equals(resources.getString(R.string.light_theme))) return R.style.Theme_SilentTimer_Light;
        return -1;
    }

    public void registerActivePendingIntentListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mActivePendingIntentsFile.registerOnSharedPreferenceChangeListener(listener);
    }

    public void setStartedIntervalUUIDs(Set<String> uuids) {
        mDefaultSharedPreferences.edit().putStringSet(STARTED_IDS_TAG, uuids).apply();
    }

    public Set<String> getStartedIntervalUUIDs() {
        Set<String> set = new HashSet<>();
        for (String uuid : mDefaultSharedPreferences.getStringSet(STARTED_IDS_TAG, new HashSet<>())) {
            set.add(uuid);
        }
        return set;
    }

    public Map<String, String> getActivePendingIntentsMap() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, ?> entry : mActivePendingIntentsFile.getAll().entrySet()) {
            map.put(entry.getKey(), (String)entry.getValue());
        }
        return map;
    }

    public boolean setPendingIntentHandle(String uuid, String uri) {
        return mActivePendingIntentsFile.edit().putString(uuid, uri).commit();
    }

    public String getPendingIntentHandle(String uuid) {
        return mActivePendingIntentsFile.getString(uuid, null);
    }

    public void removePendingIntentHandle(String uuid) {
        mActivePendingIntentsFile.edit().remove(uuid).apply();
    }

    public boolean isActivePendingIntent(String uuid) {
        if (!getDemoMode())return mActivePendingIntentsFile.contains(uuid);
        else return mDefaultSharedPreferences.getStringSet(DEMO_MODE_ACTIVE_IDS, new HashSet<>()).contains(uuid);
    }

    public int generateRequestCode() {
        int requestCode = mDefaultSharedPreferences.getInt(REQUEST_CODE_TAG, -1);
        requestCode++;
        mDefaultSharedPreferences.edit().putInt(REQUEST_CODE_TAG, requestCode).apply();
        return requestCode;
    }

    public boolean equalsActivePendingIntentsFile(SharedPreferences preferences) {
        return preferences.equals(mActivePendingIntentsFile);
    }

    public int getTimeIncrement(int n) {
        switch(n) {
            case 1: return mDefaultSharedPreferences.getInt(TIME_ONE_TAG, 30);
            case 2: return mDefaultSharedPreferences.getInt(TIME_TWO_TAG, 60);
            case 3: return mDefaultSharedPreferences.getInt(TIME_THREE_TAG, 120);
            case 4: return mDefaultSharedPreferences.getInt(TIME_FOUR_TAG, 180);
            default: return 0;
        }
    }

    public void setTimeIncrement(int n, int increment) {
        switch(n) {
            case 1:
                mDefaultSharedPreferences.edit().putInt(TIME_ONE_TAG, increment).apply();
                break;
            case 2:
                mDefaultSharedPreferences.edit().putInt(TIME_TWO_TAG, increment).apply();
                break;
            case 3:
                mDefaultSharedPreferences.edit().putInt(TIME_THREE_TAG, increment).apply();
                break;
            case 4:
                mDefaultSharedPreferences.edit().putInt(TIME_FOUR_TAG, increment).apply();
                break;
            default: return;
        }
    }

    public void setDemoMode(boolean b) {
        mDefaultSharedPreferences.edit().putBoolean(DEMO_MODE_TAG, b).commit();
    }

    public boolean getDemoMode(){
        return mDefaultSharedPreferences.getBoolean(DEMO_MODE_TAG, false);
    }

    public void activateDummy(String uuid) {
        Set<String> ids = mDefaultSharedPreferences.getStringSet(DEMO_MODE_ACTIVE_IDS, new HashSet<>());
        ids.add(uuid);
        mDefaultSharedPreferences.edit().putStringSet(DEMO_MODE_ACTIVE_IDS, ids).apply();
    }

    public void deactivateDummy(String uuid) {
        Set<String> ids = mDefaultSharedPreferences.getStringSet(DEMO_MODE_ACTIVE_IDS, new HashSet<>());
        ids.remove(uuid);
        mDefaultSharedPreferences.edit().putStringSet(DEMO_MODE_ACTIVE_IDS, ids).apply();
    }

    public void clearDummies() {
        mDefaultSharedPreferences.edit().remove(DEMO_MODE_ACTIVE_IDS).apply();
    }
}
