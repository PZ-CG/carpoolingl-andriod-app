package com.osu.pzcg.carpool.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.osu.pzcg.carpool.R;

import java.util.Calendar;
/**
 * Created by peihongzhong on 11/6/15.
 */
public class WantCarActivity extends AppCompatActivity {
    private ImageButton button_com_dep;
    private ImageButton button_com_des;
    private ImageButton button_cur_address_;
    private AlertDialog.Builder builder = null;
    private String str = null;
    private EditText show_dep;
    private EditText show_des;
    private Button date;
    private Button time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_car);

        Calendar c = Calendar.getInstance();

        //show current time on time button

        time = ((Button) findViewById(R.id.publish_time_want));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));

        //show current date on date button

        date = ((Button) findViewById(R.id.publish_date_want));
        date.setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));

        //get common places button
        button_com_dep = (ImageButton)findViewById(R.id.publish_favorite_places_want);
        button_com_des = (ImageButton)findViewById(R.id.publish_favorite_places2_want);

        builder = new AlertDialog.Builder(this);
        show_dep = (EditText)findViewById(R.id.place_want);
        show_des = (EditText)findViewById(R.id.place2_want);

        //set default value for str
        str = getResources().getStringArray(R.array.place)[0];

        //button for setting date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",2);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);

                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        //button for setting time
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",2);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });

        //press button to choose common place
        button_com_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Places");
                builder.setSingleChoiceItems(getResources().getStringArray(R.array.place), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str = getResources().getStringArray(R.array.place)[which];
                    }
                });
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        show_dep.setText(str);
                    }
                });
                builder.create().show();
            }
        });

        button_com_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Places");
                builder.setSingleChoiceItems(getResources().getStringArray(R.array.place), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        str = getResources().getStringArray(R.array.place)[which];
                    }
                });
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        show_des.setText(str);
                    }
                });
                builder.create().show();
            }
        });
    }


}
