package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class SilentAlarmSender {
    private static DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm");

    public static void send(SilentAlarmData alarm) {
        if (alarm.getRepeat() == 0) {
            LocalDateTime nowTime = new LocalDateTime();
            LocalDateTime startTime = new LocalDateTime();

            startTime = startTime.withFields(format.parseLocalTime(alarm.getStartDate()));

//            if (startTime.isAfter(endTime)) {
//                startTime = startTime.plusHours(24);
//            }

        }


    }

    public static void activate(SilentAlarmData alarm) {

    }
}
