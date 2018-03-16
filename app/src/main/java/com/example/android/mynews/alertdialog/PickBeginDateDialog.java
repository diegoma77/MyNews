package com.example.android.mynews.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.android.mynews.R;

/**
 * Created by Diego Fajardo on 16/03/2018.
 */

public class PickBeginDateDialog extends AppCompatDialogFragment {

    private static final String TAG = "PickBeginDateDialog";
    
    private DatePicker datePicker;
    private Button buttonSubmit;
    private Button buttonCancel;

    private PickBeginDateDialogListener mListener;

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private String selectedDate;

    public interface PickBeginDateDialogListener {
        void onSubmitClicker (String string);
        void onCancelClicker (String string);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.alert_dialog_layout, null);

        datePicker = (DatePicker) mView.findViewById(R.id.datePicker);
        buttonSubmit = (Button) mView.findViewById(R.id.date_picker_submit_button);
        buttonCancel = (Button) mView.findViewById(R.id.date_picker_cancel_button);

        builder.setView(mView);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = monthOfYear + 1;
                selectedDay = dayOfMonth;

                selectedDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;

                Log.i(TAG, selectedDate);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSubmitClicker(selectedDate + " has been SUBMITTED");
                getDialog().dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClicker("A date was not picked");
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
            //Exception that will be thrown if we show the dialog but forget to implement the PickBeginDateDialogListener
            //in our SearchArticlesActivity
            throw new ClassCastException(context.toString()
                    + "MUST IMPLEMENT PickBeginDateDialogListener");
        }
    }
}