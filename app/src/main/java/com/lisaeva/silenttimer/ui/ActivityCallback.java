package com.lisaeva.silenttimer.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

public interface ActivityCallback {
    void openSilentIntervalFragment();
    void openSilentIntervalListFragment();
    void requestPermissions();
    boolean checkPermissions();
}
