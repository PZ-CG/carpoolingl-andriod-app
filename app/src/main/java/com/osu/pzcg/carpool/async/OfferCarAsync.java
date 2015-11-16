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

/**
 * Created by GoThumbers on 11/8/15.
 */

public class OfferCarAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public OfferCarAsync(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];
            String time = params[1];
            String dep_id = params[2];
            String des_id = params[3];
            String seats_sum = params[4];
            String available_seats = params[5];
            String place1_lng = params[6];
            String place1_lat = params[7];
            String place1_name = params[8];
            String place2_lng = params[9];
            String place2_lat = params[10];
            String place2_name = params[11];


            String link = "http://pzcg.biz/publish_new_carpool.php?initiator_name="+ URLEncoder.encode(username)+"&time="+URLEncoder.encode(time)+
                    "&departure="+URLEncoder.encode(dep_id)+"&destination="+URLEncoder.encode(des_id)+"&seats_sum="+URLEncoder.encode(seats_sum)+
                    "&available_seats="+URLEncoder.encode(available_seats)+"&longitude1="+URLEncoder.encode(place1_lng)+"&lattitude1="+URLEncoder.encode(place1_lat)+
                    "&place_name1="+ URLEncoder.encode(place1_name)+"&longitude2="+URLEncoder.encode(place2_lng)+"&lattitude2="+URLEncoder.encode(place2_lat)+
                    "&place_name2="+URLEncoder.encode(place2_name);
//

            //String link = "http://pzcg.biz/register.php";
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



