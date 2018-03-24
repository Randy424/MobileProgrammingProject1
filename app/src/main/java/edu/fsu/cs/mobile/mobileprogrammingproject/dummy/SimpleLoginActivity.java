package edu.fsu.cs.mobile.mobileprogrammingproject.dummy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * Created by Aaron on 3/24/2018.
 */

public class SimpleLoginActivity extends AppCompatActivity {
    private EditText mPassword;
    private EditText mEmail;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }}
