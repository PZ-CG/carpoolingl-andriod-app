package com.osu.pzcg.carpool.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.osu.pzcg.carpool.LoginAsync;
import com.osu.pzcg.carpool.R;


public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);


        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username.getText().toString();
                String pw = password.getText().toString();
                new LoginAsync(LoginActivity.this).execute(un, pw);
            }
        });
    }
}
