package io.javabrains.coronavirustracker.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class AmericanFormatDate {

    public static String getYesterdaysDateInAmericanFormat(){
        Date yesterday = yesterday();
        String formattedDateYesterday = getYesterdayDateString(yesterday);
        formattedDateYesterday = formattedDateYesterday.replaceAll("/","-");
        return formattedDateYesterday;
    }


    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String getYesterdayDateString(Date yesterday) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(yesterday());
    }

}
