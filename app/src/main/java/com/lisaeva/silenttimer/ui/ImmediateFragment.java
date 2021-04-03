package com.lisaeva.silenttimer.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.SilentIntervalValidator;
import com.lisaeva.silenttimer.databinding.FragmentImmediateBinding;
import com.lisaeva.silenttimer.localdata.SilentIntervalList;
import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
import com.lisaeva.silenttimer.viewmodel.ImmediateViewModel;
import org.joda.time.DateTime;

/**
 * Controller for the Schedule Immediate screen.
 */
public class ImmediateFragment extends Fragment implements TimePickerDialog.DialogResult {
    private static final int LAYOUT = R.layout.fragment_immediate;
    private static final int LAYOUT_MENU = R.menu.fragment_immediate;
    private static final boolean HAS_OPTIONS_MENU = true;

    private Context mContext;
    private MainActivityCallback mMainActivityCallback;
    private SilentIntervalList mSilentIntervalList;
    private SharedPreferenceUtil mSharedPreferenceUtil;
    private FragmentImmediateBinding mBinding;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityCallback = (MainActivityCallback) getActivity();
        mContext = getContext();
        mSilentIntervalList = SilentIntervalListAccess.getInstance(mContext);
        mSilentIntervalList.loadList();
        mSharedPreferenceUtil = new SharedPreferenceUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
        mBinding.setViewModel(new ImmediateViewModel(mSharedPreferenceUtil));
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
            case R.id.cancel:
                mMainActivityCallback.openListFragment();
                break;
            default: break;
        }
        return true;
    }

    // Click Events --------------------------------------------------------------------------------

    public void onClickTime(int n) {
        if(mMainActivityCallback.checkPermissions()) {
            SilentInterval interval = new SilentInterval();
            DateTime now = DateTime.now();
            String endTime = SilentInterval.FORMATTER.print(now.plusMinutes(mSharedPreferenceUtil.getTimeIncrement(n)));
            interval.setEndTime(endTime);

            int resultCode = SilentIntervalValidator.validate(interval);
            ImmediateViewModel viewModel = mBinding.getViewModel();
            String toastMessage = "audio turned off for " + viewModel.getTime(n) + " " + viewModel.getUnit(n);

            if (resultCode == SilentIntervalValidator.SUCCESS) {
                mSilentIntervalList.add(interval);
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG);
                mMainActivityCallback.openListFragment();
            } else {
                SilentIntervalValidator.showErrorMessage(getContext(), resultCode);
            }
        } else {
            mMainActivityCallback.requestPermissions();
        }
    }

    public void chooseClockTime() {
        if (mMainActivityCallback.checkPermissions()) {
            DateTime now = DateTime.now();
            TimePickerDialog dialog = new TimePickerDialog(this, now.getHourOfDay(), now.getMinuteOfHour());
            FragmentManager manager = getChildFragmentManager();
            dialog.show(manager, null);
        } else {
            mMainActivityCallback.requestPermissions();
        }
    }

    // TimePickerDialog Result ---------------------------------------------------------------------

    @Override
    public void onDialogResult(TimePickerDialog dialog) {
        SilentInterval interval = new SilentInterval();
        interval.setEndDate(dialog.getHour(), dialog.getMinute());

        int resultCode = SilentIntervalValidator.validate(interval);

        if (resultCode == SilentIntervalValidator.SUCCESS) {
            mSilentIntervalList.add(interval);
            Toast.makeText(mContext, R.string.fragment_immediate_toast_add, Toast.LENGTH_LONG);
            mMainActivityCallback.openListFragment();
        } else {
            SilentIntervalValidator.showErrorMessage(getContext(), resultCode);
        }
    }
}