package com.lisaeva.silenttimer;

import android.content.Context;

public class SilentIntervalListAccess {
    private static SilentIntervalList INSTANCE;

    public SilentIntervalList getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SilentIntervalList(context);
        } return INSTANCE;
    }
}
