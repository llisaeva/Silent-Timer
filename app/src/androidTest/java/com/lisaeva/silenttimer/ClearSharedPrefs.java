package com.lisaeva.silenttimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Ignore;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClearSharedPrefs {

    boolean clearPrefs = true;
    boolean getIdPrefs = false;


    Context context = ApplicationProvider.getApplicationContext();
    SharedPreferences preferences = context.getSharedPreferences(SilentIntervalInitiator.PREFERENCES_STARTED_IDS_KEY, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    Set<String> nonEmptySet = new HashSet<String>();

    SharedPreferences handlesPreferences = context.getSharedPreferences(SilentIntervalInitiator.PREFERENCES_SAVED_HANDLES_KEY, Context.MODE_PRIVATE);
    SharedPreferences.Editor handlesEditor = handlesPreferences.edit();

    @Test
    public void run() {
        if (clearPrefs)clearPrefs();
        if (getIdPrefs)getIdPrefs();
    }

    private void clearPrefs() {

        editor.clear().commit();
        handlesEditor.clear().commit();

        assertTrue(preferences.getStringSet(SilentIntervalInitiator.PREFERENCES_STARTED_IDS_TAG, nonEmptySet).isEmpty());
        assertTrue(handlesPreferences.getAll().isEmpty());
    }

    private void getIdPrefs() {
        nonEmptySet.add("string");
        Set<String> ids = preferences.getStringSet(SilentIntervalInitiator.PREFERENCES_STARTED_IDS_TAG, nonEmptySet);
        int cnt = 1;

        if (ids.isEmpty()) {
            Log.d("SharedPreferences", "IDS ARE EMPTY!");
        } else {
            for (String id : ids) {
                Log.d("SharedPreferences", cnt + " -- " + id);
            }
        }
    }


}
