package com.lisaeva.silenttimer.model;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.util.UUID;

public class SilentAlarm extends SilentAlarmData {
    private static final String DEFAULT_ALARM_TITLE = "Alarm";

    public SilentAlarm() {
        super(UUID.randomUUID().toString(),
                DEFAULT_ALARM_TITLE,
                "",
                null,
                null,
                Codes.NEVER.toString(),
                0,
                0,
                0,
                0);
    }




    public boolean isRepeat() {
        return super.getRepeat() == 1 ? true : false;
    }

    public boolean isShowDescription() {
        return super.getShowDescription() == 1 ? true : false;
    }

    public boolean isOn() {
        return super.getOn() == 1 ? true : false;
    }

    public boolean isCompletedTask() {
        return super.getCompletedTask() == 1 ? true : false;
    }

    enum Codes {
        SUNDAY(0, "Sun", 'S'),
        MONDAY(1, "Mon", 'M'),
        TUESDAY(2, "Tue", 'T'),
        WEDNESDAY(3, "Wed", 'W'),
        THURSDAY(4, "Thu", 'T'),
        FRIDAY(5, "Fri", 'F'),
        SATURDAY(6, "Sat", 'S'),

        DAILY(-1, "1111111", '\0'),
        NEVER(-1, "0000000",'\0'),

        DAY_CHECKED(1, "1", '1'),
        DAY_UNCHECKED(0, "0", '0');


        private int index;
        private String name;
        private char symbol;

        private Codes(int index, String name, char symbol) {
            this.index = index;
            this.name = name;
            this.symbol = symbol;
        }

        public int index() { return index; }

        @Override
        public String toString() { return name; }

        public char symbol() { return symbol; }
    }
}
