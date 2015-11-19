package com.osu.pzcg.carpool.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by peihongzhong on 11/15/15.
 */
public class SettingAsync extends AsyncTask<String, Void, String> {
    private Context context;

    public SettingAsync(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];
            String place1_id = params[1];
            String place2_id = params[2];
            String place1_lng = params[3];
            String place1_lat = params[4];
            String place1_name = params[5];
            String place2_lng = params[6];
            String place2_lat = params[7];
            String place2_name = params[8];
//
//            if(place2_id == null){
//                place2_id = "";
//                place2_lng = "";
//                place2_lat = "";
//                place1_name = "";
//            }
            String link = "http://pzcg.biz/user_setting.php?user="+ URLEncoder.encode(username)+ "&home="+URLEncoder.encode(place1_id)
                    +"&work="+URLEncoder.encode(place2_id)+ "&longitude1=" +URLEncoder.encode(place1_lng)+"&lattitude1="
                    +URLEncoder.encode(place1_lat)+ "&home_name="+ URLEncoder.encode(place1_name)+"&longitude2="+URLEncoder.encode(place2_lng)
                    +"&lattitude2="+URLEncoder.encode(place2_lat)+ "&work_name="+URLEncoder.encode(place2_name);


            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        System.out.println(result);
    }
}