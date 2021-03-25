package com.lisaeva.silenttimer.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Represents a row entry of a SilentInterval in a database.
 */
@Entity(tableName = "silent_timer_data")
public class SilentIntervalData {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name="id")
    private String uuid;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="start_time")
    private String startTime;

    @ColumnInfo(name="end_time")
    private String endTime;

    @ColumnInfo(name="weekdays")
    private String weekdays;

    @ColumnInfo(name="repeat")
    private int repeat;

    @ColumnInfo(name="show_description")
    private int showDescription;

    @ColumnInfo(name="position")
    private int position;

    public SilentIntervalData(String uuid, String title, String description, String startTime, String endTime, String weekdays, int repeat, int showDescription, int position) {
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekdays = weekdays;
        this.repeat = repeat;
        this.showDescription = showDescription;
        this.position = position;
    }

    // get() ---------------------------------------------------------------------------------------

    public String getUuid() { return uuid; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getWeekdays() { return weekdays; }
    public int getRepeat() { return repeat; }
    public int getShowDescription() { return showDescription; }
    public int getPosition() { return position; }

    // set() ---------------------------------------------------------------------------------------

    public void setUuid(String uuid) { this.uuid = uuid; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public void setWeekdays(String weekdays) { this.weekdays = weekdays; }
    public void setRepeat(int repeat) { this.repeat = repeat; }
    public void setShowDescription(int showDescription) { this.showDescription = showDescription; }
    public void setPosition(int position) { this.position = position; }

    @Ignore
    @Override
    public boolean equals(Object o) {
        if (o instanceof SilentIntervalData) {
            SilentIntervalData interval = (SilentIntervalData) o;
            if (this.uuid.equals(interval.getUuid()))
                return true;
        }
        return false;
    }
}
