package com.lisaeva.silenttimer.ui;

import android.content.Context;

/**
 * Used by fragments to communicate with the MainActivity.
 */
public interface MainActivityCallback {
    void openScheduleFragment();
    void openListFragment();
    void openImmediateFragment();
    void openEditListFragment();
    void openSettings();
    void requestPermissions();
    boolean checkPermissions();
    Context getContext();
}
