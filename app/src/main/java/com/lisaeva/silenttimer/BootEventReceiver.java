package com.lisaeva.silenttimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.lisaeva.silenttimer.model.SilentInterval;
import java.util.HashSet;

public class BootEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences.Editor startedIntervalsPreferencesEditor = context.getSharedPreferences(SilentIntervalInitiator.PREFERENCES_STARTED_IDS_KEY, Context.MODE_PRIVATE).edit();
        startedIntervalsPreferencesEditor.clear().apply();

        SharedPreferences.Editor activeIntervalsPreferencesEditor = context.getSharedPreferences(SilentIntervalInitiator.PREFERENCES_SAVED_HANDLES_KEY, Context.MODE_PRIVATE).edit();
        activeIntervalsPreferencesEditor.clear().apply();
    }
}
