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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.osu.pzcg.carpool.Adapters.PlaceArrayAdapter;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.OfferCarAsync;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class OfferCarActivity extends AppCompatActivity implements ConnectionCallbacks,OnConnectionFailedListener{
    private Spinner availableSeats;
    private ImageButton button_com_dep;
    private ImageButton button_com_des;
    private String str = null;
    private AlertDialog.Builder builder = null;
    private Button time;
    private Button date;
    private Button choose_dep;
    private Button choose_des;
    private Button publish;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST1;
    private int PLACE_PICKER_REQUEST2;
    private String place1_id;
    private String place2_id;
    public static String OFFER_DATE = null;
    public static String OFFER_TIME = null;
    public String myUserId;
    public String seatsSum;
    public String placeId;
    public String des_dep;
    public Double place1_lat;
    public Double place1_lng;
    public Double place2_lat;
    public Double place2_lng;
    public CharSequence place1_add;
    public CharSequence place2_add;
    public CharSequence place1_name;
    public CharSequence place2_name;
    private PlaceArrayAdapter mAdapter;
    private PlaceArrayAdapter mAdapter_des;
    private AutoCompleteTextView mAutocompleteTextView;
    private AutoCompleteTextView mAutocompleteTextView_des;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String TAG = "Place autocomplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_offer_car);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteTextView= (AutoCompleteTextView) findViewById(R.id.dep_offer);
        mAutocompleteTextView_des = (AutoCompleteTextView) findViewById(R.id.des_offer);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                des_dep = "dep";
                PlaceArrayAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                placeId = String.valueOf(item.placeId);
                Log.i(TAG, "Autocomplete item selected: " + item.description);
                /*
                Issue a request to the Places Geo Data API to retrieve a Place object with additional
                details about the place.
                */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            }
        });

        mAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_MOUNTAIN_VIEW , null);
        mAutocompleteTextView.setAdapter(mAdapter);


        mAutocompleteTextView_des.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                des_dep = "des";
                PlaceArrayAdapter.PlaceAutocomplete item_des = mAdapter_des.getItem(position);
                placeId = String.valueOf(item_des.placeId);
                Log.i(TAG, "aaaaaaa: " + item_des.description);
                /*
                Issue a request to the Places Geo Data API to retrieve a Place object with additional
                details about the place.
                */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        });
        mAdapter_des = new PlaceArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
                mGoogleApiClient, BOUNDS_MOUNTAIN_VIEW , null);
        mAutocompleteTextView_des.setAdapter(mAdapter_des);


        addListenerOnAvailableSeats();
        Calendar c = Calendar.getInstance();
        choose_dep = (Button)findViewById(R.id.choose_dep);
        choose_des = (Button)findViewById(R.id.choose_des);
        time = ((Button) findViewById(R.id.publish_time));
        time.setText(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE));
        date = ((Button) findViewById(R.id.publish_date));
        date.setText(c.get(Calendar.MONTH) + 1 + "-" + c.get(Calendar.DATE) + "-" + c.get(Calendar.YEAR));
        publish = (Button) findViewById(R.id.publish);
        myUserId = getIntent().getStringExtra("user");

        button_com_dep = (ImageButton) findViewById(R.id.publish_favorite_places);
        builder = new AlertDialog.Builder(this);
        button_com_dep.setOnClickListener(text_radio_dialog_listener);

        str = getResources().getStringArray(R.array.place)[0];
        button_com_des = (ImageButton)findViewById(R.id.publish_favorite_places2);

        choose_dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PLACE_PICKER_REQUEST1 = 1;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(OfferCarActivity.this), PLACE_PICKER_REQUEST1);
                } catch (GooglePlayServicesRepairableException e) {
                        GooglePlayServicesUtil
                                .getErrorDialog(e.getConnectionStatusCode(), OfferCarActivity.this, 0);
                    } catch (GooglePlayServicesNotAvailableException e) {
                        Toast.makeText(OfferCarActivity.this, "Google Play Services is not available.",Toast.LENGTH_LONG).show();
                    }
            }
        });
        choose_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PLACE_PICKER_REQUEST2 = 2;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    startActivityForResult(builder.build(OfferCarActivity.this), PLACE_PICKER_REQUEST2);
                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), OfferCarActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(OfferCarActivity.this, "Google Play Services is not available.",Toast.LENGTH_LONG).show();
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

                        mAutocompleteTextView_des.setText(str);
                    }
                });

                builder.create().show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("DATE", 1);

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
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myUserId != null && OFFER_DATE != null && OFFER_TIME != null && place1_id != null && place2_id != null && seatsSum != null) {
                        String result = new OfferCarAsync(OfferCarActivity.this).execute(myUserId, OFFER_DATE + " " + OFFER_TIME, place1_id, place2_id, seatsSum,
                                seatsSum, place1_lng + "", place1_lat + "", place1_name + "", place2_lng + "", place2_lat + "", place2_name + "").get();
                        //Log.i("guo",result);
                        Toast.makeText(OfferCarActivity.this, "Publish successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OfferCarActivity.this, MainActivity.class);
                        intent.putExtra("user",myUserId);
                        startActivity(intent);
                        OfferCarActivity.this.finish();
                    } else {
                        Toast.makeText(OfferCarActivity.this, "Please fill are the fields", Toast.LENGTH_LONG).show();
                    }
                } catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

            }
        });
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            if(des_dep == "dep") {
                place1_name = place.getName();
                place1_id = place.getId();
                place1_lat = place.getLatLng().latitude;
                place1_lng = place.getLatLng().longitude;
                Log.i(TAG, "Place details received: " + place.getName());
            } else if(des_dep == "des"){
                place2_name = place.getName();
                place2_id = place.getId();
                place2_lat = place.getLatLng().latitude;
                place2_lng = place.getLatLng().longitude;
                Log.i(TAG, "Place details received: " + place.getName());
            }
                places.release();
        }
    };

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

                    mAutocompleteTextView.setText(str);
                }
            });

            builder.create().show();

        }
    };

    //add items into spinner dynamically
    public void addListenerOnAvailableSeats(){
        availableSeats = (Spinner) findViewById(R.id.available_seats);
        availableSeats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seatsSum = ""+(position+1);
                //Log.i("guo",seatsSum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                place1_name = place.getName();
                mAutocompleteTextView.setText(place.getName());
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
                mAutocompleteTextView_des.setText(place.getName());
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
            Intent intent = new Intent(OfferCarActivity.this, MainActivity.class);
            intent.putExtra("user",myUserId);
            startActivity(intent);
            OfferCarActivity.this.finish();

        }

        return false;

    }


}
