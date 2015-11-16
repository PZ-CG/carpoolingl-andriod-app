package com.osu.pzcg.carpool.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.osu.pzcg.carpool.Adapters.PlaceArrayAdapter;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.SettingAsync;

import java.util.concurrent.ExecutionException;


/**
 * Created by peihongzhong on 11/15/15.
 */
public class SettingFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private Button setting;
    private String place1_id;
    private String place2_id;
    private String myUserId;
    public Double place1_lat;
    public Double place1_lng;
    public Double place2_lat;
    public Double place2_lng;
    public CharSequence place1_name;
    public CharSequence place2_name;
    private String home_work;
    private String placeId;
    private AutoCompleteTextView home;
    private AutoCompleteTextView work;
    private PlaceArrayAdapter adapter_home;
    private PlaceArrayAdapter adapter_work;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        myUserId = getActivity().getIntent().getStringExtra("user");
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        home = (AutoCompleteTextView) view.findViewById(R.id.home);
        work = (AutoCompleteTextView) view.findViewById(R.id.work);

        adapter_home = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_MOUNTAIN_VIEW , null);
        home.setAdapter(adapter_home);
        home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                home_work = "home";
                PlaceArrayAdapter.PlaceAutocomplete item_home = adapter_home.getItem(position);
                placeId = String.valueOf(item_home.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        });

        adapter_work = new PlaceArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,mGoogleApiClient,
                BOUNDS_MOUNTAIN_VIEW,null);
        work.setAdapter(adapter_work);

        work.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                home_work = "work";
                PlaceArrayAdapter.PlaceAutocomplete item_work = adapter_work.getItem(position);
                placeId = String.valueOf(item_work.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        });

        setting = (Button) view.findViewById(R.id.save_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(myUserId != null && (place1_id != null || place2_id != null)){
                        String result = new SettingAsync(getActivity()).execute(myUserId, place1_id, place2_id, place1_lng + "",
                                place1_lat + "", place1_name + "", place2_lng + "", place2_lat + "", place2_name + "").get();
                        Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_LONG).show();
                    }

                }catch (ExecutionException |InterruptedException ei) {
                    ei.printStackTrace();
                }
            }
        });
        return view;

    }
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                //Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            if(home_work == "home") {
                place1_name = place.getName();
                place1_id = place.getId();
                place1_lat = place.getLatLng().latitude;
                place1_lng = place.getLatLng().longitude;
            } else if(home_work == "work"){
                place2_name = place.getName();
                place2_id = place.getId();
                place2_lat = place.getLatLng().latitude;
                place2_lng = place.getLatLng().longitude;
            }
            places.release();
        }
    };

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
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
}
