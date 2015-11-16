package com.osu.pzcg.carpool.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.osu.pzcg.carpool.Adapters.SpecialListAdapters;
import com.osu.pzcg.carpool.Info.ListCard;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.MainEventAsync;
import com.osu.pzcg.carpool.async.SearchCategoryAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by peihongzhong on 10/31/15.
 */
public class HelpFragment extends Fragment {
    public Button createButton;
    public Button join;
    public Spinner category;
    public ListView listView_main;
    public SpecialListAdapters SpecialListAdapters;
    public String result;
    public JSONArray jArray_event;
    public String myUserId;
    public static MainActivity instance = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);
        createButton = (Button) view.findViewById(R.id.create_event);
        listView_main = (ListView)view.findViewById(R.id.listView_special);
        join = (Button) view.findViewById(R.id.join);
        // default content
        try {
            result = new MainEventAsync(getActivity()).execute().get();
            Log.i("z",result);
        }
        catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        myUserId = getActivity().getIntent().getStringExtra("user");
        SpecialListAdapters = new SpecialListAdapters(getActivity(), getItems(),myUserId);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SpecialActivity.class);
                intent.putExtra("user", myUserId);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        listView_main.setAdapter(SpecialListAdapters);

        category = (Spinner) view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category.setAdapter(adapter);


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                Log.i("zzzz", category);

                if (category == "All") {
                    try {
                        result = new MainEventAsync(getActivity()).execute().get();
                        //Log.i("z", result);
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }
                } else {
                    try {
                        result = new SearchCategoryAsync(getActivity()).execute(category).get();
                       // Log.i("z", result);
                    } catch (ExecutionException | InterruptedException ei) {
                        ei.printStackTrace();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public List<ListCard> getItems()
    {
        List<ListCard> mCards=new ArrayList<ListCard>();
        try{
            jArray_event = new JSONArray(result);
            JSONObject json_data=null;
            for(int i=0;i<jArray_event.length();i++){
                json_data = jArray_event.getJSONObject(i);
                String name = json_data.getString("user_name");
                String time = json_data.getString("time");
                String event_name = json_data.getString("event_name");
                String departure = json_data.getString("depName");
                String seats = json_data.getString("available_seats");
                ListCard mCard=new ListCard(name,time,event_name,departure,seats);

                mCards.add(mCard);
            }
        }catch(JSONException e1){
        }

        return mCards;

    }

}
