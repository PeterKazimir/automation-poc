package Utils;

import java.util.Arrays;

public class DateUtils {

    public static final String[] MONTHS_DE = {"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
            "November", "Dezember"};

    public static String getExpectedMonthHeaderDE(String date) {
        String[] dates = date.split("\\.");
        String monthHeader = MONTHS_DE[Integer.parseInt(dates[1]) - 1];

        monthHeader = monthHeader + " " + dates[2];
        return monthHeader;
    }

    public static int getMonthNumber(String monthName) {
        return Arrays.asList(MONTHS_DE).indexOf(monthName) + 1;
    }
}
