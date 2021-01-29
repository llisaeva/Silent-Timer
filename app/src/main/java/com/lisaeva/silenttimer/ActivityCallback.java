package com.lisaeva.silenttimer;

import android.os.Bundle;

public interface ActivityCallback {
    enum CallbackReason {
        OPEN_ALARM_FRAGMENT,
        OPEN_ALARM_LIST_FRAGMENT,
    }

    void callback(CallbackReason reason, Bundle bundle);

}
