package com.example.android.mynews;

import android.util.Log;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private static final String TAG = "ExampleUnitTest";

    @Test
    public void testStringCreationForUrl() throws Exception {

        List<String> listOfStrings = new ArrayList<>();
        listOfStrings.add("Alfa");
        listOfStrings.add("Beta");
        listOfStrings.add("Charlie");
        listOfStrings.add("Delta");
        listOfStrings.add("Echo");
        listOfStrings.add("Foxtrot");

        for (int i = 0; i < listOfStrings.size(); i++) {

            if (listOfStrings.get(i).equals("")){
                listOfStrings.remove(listOfStrings.get(i));
            }

        }

        for (int i = 0; i < listOfStrings.size() ; i++) {
            System.out.println(listOfStrings.get(i));
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < listOfStrings.size(); i++) {

            if (!listOfStrings.get(i).equals("")) {

                stringBuilder.append("+" + listOfStrings.get(i));

            }
        }

        String string = stringBuilder.substring(1);

        System.out.println(string);

        assertTrue(string.equals("Alfa+Beta+Charlie+Delta+Echo+Foxtrot"));

    }

    @Test
    public void testAnElementIsInTheList() throws Exception {

        List <String> listOne = new ArrayList<>();
        List <String> listTwo = new ArrayList<>();

        listOne.add("Alfa");
        listOne.add("Beta");
        listOne.add("Charlie");
        listOne.add("Delta");
        listOne.add("Echo");
        listOne.add("Foxtrot");

        listTwo.add("Beta");
        listTwo.add("Delta");
        listTwo.add("Echo");

        for (int i = 0; i < listOne.size() ; i++) {

            for (int j = 0; j < listTwo.size(); j++) {

                if (listOne.get(i).equals(listTwo.get(j))){
                    listOne.remove(i);
                }

            }
        }

        for (int i = 0; i < listOne.size(); i++) {
            System.out.println(listOne.get(i));
        }

        assertTrue(2==2);
    }




}