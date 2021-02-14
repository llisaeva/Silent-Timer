package com.lisaeva.silenttimer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.ActivityCallback;
import com.lisaeva.silenttimer.AlarmList;
import com.lisaeva.silenttimer.SilentAlarmManager;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.databinding.FragmentAlarmBinding;
import com.lisaeva.silenttimer.model.SilentAlarm;
import com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel;

public class AlarmFragment extends Fragment {
    public static final String SILENT_ALARM_INDEX_TAG = "silent_alarm_index";
    private static String DIALOG_START_TIME_TAG = "dialog_start_time";
    private static String DIALOG_END_TIME_TAG = "dialog_end_time";
    private static int ID_FRAGMENT_LAYOUT = R.layout.fragment_alarm;
    private static final boolean HAS_OPTIONS_MENU = true;

    private SilentAlarm alarm;
    private SilentAlarm temporary;
    private FragmentAlarmBinding fragmentAlarmBinding;
    private ActivityCallback activityCallback;
    private AlarmList alarmList;
    private int silentAlarmIndex;


    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmList = AlarmList.get(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            silentAlarmIndex = savedInstanceState.getInt(SILENT_ALARM_INDEX_TAG);
        } else {
            Bundle bundle = this.getArguments();
            silentAlarmIndex = bundle.getInt(SILENT_ALARM_INDEX_TAG);
        }

        if (silentAlarmIndex >= 0 && silentAlarmIndex < alarmList.size()) {
            alarm = alarmList.get(silentAlarmIndex);
        }

        temporary = alarmList.getTempAlarm();

        Activity activity = getActivity();

        if (activity instanceof ActivityCallback) {
            activityCallback = (ActivityCallback) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAlarmBinding fragmentAlarmBinding = DataBindingUtil.inflate(inflater, ID_FRAGMENT_LAYOUT, container, false);
        this.fragmentAlarmBinding = fragmentAlarmBinding;
        fragmentAlarmBinding.setViewModel(new SilentAlarmViewModel(temporary));
        fragmentAlarmBinding.setHandler(this);
        fragmentAlarmBinding.executePendingBindings();
        setHasOptionsMenu(HAS_OPTIONS_MENU);
        return fragmentAlarmBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SILENT_ALARM_INDEX_TAG, silentAlarmIndex);
    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_alarm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_silent_alarm:
                int code = SilentAlarmManager.validateSilentAlarm(temporary);

                switch (code) {
                    case SilentAlarmManager.SUCCESS:
                        if (alarm != null) {
                            alarmList.update(alarm);
                        } else {
                            alarmList.add(temporary);
                        }
                        alarmList.clearTempAlarm();
                        openAlarmListFragment();
                        return true;

                    case SilentAlarmManager.ERROR_END_DATE_MISSING:
                        Toast.makeText(getActivity(), R.string.error_end_date_missing, Toast.LENGTH_SHORT).show();
                        return true;
                    case SilentAlarmManager.ERROR_ALARM_NOT_VALID:
                        Toast.makeText(getActivity(), R.string.error_general_error, Toast.LENGTH_LONG).show();
                        return true;
                }
                return true;

            case R.id.delete_silent_alarm:
                if (alarm != null) {
                    alarmList.remove(silentAlarmIndex);
                    Toast.makeText(getActivity(), "Scheduled Silent Mode deleted", Toast.LENGTH_SHORT).show();
                }
                alarmList.clearTempAlarm();
                openAlarmListFragment();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Activity callback ---------------------------------------------------------------------------

    public void openAlarmListFragment() {
        if (activityCallback != null) {
            activityCallback.callback(ActivityCallback.CallbackReason.OPEN_ALARM_LIST_FRAGMENT, null);
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    // XML-Binded Methods --------------------------------------------------------------------------

    public void onClickStartDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment(fragmentAlarmBinding.getViewModel(), TimePickerFragment.DIALOG_START_TIME);
        dialog.show(manager, DIALOG_START_TIME_TAG);
    }

    public void onClickEndDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment(fragmentAlarmBinding.getViewModel(), TimePickerFragment.DIALOG_END_TIME);
        dialog.show(manager, DIALOG_END_TIME_TAG);
    }
}