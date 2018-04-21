package com.example.android.mynews.extras;

import android.content.Context;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Diego Fajardo on 19/04/2018.
 */

abstract public class Helper {

    public static void toastShort(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

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
