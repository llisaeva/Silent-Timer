package com.lisaeva.silenttimer.ui;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.lisaeva.silenttimer.R;

/**
 * Controller for the Time Picker dialog.
 */
public class TimePickerDialog extends DialogFragment {
    private static final int LAYOUT = R.layout.fragment_time_picker;
    private static final int LAYOUT_TIME_PICKER = R.id.dialog_time_picker;

    private TimePicker mTimePicker;
    private TimePickerDialog.DialogResult mListener;
    private int mMinute;
    private int mHour;

    public TimePickerDialog(TimePickerDialog.DialogResult listener, int hour, int minute) {
        this.mListener = listener;
        this.mMinute = minute;
        this.mHour = hour;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(LAYOUT, null);
        mTimePicker = view.findViewById(LAYOUT_TIME_PICKER);
        mTimePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT < 23) {
            mTimePicker.setCurrentHour(mHour);
            mTimePicker.setCurrentMinute(mMinute);
        } else {
            mTimePicker.setHour(mHour);
            mTimePicker.setMinute(mMinute);
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (alert, which) -> {

                    if (Build.VERSION.SDK_INT < 23) {
                        mHour = mTimePicker.getCurrentHour();
                        mMinute = mTimePicker.getCurrentMinute();
                    } else {
                        mHour = mTimePicker.getHour();
                        mMinute = mTimePicker.getMinute();
                    }
                    mListener.onDialogResult(this);
                    alert.dismiss();
                })
                .create();
        return dialog;
    }

    public int getMinute() { return mMinute; }

    public int getHour() { return mHour; }

    public interface DialogResult {
        void onDialogResult(TimePickerDialog dialog);
    }
}