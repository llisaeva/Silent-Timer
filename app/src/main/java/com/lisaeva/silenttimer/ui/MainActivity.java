package com.lisaeva.silenttimer.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.ActivityCallback;
import com.lisaeva.silenttimer.AlarmList;
import com.lisaeva.silenttimer.R;

public class MainActivity extends AppCompatActivity implements ActivityCallback {
    private FragmentManager fragmentManager;
    private int layoutContainer = R.id.fragment_container;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        // -------------------------------------------
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        // ----------------------------------------

        fragmentManager = getSupportFragmentManager();
        AlarmList list = AlarmList.get(getApplicationContext());

        if (savedInstance == null) {
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(layoutContainer, AlarmListFragment.class, null)
                    .commit();
        }
    }

    @Override
    public void callback(CallbackReason reason, Bundle bundle) {
        switch(reason) {
            case OPEN_ALARM_FRAGMENT:
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(layoutContainer, AlarmFragment.class, bundle)
                        .addToBackStack(null)
                        .commit();
                break;
            case OPEN_ALARM_LIST_FRAGMENT:
                fragmentManager.popBackStack();
                break;
            default:
                break;
        }
    }
}