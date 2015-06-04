package date_format_172;

import java.text.*;
import java.util.Date;

public class DateConverter {
    private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    public void testConvert(String date) {
        try {
            Date d = df.parse(date);
            String newDate = df.format(d);
            if (!date.equals(newDate)) {
                System.out.println(date + " converted to " + newDate);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}