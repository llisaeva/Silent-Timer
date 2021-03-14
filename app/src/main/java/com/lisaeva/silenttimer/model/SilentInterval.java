package com.lisaeva.silenttimer.model;

import android.os.Bundle;
import android.util.Log;
import androidx.databinding.library.baseAdapters.BR;
import com.lisaeva.silenttimer.SilentIntervalInitiator;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SilentInterval extends SilentIntervalData {
    public static final DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
    public static final String DEFAULT_SILENT_INTERVAL_TITLE = "Mute Audio";

    public SilentInterval() {
        super(UUID.randomUUID().toString(),
                "",
                "",
                null,
                null,
                Code.NEVER.toString(),
                0,
                0);
    }

    public SilentInterval(SilentIntervalData data) {
        super(data.getUuid(),
                data.getTitle(),
                data.getDescription(),
                data.getStartTime(),
                data.getEndTime(),
                data.getWeekdays(),
                data.getRepeat(),
                data.getShowDescription());
    }

    // is() ----------------------------------------------------------------------------------------

    public boolean isRepeat() {
        return super.getRepeat() == 1 ? true : false;
    }

    public boolean isShowDescription() {
        return super.getShowDescription() == 1 ? true : false;
    }

    // get() ---------------------------------------------------------------------------------------

    public boolean getWeekday(Code code) {
        return getWeekdays().charAt(code.index) == Code.DAY_CHECKED.symbol;
    }

    public int getStartHour() {
        String startTime = super.getStartTime();
        LocalTime time;
        if (startTime != null) {
            time = formatter.parseLocalTime(startTime);
        } else {
            time = LocalTime.now();
        }
        return time.getHourOfDay();
    }

    public int getStartMinute() {
        String startTime = super.getStartTime();
        LocalTime time;
        if (startTime != null) {
            time = formatter.parseLocalTime(startTime);
        } else {
            time = LocalTime.now();
        }
        return time.getMinuteOfHour();
    }

    public int getEndHour() {
        String endTime = super.getEndTime();
        LocalTime time;
        if (endTime != null) {
            time = formatter.parseLocalTime(endTime);
        } else {
            time = LocalTime.now();
        }
        return time.getHourOfDay();
    }

    public int getEndMinute() {
        String endTime = super.getEndTime();
        LocalTime time;
        if (endTime != null) {
            time = formatter.parseLocalTime(endTime);
        } else {
            time = LocalTime.now();
        }
        return time.getMinuteOfHour();
    }

    public String getDescription() {
        String description = super.getDescription();
        return description == null ? "" : description;
    }

    public String getDuration() {
        String startTimeString = getStartTime();
        String endTimeString = getEndTime();

        if (endTimeString == null)return "";

        DateTime startTime;
        if (startTimeString == null)startTime = DateTime.now();
        else startTime = DateTime.now().withFields(formatter.parseLocalTime(startTimeString));

        DateTime endTime = DateTime.now().withFields(formatter.parseLocalTime(endTimeString));

        if (startTime.isAfter(endTime)) {
            endTime = endTime.plusDays(1);
        }

        Duration duration = new Interval(startTime, endTime).toDuration();
        int hours = duration.toStandardHours().getHours();
        int minutes = duration.toStandardMinutes().getMinutes() - hours * 24;
        return hours + "h " + minutes + "min";
    }

    public String getListItemDisplayDate() {
        String weekdays = getWeekdays();
        StringBuilder sb = new StringBuilder();
        sb.append(getStartTime() + " - " + getEndTime());

        if (isRepeat()) {
            sb.append(" (");
            if(getWeekdays().equals(Code.DAILY.toString())) {
                sb.append("daily)");
                return sb.toString();
            }

            Code[] days = { Code.SUNDAY, Code.MONDAY, Code.TUESDAY, Code.WEDNESDAY, Code.THURSDAY, Code.FRIDAY, Code.SATURDAY };

            String space = "";
            int j;
            char check = Code.DAY_CHECKED.symbol;

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

    // set() ---------------------------------------------------------------------------------------

    public SilentInterval setStartDate(int hour, int min) {
        LocalTime time = new LocalTime(hour, min);
        super.setStartTime(formatter.print(time));
        return this;
    }

    public SilentInterval setEndDate(int hour, int min) {
        LocalTime time = new LocalTime(hour, min);
        super.setEndTime(formatter.print(time));
        return this;
    }

    public SilentInterval setRepeat(boolean flag) {
        int n = flag ? 1 : 0;
        if (super.getRepeat() == 0 && flag) {
            super.setWeekdays(Code.DAILY.name);
        }
        if (super.getRepeat() == 1 && !flag)super.setWeekdays(Code.NEVER.name);
        super.setRepeat(n);
        return this;
    }

    public SilentInterval setShowDescription(boolean flag) {
        int n = flag ? 1 : 0;
        super.setShowDescription(n);
        return this;
    }

    public SilentInterval setWeekday(Code code, boolean flag) {
        StringBuilder weekdays = new StringBuilder(getWeekdays());
        int index = code.index;
        String symbol = flag? Code.DAY_CHECKED.toString() : Code.DAY_UNCHECKED.toString();
        setWeekdays(weekdays.replace(index, index+1, symbol).toString());
        return this;
    }

    // ---------------------------------------------------------------------------------------------

    public static String formatInterval(String startTime, String endTime) {
        return startTime + "|" + endTime;
    }

    public static String formatInterval(Interval interval) {
        DateTimeFormatter extraFormatter = SilentIntervalInitiator.formatter;
        return formatInterval(extraFormatter.print(interval.getStart()), extraFormatter.print(interval.getEnd()));
    }

    public static Interval parseInterval(String pair) {
        DateTimeFormatter extraFormatter = SilentIntervalInitiator.formatter;
        String[] times = pair.split("\\|",2);
        return new Interval (extraFormatter.parseDateTime(times[0]), extraFormatter.parseDateTime(times[1]));
    }

    public Bundle toBundle() {
        String startTime = getStartTime();
        String endTime = getEndTime();
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
        bundle.putString(SilentIntervalInitiator.EXTRA_UUID, getUuid());
        bundle.putBoolean(SilentIntervalInitiator.EXTRA_REPEAT, isRepeat());
        DateTimeFormatter extraFormatter = SilentIntervalInitiator.formatter;

        if (isRepeat()) {
            List<Code> repeatDays = Code.parseWeekString(weekcode);
            ArrayList<String> repeatCodes = new ArrayList<>(repeatDays.size());

            for (Code weekday : repeatDays) {
                String weekdayCode = formatInterval(extraFormatter.print(startDate.withDayOfWeek(weekday.jodaTimeConstant).minusDays(7)),
                        extraFormatter.print(endDate.withDayOfWeek(weekday.jodaTimeConstant).plusDays(offset).minusDays(7)));
                repeatCodes.add(weekdayCode);
            }
            bundle.putStringArrayList(SilentIntervalInitiator.EXTRA_WEEKDAYS, repeatCodes);
        } else {
            bundle.putString(SilentIntervalInitiator.EXTRA_START_TIME, extraFormatter.print(startDate));
            bundle.putString(SilentIntervalInitiator.EXTRA_END_TIME, extraFormatter.print(endDate));
        }
        return bundle;
    }

    public void copy(SilentInterval copyFrom) {
        setUuid(copyFrom.getUuid());
        setTitle(copyFrom.getTitle());
        setDescription(copyFrom.getDescription());
        setStartTime(copyFrom.getStartTime());
        setEndTime(copyFrom.getEndTime());
        setWeekdays(copyFrom.getWeekdays());
        setRepeat(copyFrom.getRepeat());
        setShowDescription(copyFrom.getShowDescription());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SilentInterval) {
            SilentInterval interval = (SilentInterval) o;
            if (this.getUuid().equals(interval.getUuid())) {
                return true;
            } else return false;
        } else if (o instanceof SilentIntervalData) {
            return super.equals(o);
        }
        return false;
    }

    // ---------------------------------------------------------------------------------------------

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

        @Override
        public String toString() { return name; }

        public static List<Code> parseWeekString(String weekcode) {
            List<Code> list = new ArrayList<>();
            if(weekcode.equals(NEVER))return null;

            for (int i = 0; i<weekcode.length(); i++) {
                if (weekcode.charAt(i) == DAY_CHECKED.symbol) {
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