package com.lisaeva.silenttimer.ui;

import android.app.Activity;
import android.os.Bundle;
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
import com.lisaeva.silenttimer.SilentIntervalListAccess;
import com.lisaeva.silenttimer.SilentIntervalList;
import com.lisaeva.silenttimer.SilentIntervalValidator;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.databinding.FragmentSilentIntervalBinding;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;

public class SilentIntervalFragment extends Fragment {
    private static final String DIALOG_START_TIME_TAG = "dialog_start_time";
    private static final String DIALOG_END_TIME_TAG = "dialog_end_time";
    private static int ID_FRAGMENT_LAYOUT = R.layout.fragment_silent_interval;
    private static final boolean HAS_OPTIONS_MENU = true;

    private SilentInterval temporary;
    private FragmentSilentIntervalBinding fragmentSilentIntervalBinding;
    private ActivityCallback activityCallback;
    private SilentIntervalList silentIntervalList;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        silentIntervalList = new SilentIntervalListAccess().getInstance(getActivity().getApplicationContext());

        temporary = silentIntervalList.getTempSilentInterval();

        Activity activity = getActivity();

        if (activity instanceof ActivityCallback) {
            activityCallback = (ActivityCallback) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSilentIntervalBinding fragmentSilentIntervalBinding = DataBindingUtil.inflate(inflater, ID_FRAGMENT_LAYOUT, container, false);
        this.fragmentSilentIntervalBinding = fragmentSilentIntervalBinding;
        fragmentSilentIntervalBinding.setViewModel(new SilentIntervalViewModel(temporary));
        fragmentSilentIntervalBinding.setHandler(this);
        fragmentSilentIntervalBinding.executePendingBindings();
        setHasOptionsMenu(HAS_OPTIONS_MENU);
        return fragmentSilentIntervalBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_silent_interval, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_silent_interval:
                saveSilentInterval();
                return true;
            case R.id.delete_silent_interval:
                deleteSilentInterval();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Utils ---------------------------------------------------------------------------------------

    private void saveSilentInterval() {
        int code = SilentIntervalValidator.validateSilentInterval(temporary);
        switch (code) {
            case SilentIntervalValidator.SUCCESS:
                if (activityCallback.checkPermissions()) {
                    if (silentIntervalList.find(temporary.getUuid()) != null) {
                        silentIntervalList.update(temporary);
                        Toast.makeText(getContext(), R.string.toast_interval_update, Toast.LENGTH_SHORT).show();
                    } else {
                        silentIntervalList.add(temporary);
                        Toast.makeText(getContext(), R.string.toast_interval_create, Toast.LENGTH_SHORT).show();
                    }
                    silentIntervalList.clearTempSilentInterval();
                    openSilentIntervalListFragment();
                    break;
                } else {
                    activityCallback.requestPermissions();
                }
            case SilentIntervalValidator.ERROR_END_DATE_MISSING:
                Toast.makeText(getActivity(), R.string.error_end_date_missing, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), R.string.error_general_error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void deleteSilentInterval() {
        if (temporary != null) {
            if (activityCallback.checkPermissions()) {
                if (silentIntervalList.find(temporary.getUuid()) != null) {
                    silentIntervalList.remove(temporary);
                    Toast.makeText(getActivity(), R.string.toast_interval_delete, Toast.LENGTH_SHORT).show();
                }
            } else {
                activityCallback.requestPermissions();
            }
        }
        silentIntervalList.clearTempSilentInterval();
        openSilentIntervalListFragment();
    }

    // Activity callback ---------------------------------------------------------------------------

    public void openSilentIntervalListFragment() {
        if (activityCallback != null) {
            activityCallback.openSilentIntervalListFragment();
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    // XML-Binded Methods --------------------------------------------------------------------------

    public void onClickStartDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment(fragmentSilentIntervalBinding.getViewModel(), TimePickerFragment.DIALOG_START_TIME);
        dialog.show(manager, DIALOG_START_TIME_TAG);
    }

    public void onClickEndDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerFragment dialog = new TimePickerFragment(fragmentSilentIntervalBinding.getViewModel(), TimePickerFragment.DIALOG_END_TIME);
        dialog.show(manager, DIALOG_END_TIME_TAG);
    }
}