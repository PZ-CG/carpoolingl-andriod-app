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
public class OfferCarActivity extends Activity {
    private Spinner availableSeats;
    private EditText show_dep;
    private EditText show_des;
    private ImageButton button_com_dep;
    private ImageButton button_com_des;
    private String str = null;
    private AlertDialog.Builder builder = null;
    private Button time;
    private Button date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_car);

        addListenerOnAvailableSeats();
        Calendar c = Calendar.getInstance();

        time = ((Button) findViewById(R.id.publish_time));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        date = ((Button) findViewById(R.id.publish_date));
        date.setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));
        show_dep = (EditText) findViewById(R.id.place);
        show_des = (EditText) findViewById(R.id.place2);

        button_com_dep = (ImageButton) findViewById(R.id.publish_favorite_places);
        builder = new AlertDialog.Builder(this);
        button_com_dep.setOnClickListener(text_radio_dialog_listener);

        str = getResources().getStringArray(R.array.place)[0];
        button_com_des = (ImageButton)findViewById(R.id.publish_favorite_places2);
        button_com_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle(getResources().getString(R.string.place));
                builder.setSingleChoiceItems(getResources().getStringArray(R.array.place), 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        str = getResources().getStringArray(R.array.place)[which];

                    }
                });
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        show_des.setText(str);
                    }
                });

                builder.create().show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",1);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",1);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
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
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    show_dep.setText(str);
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

}

