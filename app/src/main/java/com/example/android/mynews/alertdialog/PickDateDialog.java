package com.example.android.mynews.alertdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.android.mynews.R;

/**
 * Created by Diego Fajardo on 16/03/2018.
 */

public class PickDateDialog extends DialogFragment{

    // TODO: 16/03/2018 Add interface to pass the info to the Activity that created the Dialog (see "Dialogs")
    public interface PickDateDialogListener {
        public void onDialogPositiveClick (DialogFragment dialog);
        public void onDialogNegativeClick (DialogFragment dialog);
    }

    PickDateDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (PickDateDialogListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PickDateDialogListener");
        }

    }

    public void createAlertDialog (Context context) {





        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.alert_dialog_layout, null);

        DatePicker datePicker = (DatePicker) mView.findViewById(R.id.datePicker);
        Button buttonSubmit = (Button) mView.findViewById(R.id.date_picker_submit_button);
        Button buttonCancel = (Button) mView.findViewById(R.id.date_picker_cancel_button);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        // TODO: 16/03/2018 Add onClickListener to submit button
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // TODO: 16/03/2018 Add onClickListener to cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




}
