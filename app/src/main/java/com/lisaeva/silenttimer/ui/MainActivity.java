package com.lisaeva.silenttimer.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.ActivityCallback;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements ActivityCallback {
    private FragmentManager fragmentManager;
    private int layoutContainer = R.id.fragment_container;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fragmentManager = getSupportFragmentManager();

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