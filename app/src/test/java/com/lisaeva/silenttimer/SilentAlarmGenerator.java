package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.util.List;

public class SilentAlarmGenerator {

    public List<SilentAlarmData> getDummySilentAlarms() {




        return null;
    }





    private static SilentAlarmData makeDummy(String uuid, String title, String description, String startDate, String endDate, String weekdays, int repeat, int showDescription, int on, int completedTask) {
        SilentAlarmData clone = new SilentAlarmData();
        clone.setUuid(uuid);
        clone.setTitle(title);
        clone.setDescription(description);
        clone.setStartDate(startDate);
        clone.setEndDate(endDate);
        clone.setWeekdays(weekdays);
        clone.setRepeat(repeat);
        clone.setShowDescription(showDescription);
        clone.setOn(on);
        clone.setCompletedTask(completedTask);
        return clone;
    }

    private class Dummy {
        String uuid;
        String title;
        String description;
        String startDate;
        String endDate;
        String weekdays;
        int repeat;
        int showDescription;
        int on;
        int completedTask;
    }
}
