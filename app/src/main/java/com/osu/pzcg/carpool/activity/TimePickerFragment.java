package com.osu.pzcg.carpool.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import com.osu.pzcg.carpool.R;

import java.util.Calendar;

/**
 * Created by peihongzhong on 11/4/15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private int button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle bundle = this.getArguments();
        if(bundle != null){
            button = bundle.getInt("TIME",1);
        }
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (button == 1){
            Log.w("DatePicker", "Date = " + minute);
            ((TextView) getActivity().findViewById(R.id.publish_time)).setText(hourOfDay + ":" + minute);
        } else if (button == 2){
            Log.w("DatePicker", "Date = " + minute);
            ((TextView) getActivity().findViewById(R.id.publish_time_want)).setText(hourOfDay + ":" + minute);
        }
    }
}
