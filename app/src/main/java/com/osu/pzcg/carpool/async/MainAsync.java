package com.osu.pzcg.carpool.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
/**
 * Created by GoThumbers on 10/7/15.
 */
public class MainAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public static String RESULT;
    Handler mHandler;
    JSONArray jArray;
    public MainAsync(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            String link = "http://pzcg.biz/select_all_carpool.php";
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



