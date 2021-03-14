package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import org.joda.time.DateTime;

public abstract class SilentIntervalValidator {

    public static final int SUCCESS = 0;
    public static final int ERROR_END_DATE_MISSING = 1;
    public static final int ERROR_INTERVAL_NOT_VALID = 2;

    public static int validateSilentInterval(SilentIntervalData interval) {
        int code;

        code = validateTitle(interval);
        if (code!=SUCCESS)return code;
        code = validateTime(interval);
        if (code!=SUCCESS)return code;
        code = validateDescription(interval);

        return code;
    }

    private static int validateTitle(SilentIntervalData interval) {
        if (interval.getTitle() == null || interval.getTitle().trim().isEmpty()) {
            interval.setTitle(SilentInterval.DEFAULT_SILENT_INTERVAL_TITLE);
        }
        return SUCCESS;
    }

    private static int validateTime(SilentIntervalData interval) {
        if (interval.getEndTime() == null)return ERROR_END_DATE_MISSING;

        if (interval.getEndTime() != null && interval.getStartTime() == null) {
            String startTime = SilentInterval.formatter.print(DateTime.now());
            interval.setStartTime(startTime);
            return SUCCESS;
        }
        return SUCCESS;
    }

    private static int validateDescription(SilentIntervalData interval) {
        if (interval.getDescription() == null || interval.getDescription().trim().isEmpty()) {
            interval.setDescription(null);
            interval.setShowDescription(0);
        }
        return SUCCESS;
    }
}