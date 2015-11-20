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
 * Created by peihongzhong on 11/13/15.
 */
public class SpecialEventJoinAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public static String RESULT;
    public SpecialEventJoinAsync(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];
            String time = params[1];
            String event_name = params[2];
            String departure = params[3];
            String seats = params[4];
            String current = params[5];
            //Log.i("just for test",username+time+event_name+departure+seats+current);

            String link = "http://pzcg.biz/join_event_carpool.php?initiator_name="+ URLEncoder.encode(username)+"&time="+URLEncoder.encode(time)+
                    "&user_name="+URLEncoder.encode(current)+"&available_seats="+URLEncoder.encode(seats);
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
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        System.out.println(result);
    }
}
