package android.example.movies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ashok on 12/10/2015.
 */
public class MoviesUtil {

    public static int getYearFromDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {

            Date date = formatter.parse(dateInString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}