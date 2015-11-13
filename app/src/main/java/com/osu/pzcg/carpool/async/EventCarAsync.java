package com.osu.pzcg.carpool.async;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;

public class EventCarAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public EventCarAsync(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        try{
            String username = params[0];
            String time = params[1];
            String dep_id = params[2];
            String seats_sum = params[3];
            String available_seats = params[4];
            String place1_lng = params[5];
            String place1_lat = params[6];
            String place1_name = params[7];
            String event_name = params[8];
            String category = params[9];

            String link = "http://pzcg.biz/publish_event_carpool.php?initiator_name="+ URLEncoder.encode(username)+"&time="+URLEncoder.encode(time)+
                    "&departure_id n="+URLEncoder.encode(dep_id)+"&seats_sum="+URLEncoder.encode(seats_sum)+
                    "&available_seats="+URLEncoder.encode(available_seats)+"&longitude1="+URLEncoder.encode(place1_lng)+"&lattitude1="+URLEncoder.encode(place1_lat)+
                    "&place_name1="+ URLEncoder.encode(place1_name) + "&event_name="+URLEncoder.encode(event_name) + "category="+URLEncoder.encode(category);

            //String link
            System.out.println(link);

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();

            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result) {

    }

}
