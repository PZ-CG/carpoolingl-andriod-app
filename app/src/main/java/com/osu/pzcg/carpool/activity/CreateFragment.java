package com.osu.pzcg.carpool.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.osu.pzcg.carpool.Adapters.CardListAdpters;
import com.osu.pzcg.carpool.Info.ListCard;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.MainAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by peihongzhong on 10/31/15.
 */
public class CreateFragment extends Fragment {
    public Button offerButton;
    public Button wantButton;

    public ListView listView_main;
    public CardListAdpters cardListAdpters;
    public String result;
    public JSONArray jArray;
    public String myUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);
        offerButton = (Button) view.findViewById(R.id.offer);
        listView_main = (ListView)view.findViewById(R.id.listView_main);
        myUserId = getActivity().getIntent().getStringExtra("user");
        cardListAdpters = new CardListAdpters(getActivity(), getItems());
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),OfferCarActivity.class);
                intent.putExtra("user", myUserId);
                startActivity(intent);
                getActivity().finish();
            }
        });
        wantButton = (Button)view.findViewById(R.id.want);
        wantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),WantCarActivity.class);
                intent.putExtra("user",myUserId);
                startActivity(intent);
            }
        });
        listView_main.setAdapter(cardListAdpters);
        return view;
    }
    public List<ListCard> getItems()
    {
        List<ListCard> mCards=new ArrayList<ListCard>();
        try {
            result = new MainAsync(getActivity()).execute().get();
            Log.i("guo",result);
        }
        catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
        try{
            jArray = new JSONArray(result);
            JSONObject json_data=null;
            for(int i=0;i<jArray.length();i++){
                json_data = jArray.getJSONObject(i);
                String name = json_data.getString("user_name");
                String time = json_data.getString("time");
                String dep = json_data.getString("depName");
                String des = json_data.getString("desName");
                String seats = json_data.getString("available_seats");
                ListCard mCard=new ListCard(name, time,dep,des,seats);
                mCards.add(mCard);
            }
        }catch(JSONException e1){
        }
        return mCards;
    }


}
