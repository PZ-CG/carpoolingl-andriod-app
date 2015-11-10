package com.osu.pzcg.carpool.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.osu.pzcg.carpool.R;

import java.util.Calendar;
/**
 * Created by peihongzhong on 11/6/15.
 */
public class WantCarActivity extends AppCompatActivity implements ConnectionCallbacks,OnConnectionFailedListener {
    private ImageButton button_com_dep;
    private ImageButton button_com_des;
    private ImageButton button_cur_address_;
    private AlertDialog.Builder builder = null;
    private String str = null;
    private EditText show_dep;
    private EditText show_des;
    private Button date;
    private Button time;
    private Button choose_dep;
    private Button choose_des;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST1;
    private int PLACE_PICKER_REQUEST2;
    public String myUserId;
    public String seatsSum;
    private String place1_id;
    private String place2_id;
    public Double place1_lat;
    public Double place1_lng;
    public Double place2_lat;
    public Double place2_lng;
    public CharSequence place1_add;
    public CharSequence place2_add;
    public CharSequence place1_name;
    public CharSequence place2_name;
    private Button request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_car);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Calendar c = Calendar.getInstance();
        myUserId = getIntent().getStringExtra("user");

        //show current time on time button

        time = ((Button) findViewById(R.id.publish_time_want));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        choose_dep = (Button)findViewById(R.id.choose_dep_want);
        choose_des = (Button)findViewById(R.id.choose_des_want);
        request = (Button)findViewById(R.id.request);
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

        choose_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PLACE_PICKER_REQUEST1 = 1;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(WantCarActivity.this), PLACE_PICKER_REQUEST1);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), WantCarActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(WantCarActivity.this, "Google Play Services is not available.",Toast.LENGTH_LONG).show();
                }
            }
        });

        choose_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PLACE_PICKER_REQUEST2 = 2;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(WantCarActivity.this), PLACE_PICKER_REQUEST2);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), WantCarActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(WantCarActivity.this, "Google Play Services is not available.", Toast.LENGTH_LONG).show();
                }
            }
        });
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
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WantCarActivity.this, MainActivity.class);
                intent.putExtra("user",myUserId);
                startActivity(intent);
                WantCarActivity.this.finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                place1_name = place.getName();
                show_dep.setText(place.getName());
                place1_id = place.getId();
                place1_lat = place.getLatLng().latitude;
                place1_lng = place.getLatLng().longitude;
                place1_add = place.getAddress();
            }
        }
        else if (requestCode == PLACE_PICKER_REQUEST2) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                place2_name = place.getName();
                show_des.setText(place.getName());
                place2_id = place.getId();
                place2_lat = place.getLatLng().latitude;
                place2_lng = place.getLatLng().longitude;
                place2_add = place.getAddress();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent(WantCarActivity.this, MainActivity.class);
            intent.putExtra("user",myUserId);
            startActivity(intent);
            WantCarActivity.this.finish();

        }
        return false;
    }


}
