package h.eugene.com.onerepmax.util;

import java.util.Calendar;
import java.util.Date;

public class DateCompare {

    /*
    Checks to see if two dates are equal
    Returns True if dates are equal
     */
    public static boolean areDatesEqual(Date a, Date b) {  // Reference @Lanre Adebambo  https://github.com/lanre-ade
        Calendar cal1 = Calendar.getInstance();  // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance();  // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2
                .get(Calendar.DAY_OF_YEAR);
    }

    /*
    Checks Checks to see if a given date
    Returns True if dates are equal
    */
    public static boolean areDatesEqualYesterday(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance(); // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance(); // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) - 1 == cal2
                .get(Calendar.DAY_OF_YEAR);
    }

    /*
    Checks Checks to see if a given date
    Returns True if dates are equal
    */
    public static boolean areDatesEqualTomorrow(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance(); // put in current date; Date mDate = new Date();
        Calendar cal2 = Calendar.getInstance(); // put in the date to compare
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) + 1 == cal2
                .get(Calendar.DAY_OF_YEAR);
    }
}
