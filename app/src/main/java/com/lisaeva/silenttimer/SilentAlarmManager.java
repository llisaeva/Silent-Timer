package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.util.Calendar;

public abstract class SilentAlarmManager {

    public static final int SUCCESS = 0;
    public static final int ERROR_END_DATE_MISSING = 1;
    public static final int ERROR_ALARM_NOT_VALID = 2;

    private static final int MINUTES_IN_DAY = 1440;


    // Conversions for AlarmFragment ---------------------------------------------------------------
    public static SilentAlarmData copySilentAlarm(SilentAlarmData alarm) {
        SilentAlarmData clone = new SilentAlarmData();
        updateSilentAlarm(clone, alarm);
        return clone;
    }

    public static void updateSilentAlarm(SilentAlarmData toOverwrite, SilentAlarmData toCopyFrom) {
        toOverwrite.setTitle(toCopyFrom.getTitle());
        toOverwrite.setDescription(toCopyFrom.getDescription());
        toOverwrite.setStartDate(toCopyFrom.getStartDate());
        toOverwrite.setEndDate(toCopyFrom.getEndDate());
        toOverwrite.setWeekdays(toCopyFrom.getWeekdays());
        toOverwrite.setRepeat(toCopyFrom.getRepeat());
        toOverwrite.setShowDescription(toCopyFrom.getShowDescription());
        toOverwrite.setOn(toCopyFrom.getOn());
        toOverwrite.setCompletedTask(toCopyFrom.getCompletedTask());
    }

    public static int validateSilentAlarm(SilentAlarmData alarm) {
        int code;

        code = validateTitle(alarm);
        if (code!=SUCCESS)return code;
        code = validateTime(alarm);
        if (code!=SUCCESS)return code;
        code = validateDescription(alarm);

        return code;

    }

    // Conversions for SilentAlarmViewModel --------------------------------------------------------
    /// get() ---------
    public static String getTitle(SilentAlarmData alarm) { return alarm.getTitle(); }
    public static String getDisplayStartDate(SilentAlarmData alarm) { return alarm.getStartDate(); }
    public static String getDisplayEndDate(SilentAlarmData alarm) { return alarm.getEndDate(); }
    public static boolean getRepeat(SilentAlarmData alarm) { return alarm.getRepeat() == 1 ? true : false; }

