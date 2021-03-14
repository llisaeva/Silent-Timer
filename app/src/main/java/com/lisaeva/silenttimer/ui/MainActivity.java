package com.lisaeva.silenttimer.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.SilentIntervalInitiator;

public class MainActivity extends AppCompatActivity implements ActivityCallback {
    private FragmentManager fragmentManager;
    private static SharedPreferences sharedPreferences;
    private int layoutContainer = R.id.fragment_container;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sharedPreferences = getApplicationContext().getSharedPreferences(SilentIntervalInitiator.PREFERENCES_SAVED_HANDLES_KEY, Context.MODE_PRIVATE);
        fragmentManager = getSupportFragmentManager();

        if (savedInstance == null) {
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(layoutContainer, SilentIntervalListFragment.class, null)
                    .commit();
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public void openSilentIntervalFragment() {
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(layoutContainer, SilentIntervalFragment.class, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openSilentIntervalListFragment() {
        fragmentManager.popBackStack();
    }

    @Override
    public void requestPermissions() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permission_request_title)
                .setMessage(R.string.permission_request_message)
                .setNegativeButton(R.string.permission_request_deny, (alert, which) -> {
                    alert.dismiss();
                })
                .setPositiveButton(R.string.permission_request_grant, (alert, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                    alert.dismiss();
                }).create();
        dialog.show();
    }

    @Override
    public boolean checkPermissions(){
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            return false;
        } return true;
    }
}