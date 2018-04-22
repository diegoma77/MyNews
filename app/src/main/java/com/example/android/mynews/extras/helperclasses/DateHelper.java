package com.example.android.mynews.extras.helperclasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Diego Fajardo on 02/04/2018.
 */

/** This class is used to get dates
 * and convert them to strings*/
public class DateHelper {

    /**
     * Next two methods are used to get the dates
     * when they were not set. The system uses today's date as the endDate
     * and one month ago as the begin date. Since calendar dialog allows to
     * take as end date the 1st of January of 1900 as the soonest date,
     * there won't be a problem getting one month
     * before as begin date because NYT API has much older articles
     */
    public static String getTodayDateAndConvertToString () {

        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date today = Calendar.getInstance().getTime();

        return df.format(today);
    }

    public static String getOneMonthAgoDateAndConvertToString () {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);

        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        return df.format(cal.getTime());

    }

}
