package com.osu.pzcg.carpool;

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

/**
 * Created by GoThumbers on 10/7/15.
 */
class LoginAsync extends AsyncTask<String, Void, String> {
    private Context context;
    public LoginAsync(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            String username = params[0];
            String password = params[1];
            String link = "http://pzcg.biz/register.php?user_name="+username+"&pass_word="+password;
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
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        System.out.println(result);
    }
}


