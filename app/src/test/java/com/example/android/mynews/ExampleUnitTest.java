package com.example.android.mynews;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void checkIfDateIsAfterToday() throws Exception {

        LocalDate date = LocalDate.of(2014, 3, 18);
        LocalDate today = LocalDate.now( );

        Boolean isToday = date.isBefore(today);

        assertTrue(isToday);
        assertFalse(!isToday);
        assertTrue(date.toString().equals("2014-03-18"));

    }

    @Test
    public void checkCalendarComparison() throws Exception {

        Calendar calendarA = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();

        calendarA.set(2014,3,12);

        long a = calendarA.getTimeInMillis();
        long nowMillis = calendarNow.getTimeInMillis();

        assertTrue(nowMillis > a);
    }

}