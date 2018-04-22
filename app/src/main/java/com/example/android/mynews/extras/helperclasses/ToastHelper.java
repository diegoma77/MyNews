package com.example.android.mynews.extras.helperclasses;

import android.content.Context;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Diego Fajardo on 19/04/2018.
 */

/** This class contains helper methods that
 * allow to create toasts easier and faster */
abstract public class ToastHelper {

    public static void toastShort(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

}
