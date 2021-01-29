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
import com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel;

public class TimePickerFragment extends DialogFragment {

    public static final int DIALOG_START_TIME = 0;
    public static final int DIALOG_END_TIME = 1;

    private static final int ID_DIALOG_LAYOUT = R.layout.fragment_time_picker;
    private static final int ID_TIME_PICKER = R.id.dialog_time_picker;

    private TimePicker timePicker;
    private SilentAlarmViewModel viewModel;

    private int type;

    public TimePickerFragment(SilentAlarmViewModel viewModel, int type) {
        this.type = type;
        this.viewModel = viewModel;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(ID_DIALOG_LAYOUT, null);
        timePicker = view.findViewById(ID_TIME_PICKER);
        timePicker.setIs24HourView(true);

        int savedHour = 0;
        int savedMinute = 0;

        if (type == DIALOG_START_TIME) {
            savedHour = viewModel.getStartHour();
            savedMinute = viewModel.getStartMinute();
        } else if (type == DIALOG_END_TIME) {
            savedHour = viewModel.getEndHour();
            savedMinute = viewModel.getEndMinute();
        }


        if (Build.VERSION.SDK_INT < 23) {
            timePicker.setCurrentHour(savedHour);
            timePicker.setCurrentMinute(savedMinute);
        } else {
            timePicker.setHour(savedHour);
            timePicker.setMinute(savedMinute);
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (alert, which) -> {

                    int hour, min;
                    if (Build.VERSION.SDK_INT < 23) {
                        hour = timePicker.getCurrentHour();
                        min = timePicker.getCurrentMinute();
                    } else {
                        hour = timePicker.getHour();
                        min = timePicker.getMinute();
                    }

                    if (type == DIALOG_START_TIME)
                        viewModel.setStartDate(hour, min);
                    else if (type == DIALOG_END_TIME)
                        viewModel.setEndDate(hour, min);
                    alert.dismiss();
                })
                .create();

        return dialog;
    }

}