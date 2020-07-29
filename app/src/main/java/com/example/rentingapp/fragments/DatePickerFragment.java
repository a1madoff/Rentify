package com.example.rentingapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.rentingapp.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    int year;
    int month;
    int day;
    long minDate;

    public DatePickerFragment(Calendar cal) {
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.minDate = System.currentTimeMillis() - 1000;
    }

    public DatePickerFragment(Calendar cal, long minDate) {
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.minDate = Math.max(minDate, System.currentTimeMillis() - 1000);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Uses the current time as the default values for the picker

        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogStyle, listener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(minDate);

        return datePickerDialog;
    }
}
