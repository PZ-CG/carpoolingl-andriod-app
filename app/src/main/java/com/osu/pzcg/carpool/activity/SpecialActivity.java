package com.osu.pzcg.carpool.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.osu.pzcg.carpool.R;

import java.util.Calendar;

public class SpecialActivity extends AppCompatActivity {
    private Button time;
    private Button date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        Calendar c = Calendar.getInstance();
        time = ((Button) findViewById(R.id.publish_time_special));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        date = ((Button) findViewById(R.id.publish_date_special));
        date.setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));
    }
}
