package com.lisaeva.silenttimer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.SilentIntervalReceiver;
import com.lisaeva.silenttimer.localdata.SilentIntervalList;
import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Controller for the Settings screen.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Preference mPreferenceClearList;
    private Preference mPreferenceClearPendingIntents;
    private Preference mPreferenceClearDataBase;
    private Preference mPreferenceActivePendingIntents;
    private Preference mPreferenceTimeIncrement1;
    private Preference mPreferenceTimeIncrement2;
    private Preference mPreferenceTimeIncrement3;
    private Preference mPreferenceTimeIncrement4;
    private ListPreference mPreferenceTheme;
    private SwitchPreferenceCompat mPreferenceDemoMode;

    private SharedPreferenceUtil mSharedPreferencesUtil;
    private Context mContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        mContext = getContext();
        mSharedPreferencesUtil = new SharedPreferenceUtil(mContext);
        mSharedPreferencesUtil.registerActivePendingIntentListener(this);

        Resources resources = mContext.getResources();

        mPreferenceTheme =                 findPreference(resources.getString(R.string.preference_key_theme));
        mPreferenceClearList =             findPreference(resources.getString(R.string.preference_key_clear_list));
        mPreferenceClearPendingIntents =   findPreference(resources.getString(R.string.preference_key_clear_pi));
        mPreferenceClearDataBase =         findPreference(resources.getString(R.string.preference_key_clear_database));
        mPreferenceDemoMode =              findPreference(resources.getString(R.string.preference_key_demo));
        mPreferenceActivePendingIntents =  findPreference(resources.getString(R.string.preference_key_active_pi));
        mPreferenceTimeIncrement1 =        findPreference(resources.getString(R.string.preference_key_increment_1));
        mPreferenceTimeIncrement2 =        findPreference(resources.getString(R.string.preference_key_increment_2));
        mPreferenceTimeIncrement3 =        findPreference(resources.getString(R.string.preference_key_increment_3));
        mPreferenceTimeIncrement4 =        findPreference(resources.getString(R.string.preference_key_increment_4));

        loadActivePendingIntents();

        // Theme preference
        mPreferenceTheme.setOnPreferenceChangeListener(((preference, newValue) -> {
            Toast.makeText(mContext, "Rotate screen or restart app for changes", Toast.LENGTH_LONG).show();
            return true;
        }));

        // Clear List preference
        setDialog(mPreferenceClearList,
                R.string.preference_clear_list_title_dialog,
                R.string.preference_clear_list_message_dialog,
                R.string.preference_clear_list_positive_dialog,
                R.string.preference_clear_list_negative_dialog,
                (alert, which) -> {
            SilentIntervalList list = SilentIntervalListAccess.getListInstance(mContext);
            list.loadList();
            for (SilentInterval interval : list) {
                list.deactivate(interval);
            }
            Toast.makeText(getContext(), R.string.preference_clear_list_toast_dialog, Toast.LENGTH_SHORT).show();
        });

        // Clear PendingIntents preference
        setDialog(mPreferenceClearPendingIntents,
                R.string.preference_clear_pi_title_dialog,
                R.string.preference_clear_pi_message_dialog,
                R.string.preference_clear_pi_positive_dialog,
                R.string.preference_clear_pi_negative_dialog,
                (alert, which) -> {
            Map<String, String> map = mSharedPreferencesUtil.getActivePendingIntentsMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Intent intent = new Intent();
                try {
                    intent = Intent.parseUri(entry.getValue(),0);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                intent.setAction(SilentIntervalReceiver.ACTION_DEACTIVATE);
                intent.putExtra(SilentIntervalReceiver.EXTRA_UUID, entry.getKey());
                getContext().sendBroadcast(intent);
            }
            Toast.makeText(getContext(), R.string.preference_clear_pi_toast_dialog, Toast.LENGTH_SHORT).show();
        });

        // Clear Database preference
        setDialog(mPreferenceClearDataBase,
                R.string.preference_clear_database_title_dialog,
                R.string.preference_clear_database_message_dialog,
                R.string.preference_clear_database_positive_dialog,
                R.string.preference_clear_database_negative_dialog,
                (alert, which) -> {
            SilentIntervalList list = SilentIntervalListAccess.getListInstance(mContext);
            list.loadList();
            list.clear();
            Toast.makeText(getContext(), R.string.preference_clear_database_toast_dialog, Toast.LENGTH_SHORT).show();
        });

        // Demo Mode preference
        boolean demoMode = mSharedPreferencesUtil.getDemoMode();
        mPreferenceDemoMode.setOnPreferenceChangeListener((preference, newValue) -> {
            setDemoMode((Boolean)newValue);
            if ((Boolean)newValue)Toast.makeText(mContext, R.string.preference_demo_toast_dialog1, Toast.LENGTH_SHORT).show();
            else {
                mSharedPreferencesUtil.clearDummies();
                Toast.makeText(mContext, R.string.preference_demo_toast_dialog2, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
        mPreferenceDemoMode.setChecked(demoMode);
        setDemoMode(demoMode);

        // Time Increments
        setTimeIncrementDialog(mPreferenceTimeIncrement1, 1);
        setTimeIncrementDialog(mPreferenceTimeIncrement2, 2);
        setTimeIncrementDialog(mPreferenceTimeIncrement3, 3);
        setTimeIncrementDialog(mPreferenceTimeIncrement4, 4);
    }

    public void setDialog(Preference preference, int title, int message, int positiveButton, int negativeButton, android.content.DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveListener)
                .setNegativeButton(negativeButton, (alert, which) -> {
                    alert.dismiss();
                }).create();
        preference.setOnPreferenceClickListener((p) -> {
            dialog.show();
            return true;
        });
    }

    public void setTimeIncrementDialog(Preference preference, int n) {
        preference.setSummary(getTimeIncrementSummary(mSharedPreferencesUtil.getTimeIncrement(n)));
        View view = this.getLayoutInflater().inflate(R.layout.number_picker, null);
        NumberPicker picker = view.findViewById(R.id.number_picker);
        RadioGroup radioGroup = view.findViewById(R.id.time_radio_group);
        radioGroup.check(R.id.radio_minutes);
        picker.setMaxValue(60);
        picker.setMinValue(1);

        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(R.string.preference_time_increment_dialog_title)
                .setMessage(R.string.preference_time_increment_dialog_message)
                .setPositiveButton(R.string.preference_time_increment_dialog_positive, (alert, which) -> {
                    int timeIncrement = picker.getValue();
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    if (radioId == R.id.radio_hours)timeIncrement *= 60;
                    mSharedPreferencesUtil.setTimeIncrement(n, timeIncrement);
                    preference.setSummary(getTimeIncrementSummary(timeIncrement));
                })
                .setNegativeButton(R.string.preference_time_increment_dialog_negative, (alert, which) -> {
                    alert.dismiss();
                }).create();
        dialog.setView(view);

        preference.setOnPreferenceClickListener((p) -> {
            dialog.show();
            return true;
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (mSharedPreferencesUtil.equalsActivePendingIntentsFile(sharedPreferences)) {
            loadActivePendingIntents();
        }
    }

    private void loadActivePendingIntents() {
        SilentIntervalList list = SilentIntervalListAccess.getListInstance(mContext);
        list.loadList();

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mSharedPreferencesUtil.getActivePendingIntentsMap().entrySet()) {
            Intent intent = new Intent();
            try {
                intent = Intent.parseUri(entry.getValue(), 0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            sb.append("#" + intent.getIntExtra(SilentIntervalReceiver.EXTRA_REQUEST_CODE, -2) + ": " +
                    intent.getAction().toLowerCase().replace("com.lisaeva.silenttimer.", ""));
            if (list.get(entry.getKey()) == null) sb.append(" - MISSING!!");
            sb.append('\n');
        }

        if (sb.length() == 0)sb.append("NONE!");

        mPreferenceActivePendingIntents.setSummary(sb.toString());
    }

    private String getTimeIncrementSummary(int minutes) {
        String result = "";
        if (minutes < 60) {
            if (minutes > 1)result = minutes + " minutes";
            else result = minutes + " minute";
        } else {
            minutes /= 60;
            if (minutes > 1)result = minutes + " hours";
            else result = minutes + " hour";
        }
        return result;
    }

    private void setDemoMode(boolean b) {
        if (b) {
            mSharedPreferencesUtil.setDemoMode(true);
            mPreferenceClearList.setEnabled(false);
            mPreferenceClearDataBase.setEnabled(false);
            mPreferenceClearPendingIntents.setEnabled(false);
        } else {
            mSharedPreferencesUtil.setDemoMode(false);
            mPreferenceClearList.setEnabled(true);
            mPreferenceClearDataBase.setEnabled(true);
            mPreferenceClearPendingIntents.setEnabled(true);
        }
    }
}
