package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import org.joda.time.DateTime;

public class SilentIntervalDummies {
    private static String testUUID = "test-uuid-";
    private static String[] titles = {"Night", "College", "Work", "Library", "Event", "Silence", "Meeting", "FBI mission", "Hiding from animals", "Homework"};
    private static int[] startHours = {22, 8, 16, 12, 18, 6, 17, 1, 4, 21};
    private static int[] endHours = {7, 14, 18, 13, 19, 6, 19, 23, 7, 3};
    private static int[] startMinutes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int[] endMinutes = {0, 0, 0, 0, 0, 5, 0, 0, 0, 0};

    private static SilentInterval[] dummies = new SilentInterval[10];

    public static SilentInterval[] generateDummies() {
        for (int i = 0; i<dummies.length; i++) {
            SilentInterval interval = new SilentInterval();
            interval.setUuid(testUUID + (i+1));
            interval.setTitle(titles[0]);
            interval.setStartDate(startHours[i], startMinutes[i]);
            interval.setEndDate(endHours[i], endMinutes[i]);
        }

        dummies[0].setWeekdays(SilentInterval.Code.DAILY.toString());
        dummies[0].setRepeat(true);

        dummies[1].setWeekday(SilentInterval.Code.MONDAY, true)
                .setWeekday(SilentInterval.Code.TUESDAY, true)
                .setWeekday(SilentInterval.Code.TUESDAY, true)
                .setWeekday(SilentInterval.Code.WEDNESDAY, true)
                .setWeekday(SilentInterval.Code.THURSDAY, true)
                .setWeekday(SilentInterval.Code.FRIDAY, true)
                .setRepeat(true);

        dummies[2].setWeekday(SilentInterval.Code.MONDAY, true)
                .setWeekday(SilentInterval.Code.WEDNESDAY, true)
                .setWeekday(SilentInterval.Code.FRIDAY, true)
                .setRepeat(true);

        dummies[3].setWeekday(SilentInterval.Code.SATURDAY, true)
                .setWeekday(SilentInterval.Code.SUNDAY, true)
                .setRepeat(true);

        dummies[4].setWeekday(SilentInterval.Code.WEDNESDAY, true)
                .setRepeat(true);

        dummies[5].setRepeat(false);

        dummies[6].setWeekday(SilentInterval.Code.TUESDAY, true)
                .setWeekday(SilentInterval.Code.THURSDAY, true)
                .setRepeat(true);

        dummies[7].setRepeat(false);
        dummies[8].setRepeat(false);
        dummies[9].setWeekday(SilentInterval.Code.SUNDAY, true)
                .setRepeat(true);

        return dummies;
    }



}
