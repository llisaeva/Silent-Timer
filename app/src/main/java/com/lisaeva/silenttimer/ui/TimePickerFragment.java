package com.lisaeva.silenttimer.ui;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.DialogFragment;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;

public class TimePickerFragment extends DialogFragment {

    public static final int DIALOG_START_TIME = 0;
    public static final int DIALOG_END_TIME = 1;

    private static final int ID_DIALOG_LAYOUT = R.layout.fragment_time_picker;
    private static final int ID_TIME_PICKER = R.id.dialog_time_picker;

    private TimePicker mTimePicker;
    private SilentIntervalViewModel mViewModel;

    private int mType;

    public TimePickerFragment(SilentIntervalViewModel viewModel, int type) {
        this.mType = type;
        this.mViewModel = viewModel;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(ID_DIALOG_LAYOUT, null);
        mTimePicker = view.findViewById(ID_TIME_PICKER);
        mTimePicker.setIs24HourView(true);

        int savedHour = 0;
        int savedMinute = 0;

        if (mType == DIALOG_START_TIME) {
            savedHour = mViewModel.getStartHour();
            savedMinute = mViewModel.getStartMinute();
        } else if (mType == DIALOG_END_TIME) {
            savedHour = mViewModel.getEndHour();
            savedMinute = mViewModel.getEndMinute();
        }

        if (Build.VERSION.SDK_INT < 23) {
            mTimePicker.setCurrentHour(savedHour);
            mTimePicker.setCurrentMinute(savedMinute);
        } else {
            mTimePicker.setHour(savedHour);
            mTimePicker.setMinute(savedMinute);
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (alert, which) -> {

                    int hour, min;
                    if (Build.VERSION.SDK_INT < 23) {
                        hour = mTimePicker.getCurrentHour();
                        min = mTimePicker.getCurrentMinute();
                    } else {
                        hour = mTimePicker.getHour();
                        min = mTimePicker.getMinute();
                    }

                    if (mType == DIALOG_START_TIME)
                        mViewModel.setStartDate(hour, min);
                    else if (mType == DIALOG_END_TIME)
                        mViewModel.setEndDate(hour, min);
                    mViewModel.notifyPropertyChanged(BR.duration);
                    alert.dismiss();
                })
                .create();

        return dialog;
    }

}