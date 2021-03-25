package com.lisaeva.silenttimer;

import android.content.Context;
import android.widget.Toast;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SilentIntervalData;
import org.joda.time.DateTime;

/**
 * Validates a SilentInterval after the user creates one.
 */
public abstract class SilentIntervalValidator {

    public static final int SUCCESS = 0;
    public static final int ERROR_END_DATE_MISSING = 1;
    public static final int ERROR_DATES_MATCH = 2;
    public static final int ERROR_INTERVAL_NOT_VALID = 3;

    /**
     * Validates the parameter SilentInterval.
     * @return a code that represents its validity.
     */
    public static int validate(SilentIntervalData interval) {
        int resultCode;

        resultCode = validateTitle(interval);
        if (resultCode!=SUCCESS)return resultCode;
        resultCode = validateTime(interval);
        if (resultCode!=SUCCESS)return resultCode;
        resultCode = validateDescription(interval);

        return resultCode;
    }

    /**
     * Displays a Toast message that describes the error.
     * @param context the activity where the message should be shown.
     * @param resultCode the error that needs to be described.
     */
    public static void showErrorMessage(Context context, int resultCode) {
        switch (resultCode) {
            case ERROR_END_DATE_MISSING:
                Toast.makeText(context, R.string.error_end_date_missing, Toast.LENGTH_SHORT).show();
                break;
            case ERROR_DATES_MATCH:
                Toast.makeText(context, R.string.error_dates_match, Toast.LENGTH_SHORT).show();
                break;
            case ERROR_INTERVAL_NOT_VALID:
                Toast.makeText(context, R.string.error_general_error, Toast.LENGTH_SHORT).show();
                break;
            default: return;
        }
    }

    /**
     * If the parameter SilentInterval's title is null, it is set to a default title.
     */
    private static int validateTitle(SilentIntervalData interval) {
        if (interval.getTitle() == null || interval.getTitle().trim().isEmpty()) {
            interval.setTitle(SilentInterval.DEFAULT_SILENT_INTERVAL_TITLE);
        }
        return SUCCESS;
    }

    /**
     * Checks whether the start and end-time is set on the parameter SilentInterval.
     * Returns an error code if the parameter SilentInterval's end-time is missing, or if the
     * start-time and end-time match. Sets the start-time to the current time if it is null.
     */
    private static int validateTime(SilentIntervalData interval) {
        String startString = interval.getStartTime();
        String endString = interval.getEndTime();

        if (endString == null)return ERROR_END_DATE_MISSING;

        if (startString == null) {
            startString = SilentInterval.FORMATTER.print(DateTime.now());
            interval.setStartTime(startString);
        }

        if (startString.equals(endString))return ERROR_DATES_MATCH;

        return SUCCESS;
    }

    /**
     * Checks if the description of the parameter SilentInterval is null or contains only
     * whitespace. If so, sets the showDescription flag on the SilentInterval to true.
     */
    private static int validateDescription(SilentIntervalData interval) {
        if (interval.getDescription() == null || interval.getDescription().trim().isEmpty()) {
            interval.setDescription(null);
            interval.setShowDescription(0);
        }
        return SUCCESS;
    }
}