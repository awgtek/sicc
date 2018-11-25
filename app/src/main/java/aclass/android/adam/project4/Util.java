package aclass.android.adam.project4;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by adam on 1/7/2018.
 */

public class Util {

    public static long getTimeMillisForTimestamp(String timeCreated) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            Date timeCreatedDate = dateFormat.parse(timeCreated);
            return timeCreatedDate.getTime();
        } catch ( ParseException e) {e.printStackTrace();}
        return System.currentTimeMillis();
    }

    public static Long longOrNull(Object o, List<String> results, String errorPrefix) {
        try {
            if (o.toString().trim().equals("")) {
                return null;
            }
            return Long.parseLong((String) o);
        } catch (Exception e) {
            results.add(errorPrefix + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Double doubleOrNull(Object o, List<String> results, String errorPrefix) {
        try {
            String obj = o.toString();
            Double retval;
            if (obj.trim().equals("") || obj.equals("null")) {
                retval = null;
            } else {
                retval = Double.parseDouble((String) obj);
            }
            return retval;
        } catch (Exception e) {
            results.add(errorPrefix + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
