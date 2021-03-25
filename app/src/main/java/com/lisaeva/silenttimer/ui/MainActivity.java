package com.lisaeva.silenttimer.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;

/**
 * Application entry point.
 * Acts as a container for all fragments that display the UI.
 */
public class MainActivity extends AppCompatActivity implements MainActivityCallback {
    private static final int LAYOUT = R.layout.activity_main;
    private static final int LAYOUT_CONTAINER = R.id.fragment_container;

    private Context mContext;
    private FragmentManager mFragmentManager;

    public MainActivity() { super(LAYOUT); }

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstance) {
        mFragmentManager = getSupportFragmentManager();
        mContext = getApplicationContext();
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(mContext);
        sharedPreferenceUtil.setAppTheme(this);
        super.onCreate(savedInstance);
        if (savedInstance == null)openListFragment();
    }

    // Fragments -----------------------------------------------------------------------------------

    @Override
    public void openListFragment() {
        if (mFragmentManager.getFragments().isEmpty())openFragment(ListFragment.class, false);
        else mFragmentManager.popBackStack();
    }

    @Override public void openScheduleFragment() { openFragment(ScheduleFragment.class, true); }
    @Override public void openImmediateFragment() { openFragment(ImmediateFragment.class, true); }
    @Override public void openEditListFragment() { openFragment(EditListFragment.class, true); }
    @Override public void openSettings() { openFragment(SettingsFragment.class, true); }

    private void openFragment(@NonNull Class<? extends androidx.fragment.app.Fragment> fragmentClass, boolean addToBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(LAYOUT_CONTAINER, fragmentClass, null);
        if (addToBackStack)transaction.addToBackStack(null);
        transaction.commit();
    }

    // Utilities -----------------------------------------------------------------------------------

    @Override public @NonNull Context getContext() { return mContext; }

    @Override
    public void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_request_title)
                    .setMessage(R.string.permission_request_message)
                    .setNegativeButton(R.string.permission_request_deny, (alert, which) -> {
                        alert.dismiss();
                    })
                    .setPositiveButton(R.string.permission_request_grant, (alert, which) -> {
                        Intent intent = null;
                        intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);
                        alert.dismiss();
                    }).create();
            dialog.show();
        }
    }

    @Override
    public boolean checkPermissions(){
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            return false;
        } return true;
    }
}