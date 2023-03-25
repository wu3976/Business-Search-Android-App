package io.github.wu3976.csci571hw9;

import android.app.Dialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {

    private EditText d_start_time_input;
    public TimePickerFragment(EditText d_start_time_input) {
        this.d_start_time_input = d_start_time_input;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        d_start_time_input.setText(String.format("%02d:%02d", hour, minute));
        this.dismiss();
    }
}
