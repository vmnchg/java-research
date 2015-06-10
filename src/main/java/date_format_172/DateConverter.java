package date_format_172;

import java.text.*;
import java.util.Date;

public class DateConverter {
    private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    public void convert(String dateString) throws IllegalStateException {
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
}