package com.lisaeva.silenttimer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.SilentIntervalValidator;
import com.lisaeva.silenttimer.databinding.FragmentScheduleBinding;
import com.lisaeva.silenttimer.localdata.SilentIntervalList;
import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;

/**
 * Controller for the Schedule Silent Interval screen.
 */
public class ScheduleFragment extends Fragment implements TimePickerDialog.DialogResult {
    private static final int LAYOUT = R.layout.fragment_schedule;
    private static final int LAYOUT_MENU = R.menu.fragment_schedule;
    private static final boolean HAS_OPTIONS_MENU = true;

    private static final String DIALOG_START_TIME_TAG = "com.lisaeva.silenttimer.dialog_start_time";
    private static final String DIALOG_END_TIME_TAG = "com.lisaeva.silenttimer.dialog_end_time";

    private MainActivityCallback mMainActivityCallback;
    private SilentIntervalList mSilentIntervalList;
    private SilentInterval mTemporarySilentInterval;
    private FragmentScheduleBinding mBinding;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityCallback = (MainActivityCallback) getActivity();
        mSilentIntervalList = SilentIntervalListAccess.getInstance(mMainActivityCallback.getContext());
        mSilentIntervalList.loadList();
        mTemporarySilentInterval = mSilentIntervalList.getTempSilentInterval();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
        mBinding.setViewModel(new SilentIntervalViewModel(mTemporarySilentInterval));
        mBinding.setHandler(this);
        mBinding.executePendingBindings();
        setHasOptionsMenu(HAS_OPTIONS_MENU);
        return mBinding.getRoot();
    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(LAYOUT_MENU, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_silent_interval:
                saveSilentInterval();
                break;
            case R.id.delete_silent_interval:
                deleteSilentInterval();
                break;
            default: break;
        }
        return true;
    }

    // Menu Actions --------------------------------------------------------------------------------

    private void saveSilentInterval() {
        int resultCode = SilentIntervalValidator.validate(mTemporarySilentInterval);
        if (resultCode == SilentIntervalValidator.SUCCESS) {
            if (mMainActivityCallback.checkPermissions()) {
                if (mSilentIntervalList.get(mTemporarySilentInterval.getUuid()) != null) {
                    mSilentIntervalList.update(mTemporarySilentInterval);
                    Toast.makeText(getContext(), R.string.fragment_schedule_toast_interval_update, Toast.LENGTH_SHORT).show();
                } else {
                    mSilentIntervalList.add(mTemporarySilentInterval);
                    Toast.makeText(getContext(), R.string.fragment_schedule_toast_interval_create, Toast.LENGTH_SHORT).show();
                }
                mSilentIntervalList.clearTempSilentInterval();
                mMainActivityCallback.openListFragment();
            } else {
                mMainActivityCallback.requestPermissions();
            }
        } else {
            SilentIntervalValidator.showErrorMessage(getContext(), resultCode);
        }
    }

    private void deleteSilentInterval() {
        if (mTemporarySilentInterval != null) {
            if (mMainActivityCallback.checkPermissions()) {
                if (mSilentIntervalList.get(mTemporarySilentInterval.getUuid()) != null) {
                    mSilentIntervalList.remove(mTemporarySilentInterval);
                    Toast.makeText(getActivity(), R.string.fragment_schedule_toast_interval_delete, Toast.LENGTH_SHORT).show();
                }
            } else {
                mMainActivityCallback.requestPermissions();
            }
        }
        mSilentIntervalList.clearTempSilentInterval();
        mMainActivityCallback.openListFragment();
    }

    // Click Events --------------------------------------------------------------------------------

    public void onClickStartDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerDialog dialog = new TimePickerDialog(this, mTemporarySilentInterval.getStartHour(), mTemporarySilentInterval.getStartMinute());
        dialog.show(manager, DIALOG_START_TIME_TAG);
    }

    public void onClickEndDate() {
        FragmentManager manager = getChildFragmentManager();
        TimePickerDialog dialog = new TimePickerDialog(this, mTemporarySilentInterval.getEndHour(), mTemporarySilentInterval.getEndMinute());
        dialog.show(manager, DIALOG_END_TIME_TAG);
    }

    // TimePickerDialog Result ---------------------------------------------------------------------

    @Override
    public void onDialogResult(TimePickerDialog dialog) {
        String tag = dialog.getTag();
        SilentIntervalViewModel viewModel = mBinding.getViewModel();

        if (tag.equals(DIALOG_START_TIME_TAG)) {
            viewModel.setStartDate(dialog.getHour(), dialog.getMinute());
            viewModel.notifyPropertyChanged(BR.startDate);
        } else if (tag.equals(DIALOG_END_TIME_TAG)) {
            viewModel.setEndDate(dialog.getHour(), dialog.getMinute());
            viewModel.notifyPropertyChanged(BR.endDate);
        }
    }
}