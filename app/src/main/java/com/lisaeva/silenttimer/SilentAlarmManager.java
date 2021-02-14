package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.util.Calendar;

public abstract class SilentAlarmManager {

    public static final int SUCCESS = 0;
    public static final int ERROR_END_DATE_MISSING = 1;
    public static final int ERROR_ALARM_NOT_VALID = 2;


    // Conversions for AlarmFragment ---------------------------------------------------------------
    public static int validateSilentAlarm(SilentAlarmData alarm) {
        int code;

        code = validateTitle(alarm);
        if (code!=SUCCESS)return code;
        code = validateTime(alarm);
        if (code!=SUCCESS)return code;
        code = validateDescription(alarm);

        return code;
    }

    // Validates a silentAlarm in SilentAlarmFragment ----------------------------------------------
    // Used when user selects check-mark in the action bar
    private static int validateTitle(SilentAlarmData alarm) {
        if (alarm.getTitle() == null || alarm.getTitle().trim().isEmpty()) {
            alarm.setTitle("Alarm");
        }
        return SUCCESS;
    }

    private static int validateTime(SilentAlarmData alarm) {
        if (alarm.getEndDate() == null) {
            return ERROR_END_DATE_MISSING;
        }
        if (alarm.getEndDate() != null &&alarm.getStartDate() == null) {
//            Calendar calendar = Calendar.getInstance();
//            String formatted = formatTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
//            alarm.setStartDate(formatted);
            return SUCCESS;
        }
        return SUCCESS;
    }

    private static int validateDescription(SilentAlarmData alarm) {
        if (alarm.getDescription() == null || alarm.getDescription().trim().isEmpty()) {
            alarm.setDescription(null);
            alarm.setShowDescription(0);
        }
        return SUCCESS;
    }


}