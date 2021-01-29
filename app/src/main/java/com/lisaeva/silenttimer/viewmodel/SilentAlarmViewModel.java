/*
 * Bridge between SilentAlarmData and UI
 * Forwards method calls
 *
 */

package com.lisaeva.silenttimer.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.lisaeva.silenttimer.BR;
import com.lisaeva.silenttimer.SilentAlarmManager;
import com.lisaeva.silenttimer.persistence.SilentAlarmData;

public class SilentAlarmViewModel extends BaseObservable {
    SilentAlarmData silentAlarm;

    public SilentAlarmViewModel(SilentAlarmData alarm) { silentAlarm = alarm; }

    @Bindable
    public String getTitle() { return SilentAlarmManager.getTitle(silentAlarm); }

    @Bindable
    public String getStartDate() { return SilentAlarmManager.getDisplayStartDate(silentAlarm); }

    @Bindable
    public String getEndDate() { return SilentAlarmManager.getDisplayEndDate(silentAlarm); }

    @Bindable
    public boolean getRepeat() { return SilentAlarmManager.getRepeat(silentAlarm); }

    @Bindable
    public boolean getSunday() { return SilentAlarmManager.getSunday(silentAlarm); }

    @Bindable
    public boolean getMonday() { return SilentAlarmManager.getMonday(silentAlarm); }

    @Bindable
    public boolean getTuesday() { return SilentAlarmManager.getTuesday(silentAlarm);}

    @Bindable
    public boolean getWednesday() { return SilentAlarmManager.getWednesday(silentAlarm); }

    @Bindable
    public boolean getThursday() { return SilentAlarmManager.getThursday(silentAlarm); }

    @Bindable
    public boolean getFriday() { return SilentAlarmManager.getFriday(silentAlarm); }

    @Bindable
    public boolean getSaturday() { return SilentAlarmManager.getSaturday(silentAlarm); }

    @Bindable
    public boolean getShowDescription() { return SilentAlarmManager.getShowDescription(silentAlarm); }

    @Bindable
    public String getDescription() { return SilentAlarmManager.getDescription(silentAlarm); }

    @Bindable
    public String getDuration() { return SilentAlarmManager.getDuration(silentAlarm); }

    @Bindable
    public String getListItemDisplayDate() { return SilentAlarmManager.getListItemDisplayDate(silentAlarm); }

    public int getStartHour() { return SilentAlarmManager.getStartHour(silentAlarm); }
    public int getStartMinute() {
        return SilentAlarmManager.getStartMinute(silentAlarm);
    }
    public int getEndHour() { return SilentAlarmManager.getEndHour(silentAlarm); }
    public int getEndMinute() { return SilentAlarmManager.getEndMinute(silentAlarm); }

    public void setTitle(String title) {
        SilentAlarmManager.setTitle(silentAlarm, title);
        notifyPropertyChanged(BR.title);
    }

    public void setStartDate(int hour, int min) {
        SilentAlarmManager.setDisplayStartDate(silentAlarm, hour, min);
        notifyPropertyChanged(BR.startDate);
    }

    public void setEndDate(int hour, int min) {
        SilentAlarmManager.setDisplayEndDate(silentAlarm, hour, min);
        notifyPropertyChanged(BR.endDate);
    }

    public void setRepeat(boolean repeat) {
        SilentAlarmManager.setRepeat(silentAlarm, repeat);
        notifyChange();
    }

    public void setSunday(boolean flag) {
        SilentAlarmManager.setSunday(silentAlarm, flag);
        notifyPropertyChanged(BR.sunday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setMonday(boolean flag) {
        SilentAlarmManager.setMonday(silentAlarm, flag);
        notifyPropertyChanged(BR.monday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setTuesday(boolean flag) {
        SilentAlarmManager.setTuesday(silentAlarm, flag);
        notifyPropertyChanged(BR.tuesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setWednesday(boolean flag) {
        SilentAlarmManager.setWednesday(silentAlarm, flag);
        notifyPropertyChanged(BR.wednesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setThursday(boolean flag) {
        SilentAlarmManager.setThursday(silentAlarm, flag);
        notifyPropertyChanged(BR.thursday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setFriday(boolean flag) {
        SilentAlarmManager.setFriday(silentAlarm, flag);
        notifyPropertyChanged(BR.friday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setSaturday(boolean flag) {
        SilentAlarmManager.setSaturday(silentAlarm, flag);
        notifyPropertyChanged(BR.saturday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setShowDescription(boolean flag) {
        SilentAlarmManager.setShowDescription(silentAlarm, flag);
        notifyPropertyChanged(BR.showDescription);
    }

    public void setDescription(String description) {
        SilentAlarmManager.setDescription(silentAlarm, description);
        notifyPropertyChanged(BR.description);
    }
}