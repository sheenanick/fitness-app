package com.lifehackig.fitnessapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.lifehackig.fitnessapp.R;


public class SaveWorkoutDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_save_workout, null))
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog dialogView = (Dialog) dialog;
                        EditText workoutName = (EditText) dialogView.findViewById(R.id.workoutName);
                        String name = workoutName.getText().toString();
                        mListener.onDialogPositiveClick(SaveWorkoutDialogFragment.this, name);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SaveWorkoutDialogFragment.this);
                    }
                });
        return builder.create();
    }


    public interface SaveWorkoutDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String workoutName);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    SaveWorkoutDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SaveWorkoutDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
