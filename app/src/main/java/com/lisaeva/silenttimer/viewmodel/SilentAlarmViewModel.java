/*
 * Bridge between SilentAlarmData and UI
 * Forwards method calls
 *
 */

package com.lisaeva.silenttimer.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.lisaeva.silenttimer.BR;
import com.lisaeva.silenttimer.model.SilentAlarm;

public class SilentAlarmViewModel extends BaseObservable {
    SilentAlarm silentAlarm;

    public SilentAlarmViewModel(SilentAlarm alarm) { silentAlarm = alarm; }

    @Bindable
    public String getTitle() { return silentAlarm.getTitle(); }

    @Bindable
    public String getStartDate() { return silentAlarm.getStartDate(); }

    @Bindable
    public String getEndDate() { return silentAlarm.getEndDate(); }

    @Bindable
    public boolean getRepeat() { return silentAlarm.isRepeat(); }

    @Bindable
    public boolean getSunday() { return silentAlarm.getWeekday(SilentAlarm.Code.SUNDAY); }

    @Bindable
    public boolean getMonday() { return silentAlarm.getWeekday(SilentAlarm.Code.MONDAY); }

    @Bindable
    public boolean getTuesday() { return silentAlarm.getWeekday(SilentAlarm.Code.TUESDAY);}

    @Bindable
    public boolean getWednesday() { return silentAlarm.getWeekday(SilentAlarm.Code.WEDNESDAY); }

    @Bindable
    public boolean getThursday() { return silentAlarm.getWeekday(SilentAlarm.Code.THURSDAY); }

    @Bindable
    public boolean getFriday() { return silentAlarm.getWeekday(SilentAlarm.Code.FRIDAY); }

    @Bindable
    public boolean getSaturday() { return silentAlarm.getWeekday(SilentAlarm.Code.SATURDAY); }

    @Bindable
    public boolean getShowDescription() { return silentAlarm.isShowDescription(); }

    @Bindable
    public String getDescription() { return silentAlarm.getDescription(); }

    @Bindable
    public String getDuration() { return silentAlarm.getDuration(); }

    @Bindable
    public String getListItemDisplayDate() { return silentAlarm.getListItemDisplayDate(); }

    public int getStartHour() { return silentAlarm.getStartHour(); }
    public int getStartMinute() {
        return silentAlarm.getStartMinute();
    }
    public int getEndHour() { return silentAlarm.getEndHour(); }
    public int getEndMinute() { return silentAlarm.getEndMinute(); }

    public void setTitle(String title) {
        silentAlarm.setTitle(title);
        notifyPropertyChanged(BR.title);
    }

    public void setStartDate(int hour, int min) {
        silentAlarm.setStartDate(hour, min);
        notifyPropertyChanged(BR.startDate);
    }

    public void setEndDate(int hour, int min) {
        silentAlarm.setEndDate(hour, min);
        notifyPropertyChanged(BR.endDate);
    }

    public void setRepeat(boolean repeat) {
        silentAlarm.setRepeat(repeat);
        notifyChange();
    }

    public void setSunday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.SUNDAY, flag);
        notifyPropertyChanged(BR.sunday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setMonday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.MONDAY, flag);
        notifyPropertyChanged(BR.monday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setTuesday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.TUESDAY, flag);
        notifyPropertyChanged(BR.tuesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setWednesday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.WEDNESDAY, flag);
        notifyPropertyChanged(BR.wednesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setThursday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.THURSDAY, flag);
        notifyPropertyChanged(BR.thursday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setFriday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.FRIDAY, flag);
        notifyPropertyChanged(BR.friday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setSaturday(boolean flag) {
        silentAlarm.setWeekday(SilentAlarm.Code.SATURDAY, flag);
        notifyPropertyChanged(BR.saturday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setShowDescription(boolean flag) {
        silentAlarm.setShowDescription(flag);
        notifyPropertyChanged(BR.showDescription);
    }

    public void setDescription(String description) {
        silentAlarm.setDescription(description);
        notifyPropertyChanged(BR.description);
    }
}