/*
 * Bridge between SilentAlarmData and UI
 * Forwards method calls
 *
 */

package com.lisaeva.silenttimer.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.lisaeva.silenttimer.BR;
import com.lisaeva.silenttimer.model.SilentInterval;

/**
 *  ViewModel that is binded to a layout file. Represents list items on the Main screen,
 *  and the content on the Schedule Silent Interval screen.
 */
public class SilentIntervalViewModel extends BaseObservable {
    private SilentInterval mSilentInterval;
    private boolean active = false;

    public SilentIntervalViewModel(SilentInterval interval) {
        mSilentInterval = interval;
    }

    @Bindable
    public String getTitle() { return mSilentInterval.getTitle(); }

    @Bindable
    public String getStartDate() { return mSilentInterval.getStartTime(); }

    @Bindable
    public String getEndDate() { return mSilentInterval.getEndTime(); }

    @Bindable
    public boolean getRepeat() { return mSilentInterval.isRepeat(); }

    @Bindable
    public boolean getSunday() { return mSilentInterval.getWeekday(SilentInterval.Code.SUNDAY); }

    @Bindable
    public boolean getMonday() { return mSilentInterval.getWeekday(SilentInterval.Code.MONDAY); }

    @Bindable
    public boolean getTuesday() { return mSilentInterval.getWeekday(SilentInterval.Code.TUESDAY);}

    @Bindable
    public boolean getWednesday() { return mSilentInterval.getWeekday(SilentInterval.Code.WEDNESDAY); }

    @Bindable
    public boolean getThursday() { return mSilentInterval.getWeekday(SilentInterval.Code.THURSDAY); }

    @Bindable
    public boolean getFriday() { return mSilentInterval.getWeekday(SilentInterval.Code.FRIDAY); }

    @Bindable
    public boolean getSaturday() { return mSilentInterval.getWeekday(SilentInterval.Code.SATURDAY); }

    @Bindable
    public boolean getShowDescription() { return mSilentInterval.isShowDescription(); }

    @Bindable
    public String getDescription() { return mSilentInterval.getDescription(); }

    @Bindable
    public String getDuration() { return mSilentInterval.getDuration(); }

    @Bindable
    public String getListItemDisplayDate() { return mSilentInterval.getListItemDisplayDate(); }

    @Bindable
    public boolean getActive() { return active; }
    public int getStartHour() { return mSilentInterval.getStartHour(); }
    public int getStartMinute() {
        return mSilentInterval.getStartMinute();
    }
    public int getEndHour() { return mSilentInterval.getEndHour(); }
    public int getEndMinute() { return mSilentInterval.getEndMinute(); }

    public void setTitle(String title) {
        mSilentInterval.setTitle(title);
        notifyPropertyChanged(BR.title);
    }

    public void setStartDate(int hour, int min) {
        mSilentInterval.setStartDate(hour, min);
        notifyPropertyChanged(BR.startDate);
    }

    public void setEndDate(int hour, int min) {
        mSilentInterval.setEndDate(hour, min);
        notifyPropertyChanged(BR.endDate);
    }

    public void setRepeat(boolean repeat) {
        mSilentInterval.setRepeat(repeat);
        notifyChange();
    }

    public void setSunday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.SUNDAY, flag);
        notifyPropertyChanged(BR.sunday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setMonday(boolean flag) {
        if (getMonday() != flag) {
            mSilentInterval.setWeekday(SilentInterval.Code.MONDAY, flag);
            notifyPropertyChanged(BR.monday);
            notifyPropertyChanged(BR.repeat);
        }
    }

    public void setTuesday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.TUESDAY, flag);
        notifyPropertyChanged(BR.tuesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setWednesday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.WEDNESDAY, flag);
        notifyPropertyChanged(BR.wednesday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setThursday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.THURSDAY, flag);
        notifyPropertyChanged(BR.thursday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setFriday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.FRIDAY, flag);
        notifyPropertyChanged(BR.friday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setSaturday(boolean flag) {
        mSilentInterval.setWeekday(SilentInterval.Code.SATURDAY, flag);
        notifyPropertyChanged(BR.saturday);
        notifyPropertyChanged(BR.repeat);
    }

    public void setShowDescription(boolean flag) {
        mSilentInterval.setShowDescription(flag);
        notifyPropertyChanged(BR.showDescription);
    }

    public void setDescription(String description) {
        mSilentInterval.setDescription(description);
        notifyPropertyChanged(BR.description);
    }

    public void setActive(boolean active) {
        this.active = active;
        notifyPropertyChanged(BR.active);
    }
}