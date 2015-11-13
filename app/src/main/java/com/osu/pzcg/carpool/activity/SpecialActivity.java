package com.osu.pzcg.carpool.activity;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.OfferCarAsync;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SpecialActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private Button time;
    private Button date;
    private Button choose_des;
    private Button publish_event;
    private ImageButton common_dep;
    private Spinner event;
    private AlertDialog.Builder builder;
    private String str;
    private EditText show_des;
    private int PLACE_PICKER_REQUEST;
    private String place_id;
    public String myUserId;
    public Double place_lat;
    public Double place_lng;
    public CharSequence place_add;
    public CharSequence place_name;
    private GoogleApiClient mGoogleApiClient;
    public static String OFFER_DATE_EVENT = null;
    public static String OFFER_TIME_EVENT = null;
    private String seatsSum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        Calendar c = Calendar.getInstance();
        time = ((Button) findViewById(R.id.publish_time_special));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        date = ((Button) findViewById(R.id.publish_date_special));
        date.setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));

        common_dep = (ImageButton) findViewById(R.id.publish_favorite_places_event);
        builder = new AlertDialog.Builder(this);
        show_des = (EditText) findViewById(R.id.place2);
        event = (Spinner) findViewById(R.id.events);
        choose_des = (Button) findViewById(R.id.choose_des_event);
        myUserId = getIntent().getStringExtra("user");

        publish_event = (Button)findViewById(R.id.publish_event);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApiIfAvailable(Places.GEO_DATA_API)
                .addApiIfAvailable(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // spinner

        event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                seatsSum = ""+(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.special_event, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        event.setAdapter(adapter);

        //common place button
        str = getResources().getStringArray(R.array.place)[0];
        common_dep.setOnClickListener(new View.OnClickListener() {
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

//        // map button
        choose_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PLACE_PICKER_REQUEST = 3;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(SpecialActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), SpecialActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(SpecialActivity.this, "Google Play Services is not available.", Toast.LENGTH_LONG).show();
                }
            }
        });
        //time button
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TIME",3);
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        //date button
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE",3);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        //publish event button
        publish_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myUserId != null && OFFER_DATE_EVENT != null && OFFER_TIME_EVENT != null && place_id != null && place_id != null && seatsSum != null) {
                        String result = new OfferCarAsync(SpecialActivity.this).execute(myUserId, OFFER_DATE_EVENT + " " + OFFER_TIME_EVENT, place_id, seatsSum,
                                seatsSum,place_lng+"",place_lat+"",place_name+"").get();
                        Log.i("guo", result);

                    } else {
                        Toast.makeText(SpecialActivity.this, "Please fill are the fields", Toast.LENGTH_LONG).show();
                    }
                }catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

                Intent intent = new Intent(SpecialActivity.this, MainActivity.class);
                intent.putExtra("user",myUserId);
                startActivity(intent);
                SpecialActivity.this.finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                place_name = place.getName();
                show_des.setText(place.getName());
                place_id = place.getId();
                place_lat = place.getLatLng().latitude;
                place_lng = place.getLatLng().longitude;
                place_add = place.getAddress();
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
            //Intent intent = new Intent(this, MainActivity.class);
            //intent.putExtra("user",myUserId);
            //startActivity(intent);
            //getFragmentManager().popBackStack();
            SpecialActivity.this.finish();

        }

        return false;

    }



}
