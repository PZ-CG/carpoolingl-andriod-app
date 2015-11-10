package com.osu.pzcg.carpool.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.osu.pzcg.carpool.R;
import com.osu.pzcg.carpool.async.FbAsync;
import com.osu.pzcg.carpool.async.LoginAsync;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button register;
    LoginButton fbLoginButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    CallbackManager callbackManager;
    public static String RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String saved_un = pref.getString("un", "");
        String saved_pw = pref.getString("pw", "");
        username.setText(saved_un);
        password.setText(saved_pw);

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("User:" + loginResult.getAccessToken().getUserId());
                new FbAsync(LoginActivity.this).execute(loginResult.getAccessToken().getUserId());
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("user", loginResult.getAccessToken().getUserId());
                startActivity(intent);
                LoginActivity.this.finish();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this,"Canceled",Toast.LENGTH_LONG);


            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG);
            }
        });



        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString();
                String pw = password.getText().toString();
                try {
                    un = username.getText().toString();
                    pw = password.getText().toString();
                    RESULT = new LoginAsync(LoginActivity.this).execute(un, pw).get();
                }
                catch (ExecutionException | InterruptedException ei) {
                    ei.printStackTrace();
                }

                if(RESULT.indexOf("successful")!=-1 ) {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("user", un);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    editor = pref.edit();
                    editor.putString("un", un);
                    editor.putString("pw", pw);
                    editor.putString("un", un);
                    editor.putString("pw", pw);
                    editor.commit();
                }else{
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_LONG).show();
                }
            }
        });


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
