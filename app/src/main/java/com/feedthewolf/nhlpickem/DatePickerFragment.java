package com.feedthewolf.nhlpickem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nick on 1/14/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2017, Calendar.JUNE, 17, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        Date maxDate = cal.getTime();

        cal.set(2016, Calendar.OCTOBER, 12, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        Date minDate = cal.getTime();

        dialog.getDatePicker().setMinDate(minDate.getTime());
        dialog.getDatePicker().setMaxDate(maxDate.getTime());

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        /*
        if (context instanceof Activity){
            a=(Activity) context;
        }
        */

        try {
            mCallback = (DatePickerDialog.OnDateSetListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = new Activity();

        if (context instanceof Activity){
            a=(Activity) context;
        }

        try {
            mCallback = (DatePickerDialog.OnDateSetListener) a;
        }catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement OnArticleSelectedListener");
        }

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        mCallback.onDateSet(view, year, month, day);
    }
}
