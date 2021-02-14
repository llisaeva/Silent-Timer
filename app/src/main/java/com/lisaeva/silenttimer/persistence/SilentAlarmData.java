package com.lisaeva.silenttimer.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "silent_alarms")
public class SilentAlarmData {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="id")
    private String uuid;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="start_date")
    private String startDate;

    @ColumnInfo(name="end_date")
    private String endDate;

    @ColumnInfo(name="weekdays")
    private String weekdays;

    @ColumnInfo(name="repeat")
    private int repeat;

    @ColumnInfo(name="show_description")
    private int showDescription;

    @ColumnInfo(name="active")
    private int active;

    @ColumnInfo(name="started")
    private int started;

    public SilentAlarmData(String uuid, String title, String description, String startDate, String endDate, String weekdays, int repeat, int showDescription, int active, int started) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weekdays = weekdays;
        this.repeat = repeat;
        this.showDescription = showDescription;
        this.active = active;
        this.started = started;
    }

    // get() ---------------------------------------------------------------------------------------

    public String getUuid() { return uuid; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getWeekdays() { return weekdays; }

    public int getRepeat() { return repeat; }
    public int getShowDescription() { return showDescription; }
    public int getActive() { return active; }
    public int getStarted() { return started; }

    // set() ---------------------------------------------------------------------------------------

    public void setUuid(String uuid) { this.uuid = uuid; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setWeekdays(String weekdays) { this.weekdays = weekdays; }

    public void setRepeat(int repeat) { this.repeat = repeat; }
    public void setShowDescription(int showDescription) { this.showDescription = showDescription; }
    public void setActive(int active) { this.active = active; }
    public void setStarted(int started) { this.started = started; }

    @Ignore
    @Override
    public boolean equals(Object o) {
        if (o instanceof SilentAlarmData) {
            SilentAlarmData alarm = (SilentAlarmData) o;
            if (this.uuid.equals(alarm.getUuid()))
                return true;
        }
        return false;
    }
}
