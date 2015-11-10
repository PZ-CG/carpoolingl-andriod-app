package com.osu.pzcg.carpool.activity;

/**
 * Created by peihongzhong on 10/31/15.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.osu.pzcg.carpool.Adapters.HisListAdapters;
import com.osu.pzcg.carpool.Info.ListCard;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.HistoryAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReadFragment extends Fragment {
    public ListView listView_his;
    public String myUserId;
    public HisListAdapters hisListAdapters;
    public String result;
    public JSONArray jArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read, container, false);
        listView_his = (ListView)view.findViewById(R.id.listView_his);
        myUserId = getActivity().getIntent().getStringExtra("user");
        hisListAdapters = new HisListAdapters(getActivity(), getItems());
        listView_his.setAdapter(hisListAdapters);

        return view;
    }
    public List<ListCard> getItems()
    {
        List<ListCard> mCards=new ArrayList<ListCard>();
        try {
            result = new HistoryAsync(getActivity()).execute(myUserId).get();
            Log.i("guo", result);
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
                String peopleSum = json_data.getString("peopleSum");
                ListCard mCard=new ListCard(name, time,dep,des,peopleSum);
                mCards.add(mCard);
            }
        }catch(JSONException e1){
        }
        return mCards;
    }
}