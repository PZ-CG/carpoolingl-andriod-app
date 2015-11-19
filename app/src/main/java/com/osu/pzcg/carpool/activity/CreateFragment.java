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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static MainActivity instance = null;
    public String request_result;
    public int sort_func;
    private final int OFFER_CAR = 1;
    private final int WANT_CAR = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);
        offerButton = (Button) view.findViewById(R.id.offer);
        listView_main = (ListView)view.findViewById(R.id.listView_main);
        myUserId = getActivity().getIntent().getStringExtra("user");

        cardListAdpters = new CardListAdpters(getActivity(), getItems(),myUserId);
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
                startActivityForResult(intent, WANT_CAR);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        request_result = data.getExtras().getString("request");
        sort_func = data.getExtras().getInt("sort_func",3);
        String request_date = data.getExtras().getString("request_date");
        String request_time = data.getExtras().getString("request_time");
        String request_combined = request_date+" "+request_time;
        double request_dep_lng = data.getExtras().getDouble("request_dep_lng");
        double request_dep_lat = data.getExtras().getDouble("request_dep_lat");
        double request_des_lng = data.getExtras().getDouble("request_des_lng");
        double request_des_lat = data.getExtras().getDouble("request_des_lat");

        Log.i("chaoqun", sort_func + "");

        try {
            Log.i("chaoqun","aaaaaaaa");
            SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm");
            Date req_time=sdf.parse(request_combined);
            Log.i("chaoqun","bbbbbbb");

            List<ListCard> temp_cards=new ArrayList<>();
            List<ListCard> mCards=new ArrayList<>();

            if (sort_func == 1) {

                    JSONArray jsonArray = new JSONArray(request_result);
                    JSONObject jsonData = null;
                Log.i("chaoqun", jsonArray.length() + "");

                    Map<String, Double> diffMap = new HashMap<String, Double>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonData = jsonArray.getJSONObject(i);
                        String name = jsonData.getString("user_name");
                        String time = jsonData.getString("time");
                        String dep = jsonData.getString("depName");
                        String des = jsonData.getString("desName");
                        String seats = jsonData.getString("available_seats");
                        Date temp_time=sdf.parse(time);
                        ListCard mCard = new ListCard(name, time, dep, des, seats);
                        temp_cards.add(mCard);
                        double diff = Math.abs(req_time.getTime()-temp_time.getTime());
                        diffMap.put(i + "", diff);
                    }
                    System.out.println("Before ranking：" + diffMap);
                    List<Map.Entry<String, Double>> sorted_diff = new ArrayList<Map.Entry<String, Double>>(diffMap.entrySet());
                    Collections.sort(sorted_diff, new Comparator<Map.Entry<String, Double>>() {
                        public int compare(Map.Entry<String, Double> o1,
                                           Map.Entry<String, Double> o2) {
                            return (o1.getValue()).toString().compareTo(o2.getValue().toString());
                        }
                    });
                    System.out.println("After ranking：" + sorted_diff);


                    for (int i = 0; i < sorted_diff.size(); i++) {
                        String str = sorted_diff.get(i).toString();
                        int num = Integer.parseInt(str.substring(0, 1));
                        Log.i("chaoqun",num+"");
                        ListCard lc = temp_cards.get(num);
                        mCards.add(lc);
                    }
                    for(int i=0;i<mCards.size();i++){
                        Log.i("chaoqun",mCards.get(i).getTime());
                    }

                }
            if (sort_func == 2) {

                JSONArray jsonArray = new JSONArray(request_result);
                JSONObject jsonData = null;
                Log.i("chaoqun", jsonArray.length() + "");

                Map<String, Double> diffMap = new HashMap<String, Double>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);
                    String name = jsonData.getString("user_name");
                    String time = jsonData.getString("time");
                    String dep = jsonData.getString("depName");
                    String des = jsonData.getString("desName");
                    String seats = jsonData.getString("available_seats");
                    double dep_lng = Double.parseDouble(jsonData.getString("depLng").trim());
                    double dep_lat = Double.parseDouble(jsonData.getString("depLat").trim());
                    double des_lng = Double.parseDouble(jsonData.getString("desLng").trim());
                    double des_lat = Double.parseDouble(jsonData.getString("desLat").trim());
                    ListCard mCard = new ListCard(name, time, dep, des, seats);
                    temp_cards.add(mCard);
                    double diff = Math.abs(request_dep_lng-dep_lng)+Math.abs(request_dep_lat-dep_lat)+Math.abs(request_des_lng-des_lng)+Math.abs(request_des_lat-des_lat);
                    diffMap.put(i + "", diff);
                }
                System.out.println("Before ranking：" + diffMap);
                List<Map.Entry<String, Double>> sorted_diff = new ArrayList<Map.Entry<String, Double>>(diffMap.entrySet());
                Collections.sort(sorted_diff, new Comparator<Map.Entry<String, Double>>() {
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2) {
                        return (o1.getValue()).toString().compareTo(o2.getValue().toString());
                    }
                });
                System.out.println("After ranking：" + sorted_diff);


                for (int i = 0; i < sorted_diff.size(); i++) {
                    String str = sorted_diff.get(i).toString();
                    int num = Integer.parseInt(str.substring(0, 1));
                    Log.i("chaoqun",num+"");
                    ListCard lc = temp_cards.get(num);
                    mCards.add(lc);
                }
                for(int i=0;i<mCards.size();i++){
                    Log.i("chaoqun",mCards.get(i).getTime());
                }

            }
            cardListAdpters = new CardListAdpters(getActivity(), mCards, myUserId);
            listView_main.setAdapter(cardListAdpters);
        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }
}
