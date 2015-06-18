package date_format;

import java.lang.ref.SoftReference;
import java.text.*;
import java.util.Date;

public class DateConverter {
    private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    public static void convert(String dateString) throws IllegalStateException {
        try {
            Date date = df.parse(dateString);
            String newDate = df.format(date);
            if (!dateString.equals(newDate)) {
                throw new IllegalStateException(dateString + " converted to " + newDate);
            }
        } catch (ParseException e) {
            System.out.println(e);
        }
    }


    private static final ThreadLocal<SoftReference<DateFormat>> THREAD_LOCAL = new ThreadLocal<SoftReference<DateFormat>>();

    private static DateFormat getDateFormat() {
        SoftReference<DateFormat> ref = THREAD_LOCAL.get();
        if (ref != null) {
            DateFormat result = ref.get();
            if (result != null) {
                return result;
            }
        }
        DateFormat result = new SimpleDateFormat("yyyy/MM/dd");
        ref = new SoftReference<DateFormat>(result);
        THREAD_LOCAL.set(ref);
        return result;
    }

    public static void convertThreadSafe(String dateString) throws IllegalStateException {
        try {
            Date date = getDateFormat().parse(dateString);
            String newDate = getDateFormat().format(date);
            if (!dateString.equals(newDate)) {
                throw new IllegalStateException(dateString + " converted to " + newDate);
            }
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

}