package com.lisaeva.silenttimer.viewmodel;

import androidx.databinding.BaseObservable;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;

/**
 * ViewModel that is binded to a layout file, which represents the Schedule Immediate screen.
 */
public class ImmediateViewModel extends BaseObservable {
    private SharedPreferenceUtil mSharedPreferenceUtil;

    public ImmediateViewModel(SharedPreferenceUtil sharedPreferenceUtil) {
        mSharedPreferenceUtil = sharedPreferenceUtil;
    }

    /**
     * @param n slot number (on the UI) of the time increment.
     * @return immediate time increment (in either hours or minutes), depends on the choice
     *         the user made in Settings.
     */
    public String getTime(int n) { return formatTime(mSharedPreferenceUtil.getTimeIncrement(n)); }

    /**
     * @param n slot number (on the UI) of the time increment.
     * @return unit of the time increment, either minutes or hours.
     */
    public String getUnit(int n) { return formatUnit(mSharedPreferenceUtil.getTimeIncrement(n)); }

    /**
     * @param minutes time increment in minutes
     * @return number of hours if the time increment is greater than 60,
     *         number of minutes otherwise.
     */
    private String formatTime(int minutes) {
        if (minutes < 60) {
            return String.valueOf(minutes);
        } else return String.valueOf(minutes / 60);
    }

    /**
     * @param minutes time increment in minutes
     * @return "mins" if time increment is less than 60, and greater than 1;
     *         "min" if time increment is 1;
     *         "hour" if time increment is 60;
     *         "hours" otherwise.
     */
    private String formatUnit(int minutes) {
        if (minutes < 60) {
            if (minutes > 1)return "mins";
            else return "min";
        } else {
            minutes /= 60;
            if (minutes > 1)return "hours";
            else return "hour";
        }
    }
}
