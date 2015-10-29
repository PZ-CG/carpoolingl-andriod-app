package com.osu.pzcg.carpool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.RegisterAsync;

import java.util.concurrent.ExecutionException;


/**
 * Created by GoThumbers on 10/9/15.
 */
public class RegisterActivity extends Activity{
    EditText username;
    EditText password;
    Button register;
    public static String RESULT;
    String un,pw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Log.i("LifeCycle", "onCreate()");

        username = (EditText) findViewById(R.id.reg_username);
        password = (EditText) findViewById(R.id.reg_password);
        register = (Button) findViewById(R.id.reg_register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    un = username.getText().toString();
                    pw = password.getText().toString();
                    RESULT = new RegisterAsync(RegisterActivity.this).execute(un, pw).get();
                    }
                catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

                System.out.println(RESULT);
                if(RESULT.indexOf("successful")!=-1 ) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("user", un);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LifeCycle","onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LifeCycle", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LifeCycle", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LifeCycle", "onDestroy()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LifeCycle", "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LifeCycle", "onRestart()");
    }
//    Handler handler = new Handler(){
//
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case 1:
//                    String result = (String)msg.obj;
//                    System.out.println("hhhhhhhhhhhhhhhhh"+result);
//                    if(result.indexOf("successful")!=-1 ) {
//                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                        intent.putExtra("user", un);
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    };
}
