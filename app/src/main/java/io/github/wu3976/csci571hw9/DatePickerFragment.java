package io.github.wu3976.csci571hw9;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

    TextView d_date_input;
    public DatePickerFragment(TextView d_date_input) {
        this.d_date_input = d_date_input;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        d_date_input.setText((month+1) + "-" + day + "-" + year);
    }
}