    public static boolean getSunday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.SUNDAY.index()); }
    public static boolean getMonday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.MONDAY.index()); }
    public static boolean getTuesday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.TUESDAY.index()); }
    public static boolean getWednesday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.WEDNESDAY.index()); }
    public static boolean getThursday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.THURSDAY.index()); }
    public static boolean getFriday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.FRIDAY.index()); }
    public static boolean getSaturday(SilentAlarmData alarm) { return checkWeekDay(alarm, Codes.SATURDAY.index()); }

    public static boolean getShowDescription(SilentAlarmData alarm) { return alarm.getShowDescription() == 1 ? true : false; }
    public static String getDescription(SilentAlarmData alarm) { return alarm.getDescription(); }

    public static String getDuration(SilentAlarmData alarm) { return calculateDurationForDisplay(alarm); }

    public static String getListItemDisplayDate(SilentAlarmData alarm) {
        String[] days = new String[] { Codes.SATURDAY.toString(),
                                       Codes.MONDAY.toString(),
                                       Codes.TUESDAY.toString(),
                                       Codes.WEDNESDAY.toString(),
                                       Codes.THURSDAY.toString(),
                                       Codes.FRIDAY.toString(),
                                       Codes.SATURDAY.toString() };

        char[] weekdays = alarm.getWeekdays().toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(alarm.getStartDate() + "-" + alarm.getEndDate());

        if (alarm.getRepeat() == 1) {
            sb.append(" (");
            if(alarm.getWeekdays().equals(Codes.DAILY.toString())) {
                sb.append("daily)");
                return sb.toString();
            }

            String space = "";
            int j;

            for (int i = 0; i<7; i++) {
                if (weekdays[i] == '1') {
                    for (j = i+1; j<7 && weekdays[j] == '1'; j++)continue;
                    j--;
                    if (i+1 == j) {
                        sb.append(space + days[i] + ", " + days[j]);
                    } else if (i<j) {
                        sb.append (space + days[i] + "-" + days[j]);
                    } else {
                        sb.append(space + days[i]);
                    }
                    i=j+1;
                    space = ", ";
                }
            }
            sb.append(")");
        }

        return sb.toString();
    }

    public static int getStartHour(SilentAlarmData alarm) {
        if (alarm.getStartDate() != null) {
            return parseTime(alarm.getStartDate())[0];
        } else {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
    }

    public static int getStartMinute(SilentAlarmData alarm) {
        if (alarm.getStartDate() != null) {
            return parseTime(alarm.getStartDate())[1];
        } else {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.MINUTE);
        }
    }

    public static int getEndHour(SilentAlarmData alarm) {
        if (alarm.getEndDate() != null) {
            return parseTime(alarm.getEndDate())[0];
        } else {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
    }

    public static int getEndMinute (SilentAlarmData alarm) {
        if (alarm.getEndDate() != null) {
            return parseTime(alarm.getEndDate())[1];
        } else {
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.MINUTE);
        }
    }

    // util
    private static boolean checkWeekDay(SilentAlarmData alarm, int index) {
        String weekdays = alarm.getWeekdays();
        if (weekdays.charAt(index) == Codes.DAY_CHECKED.symbol())
            return true;
        return false;
    }

    /// set() ---------
    public static void setTitle(SilentAlarmData alarm, String title) {
        if (alarm.getTitle() != title) {
            alarm.setTitle(title);
        }
    }

    public static void setDisplayStartDate(SilentAlarmData alarm, int hour, int min) {
        String time = formatTime(hour, min);
        alarm.setStartDate(time);
    }

    public static void setDisplayEndDate(SilentAlarmData alarm, int hour, int min) {
        String time = formatTime(hour, min);
        alarm.setEndDate(time);
    }

    public static void setRepeat(SilentAlarmData alarm, boolean repeat) {
        int b = repeat ? 1 : 0;
        if (alarm.getRepeat() != b) {
            alarm.setRepeat(b);
            alarm.setWeekdays(Codes.DAILY.toString());
        }
    }

    public static void setSunday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.SUNDAY.index(), flag);
    }

    public static void setMonday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.MONDAY.index(), flag);
    }

    public static void setTuesday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.TUESDAY.index(), flag);
    }

    public static void setWednesday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.WEDNESDAY.index(), flag);
    }

    public static void setThursday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.THURSDAY.index(), flag);
    }

    public static void setFriday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.FRIDAY.index(), flag);
    }

    public static void setSaturday(SilentAlarmData alarm, boolean flag) {
        setWeekday(alarm, Codes.SUNDAY.index(), flag);
    }

    public static void setShowDescription(SilentAlarmData alarm, boolean flag) {
        int i = flag ? 1 : 0;
        if (alarm.getShowDescription() != i)
            alarm.setShowDescription(i);
    }

    private static void setWeekday (SilentAlarmData alarm, int index, boolean flag) {
        Codes code = flag ? Codes.DAY_CHECKED : Codes.DAY_UNCHECKED;
        StringBuilder weekdays = new StringBuilder(alarm.getWeekdays());
        if (weekdays.charAt(index) != code.symbol()) {
            weekdays.replace(index, index+1, code.toString());
        }

        String result = weekdays.toString();
        if (result.equals(Codes.NEVER.toString())) {
            alarm.setRepeat(0);
        }
        alarm.setWeekdays(result);
    }

    public static void setDescription(SilentAlarmData alarm, String description) {
        alarm.setDescription(description);
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
            Calendar calendar = Calendar.getInstance();
            String formatted = formatTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
            alarm.setStartDate(formatted);
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

    // Format the time. Custom, because DateTimeFormatter is not supported on older android versions.
    private static String formatTime(int hour, int min) {
        return String.format("%1$02d:%2$02d", hour, min);
    }

    private static int getMinutes(String time) {
        String[] arr = time.split(":");
        int hour = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);

        return hour*60 + min;
    }

    private static int[] parseTime(String time) {
        int[] result = new int[2];
        String[] parsed = time.split(":");
        result[0] = Integer.parseInt(parsed[0]);
        result[1] = Integer.parseInt(parsed[1]);
        return result;
    }


    // Calculates the duration displayed in the "Add Alarm" and "Edit Alarm" windows.
    private static String calculateDurationForDisplay(SilentAlarmData alarm) {
        String startString = alarm.getStartDate();
        String endString = alarm.getEndDate();
        if (startString != null && endString != null) {
            int startTimeMinutes = getMinutes(startString);
            int endTimeMinutes = getMinutes(endString);

            if (endTimeMinutes <= startTimeMinutes) {
                endTimeMinutes += MINUTES_IN_DAY;
            }

            int hour = (endTimeMinutes-startTimeMinutes) / 60;
            int min = (endTimeMinutes-startTimeMinutes) % 60;
            return hour + "h " + min + "min";
        }
        return "";
    }
}