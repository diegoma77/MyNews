package com.example.android.mynews.extras.helperclasses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Diego Fajardo on 22/04/2018.
 */

/** This class is used when
 * displaying the list of articles.
 * It allows showing and hiding elements
 * in an easier way
 * */
public class ShowHelper {

    /** Shows the ProgressBar and hides the RecyclerView and the ErrorMessage
     * */
    public static void showProgressBar (ProgressBar progressBar,
                                 TextView textViewError,
                                 RecyclerView recyclerView) {

        progressBar.setVisibility(View.VISIBLE);
        textViewError.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    /** Shows the RecyclerView and hides the ProgressBar and the ErrorMessage
     * */
    public static void showRecyclerView(ProgressBar progressBar,
                                 TextView textViewError,
                                 RecyclerView recyclerView) {

        progressBar.setVisibility(View.INVISIBLE);
        textViewError.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    /** Shows the ErrorMessage and hides the ProgressBar and the RecyclerView
     * */
    public static void showErrorMessage (ProgressBar progressBar,
                                  TextView textViewError,
                                  RecyclerView recyclerView) {

        progressBar.setVisibility(View.INVISIBLE);
        textViewError.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

    }

}
