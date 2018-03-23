package com.example.android.mynews.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.android.mynews.R;

import java.util.Calendar;

/**
 * Created by Diego Fajardo on 16/03/2018.
 */

public class PickBeginDateDialog extends AppCompatDialogFragment {

    // TODO: 22/03/2018 Modify Date dialog layout

    private static final String TAG = "PickBeginDateDialog";

    private DatePicker datePicker;
    private Button buttonSubmit;
    private Button buttonCancel;

    private PickBeginDateDialogListener mListener;

    private String selectedYear;
    private String selectedMonth;
    private String selectedDay;
    private String selectedDateForTextView;
    private String selectedDateForUrl;

    public interface PickBeginDateDialogListener {
        void onBeginSubmitClicker(String selectedDateForTextView, String selectedDateForUrl);
        void onBeginCancelClicker(String cancelMessage);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.date_picker_dialog, null);

        datePicker = (DatePicker) mView.findViewById(R.id.datePicker);
        buttonSubmit = (Button) mView.findViewById(R.id.date_picker_submit_button);
        buttonCancel = (Button) mView.findViewById(R.id.date_picker_cancel_button);

        builder.setView(mView);

        Calendar c = Calendar.getInstance();

        datePicker.setMaxDate(c.getTimeInMillis());
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                selectedYear = year + "";

                if ((monthOfYear+1) < 10) {
                    selectedMonth = "0" + (monthOfYear+1);
                }
                else { selectedMonth = (monthOfYear+1) + ""; }

                if (dayOfMonth < 10) {
                    selectedDay = "0" + (dayOfMonth);
                }
                else { selectedDay = dayOfMonth + ""; }

                selectedDateForTextView = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                selectedDateForUrl = selectedYear + selectedMonth + selectedDay;

                Log.i(TAG, selectedDateForTextView);
                Log.i(TAG, selectedDateForUrl);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBeginSubmitClicker(selectedDateForTextView, selectedDateForUrl);
                getDialog().dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBeginCancelClicker("A date was not picked");
                getDialog().dismiss();
            }
        });

        return builder.create() ;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {
            mListener = (PickBeginDateDialogListener) context;
        }catch (ClassCastException e) {
            //Exception that will be thrown if we show the dialog but forget to implement the PickEndDateDialogListener
            //in our SearchArticlesActivity
            throw new ClassCastException(context.toString()
                    + "MUST IMPLEMENT PickEndDateDialogListener");
        }
    }
}