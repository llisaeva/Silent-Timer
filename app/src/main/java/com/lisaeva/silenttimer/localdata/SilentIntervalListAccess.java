package com.lisaeva.silenttimer.localdata;

import android.content.Context;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;

/**
 * Access class to a single instance of SilentIntervalList.
 */
public abstract class SilentIntervalListAccess {
    private static SilentIntervalList INSTANCE;
    private static SilentIntervalList INSTANCE_DEMO;
    private static SharedPreferenceUtil SHARED_PREFERENCES;

    public static SilentIntervalList getInstance(Context context) {
        if (SHARED_PREFERENCES == null)SHARED_PREFERENCES = new SharedPreferenceUtil(context);
        if (INSTANCE == null)INSTANCE = new SilentIntervalList(context);

        if (!SHARED_PREFERENCES.getDemoMode()){
            INSTANCE_DEMO = null;
            return INSTANCE;
        }

        else if (INSTANCE_DEMO == null)INSTANCE_DEMO = new DemoIntervalList(context);
        return INSTANCE_DEMO;
    }

    public static SilentIntervalList getListInstance(Context context) {
        if (INSTANCE == null)INSTANCE = new SilentIntervalList(context);
        return INSTANCE;
    }
}
