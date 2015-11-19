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
 * Created by peihongzhong on 11/19/15.
 */
public class GetSettingInfoAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public GetSettingInfoAsync(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            String username = params[0];
            String link = "http://pzcg.biz/get_setting_information.php?initiator_name="+ URLEncoder.encode(username);
            //String link
            //System.out.println(link);

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
