package com.osu.pzcg.carpool.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.osu.pzcg.carpool.R;

import java.util.Calendar;

/**
 * Created by peihongzhong on 11/3/15.
 */
public class OfferCar extends Activity {
    private Spinner availableSeats;
    private EditText show;
    private ImageButton button;
    private String str = null;
    private AlertDialog.Builder builder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_car);
        setTitle("offer a car!");
        addListenerOnAvailableSeats();
        Calendar c = Calendar.getInstance();
        ((Button) findViewById(R.id.publish_time)).setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        ((Button) findViewById(R.id.publish_date)).setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));
        show = (EditText) findViewById(R.id.place);
        button = (ImageButton) findViewById(R.id.publish_favorite_places);
        builder = new AlertDialog.Builder(this);
        button.setOnClickListener(text_radio_dialog_listener);
        str = getResources().getStringArray(R.array.place)[0];
    }

    View.OnClickListener text_radio_dialog_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            builder.setTitle(getResources().getString(R.string.place));
            builder.setSingleChoiceItems(getResources().getStringArray(R.array.place), 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                str = getResources().getStringArray(R.array.place)[which];

            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                show.setText(str);
            }
        });

        builder.create().show();

    }
    };


    //add items into spinner dynamically
    public void addListenerOnAvailableSeats(){
        availableSeats = (Spinner) findViewById(R.id.available_seats);
        availableSeats.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}

