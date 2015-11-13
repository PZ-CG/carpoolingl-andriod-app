package com.osu.pzcg.carpool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.osu.pzcg.carpool.R;

import java.util.Calendar;

/**
 * Created by peihongzhong on 11/4/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private int button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Bundle bundle = this.getArguments();
        if(bundle != null){
            button = bundle.getInt("DATE",1);
        }
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(button == 1){
            Log.w("DatePicker", "Date = " + year);
            ((Button) getActivity().findViewById(R.id.publish_date)).setText(month + 1 + "-" + day + "-" + year);
            OfferCarActivity.OFFER_DATE = month + 1 + "-" + day + "-" + year;
        } else if (button == 2){
            Log.w("DatePicker", "Date = " + year);
            ((Button) getActivity().findViewById(R.id.publish_date_want)).setText(month + 1 + "-" + day + "-" + year);
        } else if(button == 3){
            ((Button) getActivity().findViewById(R.id.publish_date_special)).setText(month + 1 + "-" + day + "-" + year);
            SpecialActivity.OFFER_DATE_EVENT = month + 1 + "-" + day + "-" + year;
        }
    }
}