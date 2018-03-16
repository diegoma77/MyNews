package com.example.android.mynews.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * Created by Diego Fajardo on 16/03/2018.
 */

public class PickBeginDateDialog extends AppCompatDialogFragment {

    private PickBeginDateDialogListener mListener;

    public interface PickBeginDateDialogListener {
        void onSubmitClicker (String string);
        void onCancelClicker (String string);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("ATTENTION")
                .setMessage("Do you want to increment the counter by one?")
                .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            mListener.onSubmitClicker("I pressed SUBMIT");
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            mListener.onCancelClicker("I pressed CANCEL");
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

/**
 * AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

 LayoutInflater inflater = LayoutInflater.from(getActivity());
 View mView = inflater.inflate(R.layout.alert_dialog_layout, null);

 DatePicker datePicker = (DatePicker) mView.findViewById(R.id.datePicker);
 Button buttonSubmit = (Button) mView.findViewById(R.id.date_picker_submit_button);
 Button buttonCancel = (Button) mView.findViewById(R.id.date_picker_cancel_button);

 mBuilder.setView(mView);

 // TODO: 16/03/2018 Add onClickListener to submit button
 buttonSubmit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

mListener.onDialogPositiveClick((DialogDELETE.this));

}
});

 // TODO: 16/03/2018 Add onClickListener to cancel button
 buttonCancel.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

mListener.onDialogNegativeClick((DialogDELETE.this));

}
});
 *
 *
 *
 *
 *
 *
 * */