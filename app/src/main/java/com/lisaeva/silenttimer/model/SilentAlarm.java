package com.lisaeva.silenttimer.model;

import android.os.Bundle;
import android.util.Log;
import com.lisaeva.silenttimer.SilentAlarmInitiator;
import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SilentAlarm extends SilentAlarmData {
    public static final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");

    private static final String DEFAULT_ALARM_TITLE = "Alarm";

    public SilentAlarm() {
        super(UUID.randomUUID().toString(),
                DEFAULT_ALARM_TITLE,
                "",
                null,
                null,
                Code.NEVER.toString(),
                0,
                0,
                0,
                0);
    }

    public SilentAlarm(SilentAlarmData data) {
        super(data.getUuid(),
                data.getTitle(),
                data.getDescription(),
                data.getStartDate(),
                data.getEndDate(),
                data.getWeekdays(),
                data.getRepeat(),
                data.getShowDescription(),
                data.getActive(),
                data.getStarted());
    }

    public boolean getWeekday(Code code) {
        return getWeekdays().charAt(code.index()) == Code.DAY_CHECKED.symbol();
    }

    public boolean isRepeat() {
        return super.getRepeat() == 1 ? true : false;
    }

    public boolean isShowDescription() {
        return super.getShowDescription() == 1 ? true : false;
    }

    public boolean isActive() {
        return super.getActive() == 1 ? true : false;
    }

    public boolean isStarted() { return super.getStarted() == 1 ? true : false; }

    public int getStartHour() {
        String startDate = super.getStartDate();
        LocalTime time;
        if (startDate != null) {
            time = formatter.parseLocalTime(startDate);
        } else {
            time = LocalTime.now();
        }
        return time.getHourOfDay();
    }
    public int getStartMinute() {
        String startDate = super.getStartDate();
        LocalTime time;
        if (startDate != null) {
            time = formatter.parseLocalTime(startDate);
        } else {
            time = LocalTime.now();
        }
        return time.getMinuteOfHour();
    }
    public int getEndHour() {
        String endDate = super.getEndDate();
        LocalTime time;
        if (endDate != null) {
            time = formatter.parseLocalTime(endDate);
        } else {
            time = LocalTime.now();
        }
        return time.getHourOfDay();
    }
    public int getEndMinute() {
        String endDate = super.getEndDate();
        LocalTime time;
        if (endDate != null) {
            time = formatter.parseLocalTime(endDate);
        } else {
            time = LocalTime.now();
        }
        return time.getMinuteOfHour();
    }

    public Bundle toBundle() {
        String startTime = getStartDate();
        String endTime = getEndDate();
        String weekcode = getWeekdays();
        if (startTime == null || endTime == null || weekcode == null) return null; //TODO: THROW EXCEPTION

        DateTime startDate = new DateTime();
        DateTime endDate = new DateTime();

        startDate = startDate.withFields(formatter.parseLocalTime(startTime));
        endDate = endDate.withFields(formatter.parseLocalTime(endTime));

        int offset = 0;
        if (startDate.isAfter(endDate)) {
            offset = 1;
            endDate = endDate.plusDays(offset);
        } else if (startDate.equals(endDate)) {
            endDate = endDate.plusMinutes(5);
        }

        Bundle bundle = new Bundle();
        bundle.putString(SilentAlarmInitiator.EXTRA_UUID, getUuid());
        bundle.putBoolean(SilentAlarmInitiator.EXTRA_REPEAT, isRepeat());
        bundle.putBoolean(SilentAlarmInitiator.EXTRA_ACTIVATE, isActive());
        bundle.putBoolean(SilentAlarmInitiator.EXTRA_STARTED, isStarted());
        DateTimeFormatter extraFormatter = SilentAlarmInitiator.formatter;

        if (isRepeat()) {
            List<Code> repeatDays = Code.parseWeekString(weekcode);
            ArrayList<String> startRepeatCodes = new ArrayList<>(repeatDays.size());
            ArrayList<String> endRepeatCodes = new ArrayList<>(repeatDays.size());


            for (Code weekday : repeatDays) {
                startRepeatCodes.add(extraFormatter.print(startDate.withDayOfWeek(weekday.jodaTimeConstant)));
                endRepeatCodes.add(extraFormatter.print(startDate.withDayOfWeek(weekday.jodaTimeConstant).plusDays(offset)));
            }
            bundle.putStringArrayList(SilentAlarmInitiator.EXTRA_START_TIMES, startRepeatCodes);
            bundle.putStringArrayList(SilentAlarmInitiator.EXTRA_END_TIMES, endRepeatCodes);
        } else {
            bundle.putString(SilentAlarmInitiator.EXTRA_START_TIME, extraFormatter.print(startDate));
            bundle.putString(SilentAlarmInitiator.EXTRA_END_TIME, extraFormatter.print(endDate));
        }

        return bundle;
    }

    public String getDuration() {
//        String startString = alarm.getStartDate();
//        String endString = alarm.getEndDate();
//        if (startString != null && endString != null) {
//            int startTimeMinutes = getMinutes(startString);
//            int endTimeMinutes = getMinutes(endString);
//
//            if (endTimeMinutes <= startTimeMinutes) {
//                endTimeMinutes += MINUTES_IN_DAY;
//            }
//
//            int hour = (endTimeMinutes-startTimeMinutes) / 60;
//            int min = (endTimeMinutes-startTimeMinutes) % 60;
//            return hour + "h " + min + "min";
//        }
        return "";
    }

    public String getListItemDisplayDate() {
        String weekdays = getWeekdays();
        StringBuilder sb = new StringBuilder();
        sb.append(getStartDate() + "-" + getEndDate());

        if (isRepeat()) {
            sb.append(" (");
            if(getWeekdays().equals(Code.DAILY.toString())) {
                sb.append("daily)");
                return sb.toString();
            }

            Code[] days = { Code.SUNDAY, Code.MONDAY, Code.TUESDAY, Code.WEDNESDAY, Code.THURSDAY, Code.FRIDAY, Code.SATURDAY };

            String space = "";
            int j;
            char check = Code.DAY_CHECKED.symbol();

            for (int i = 0; i<7; i++) {
                if (weekdays.charAt(i) == check) {
                    for (j = i+1; j<7 && weekdays.charAt(j) == check; j++)continue;
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

    public void setStartDate(int hour, int min) {
        LocalTime time = new LocalTime(hour, min);
        super.setStartDate(formatter.print(time));
    }
    public void setEndDate(int hour, int min) {
        LocalTime time = new LocalTime(hour, min);
        super.setEndDate(formatter.print(time));
    }

    public void setRepeat(boolean flag) {
        int n = flag ? 1 : 0;
        super.setRepeat(n);
    }

    public void setShowDescription(boolean flag) {
        int n = flag ? 1 : 0;
        super.setRepeat(n);
    }

    public void setWeekday(Code code, boolean flag) {
        Log.d("XXXXXXX", "HERE-HERE");
        StringBuilder weekdays = new StringBuilder(getWeekdays());
        int index = code.index();
        String symbol = flag? Code.DAY_CHECKED.toString() : Code.DAY_UNCHECKED.toString();
        weekdays.replace(index, index+1, symbol);
    }

    public void copyContent(SilentAlarm copyFrom) {
        setTitle(copyFrom.getTitle());
        setDescription(copyFrom.getDescription());
        setStartDate(copyFrom.getStartDate());
        setEndDate(copyFrom.getEndDate());
        setWeekdays(copyFrom.getWeekdays());
        setRepeat(copyFrom.getRepeat());
        setShowDescription(copyFrom.getShowDescription());
        setActive(copyFrom.getActive());
        setStarted(copyFrom.getStarted());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SilentAlarm) {
            SilentAlarm alarm = (SilentAlarm) o;
            if (this.getUuid().equals(alarm.getUuid())) {
                return true;
            } else return false;
        } else if (o instanceof SilentAlarmData) {
            return super.equals(o);
        }
        return false;
    }

    // ------------------------------------
    public enum Code {
        SUNDAY(0, "Sun", 'S', DateTimeConstants.SUNDAY),
        MONDAY(1, "Mon", 'M', DateTimeConstants.MONDAY),
        TUESDAY(2, "Tue", 'T', DateTimeConstants.TUESDAY),
        WEDNESDAY(3, "Wed", 'W', DateTimeConstants.WEDNESDAY),
        THURSDAY(4, "Thu", 'T', DateTimeConstants.THURSDAY),
        FRIDAY(5, "Fri", 'F', DateTimeConstants.FRIDAY),
        SATURDAY(6, "Sat", 'S', DateTimeConstants.SATURDAY),

        DAILY(-1, "1111111", '\0', -1),
        NEVER(-1, "0000000",'\0', -1),

        DAY_CHECKED(-1, "1", '1', -1),
        DAY_UNCHECKED(-1, "0", '0', -1);


        private int index;
        private String name;
        private char symbol;
        private int jodaTimeConstant;

        Code(int index, String name, char symbol, int jodaTimeConstant) {
            this.index = index;
            this.name = name;
            this.symbol = symbol;
            this.jodaTimeConstant = jodaTimeConstant;
        }

        public int index() { return index; }

        @Override
        public String toString() { return name; }

        public char symbol() { return symbol; }

        public int jodaTimeConstant() { return jodaTimeConstant; }

        public static List<Code> parseWeekString(String weekcode) {
            List<Code> list = new ArrayList<>();
            if(weekcode.equals(NEVER))return null;

            for (int i = 0; i<weekcode.length(); i++) {
                if (weekcode.charAt(i) == DAY_CHECKED.symbol()) {
                    if (i == SUNDAY.index)list.add(SUNDAY);
                    else if (i == MONDAY.index)list.add(MONDAY);
                    else if (i == TUESDAY.index)list.add(TUESDAY);
                    else if (i == WEDNESDAY.index)list.add(WEDNESDAY);
                    else if (i == THURSDAY.index)list.add(THURSDAY);
                    else if (i == FRIDAY.index)list.add(FRIDAY);
                    else if (i == SATURDAY.index)list.add(SATURDAY);
                    else // TODO: THROW EXCEPTION
                    ;
                }
            }
            return list;
        }
    }
}