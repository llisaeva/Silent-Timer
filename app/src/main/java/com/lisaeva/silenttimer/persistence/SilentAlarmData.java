package com.lisaeva.silenttimer.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

    @ColumnInfo(name="on")
    private int on;

    @ColumnInfo(name="completed_task")
    private int completedTask;

    // get() ---------------------------------------------------------------------------------------

    public String getUuid() { return uuid; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getWeekdays() { return weekdays; }

    public int getRepeat() { return repeat; }
    public int getShowDescription() { return showDescription; }
    public int getOn() { return on; }
    public int getCompletedTask() { return completedTask; }

    // set() ---------------------------------------------------------------------------------------

    public void setUuid(String uuid) { this.uuid = uuid; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setWeekdays(String weekdays) { this.weekdays = weekdays; }

    public void setRepeat(int repeat) { this.repeat = repeat; }
    public void setShowDescription(int showDescription) { this.showDescription = showDescription; }
    public void setOn(int on) { this.on = on; }
    public void setCompletedTask(int completedTask) { this.completedTask = completedTask; }
}
