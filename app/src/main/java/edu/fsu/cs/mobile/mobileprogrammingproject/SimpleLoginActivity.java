package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

import static edu.fsu.cs.mobile.mobileprogrammingproject.User.phoneList;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.userList;

/**
 * Created by Aaron on 3/24/2018.
 */

public class SimpleLoginActivity extends AppCompatActivity {
    private EditText mPassword;
    private EditText mEmail;
    private Button loginButton;
    private DatabaseReference mDatabase;
    private String field;
    public boolean verified = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verified = false;
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maplaunch = new Intent(SimpleLoginActivity.this, MapsActivity.class);
                mEmail = (EditText) findViewById(R.id.email);
                mPassword =(EditText) findViewById(R.id.password);
                if(Verify(mEmail.getText().toString(),mPassword.getText().toString()))
                    startActivity(maplaunch);
                else
                    Toast.makeText(SimpleLoginActivity.this, "INVALID EMAIL OR PASSWORD", Toast.LENGTH_SHORT).show();
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SimpleLoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });


    }

    boolean Verify(String field, String password)
    {
            for (int i = 0; i < phoneList.size(); i++)
            {
                if(userList.get(phoneList.get(i)).email.equals(field) && userList.get(phoneList.get(i)).password.equals(password))
                    return true;
            }
            return false;

    }
}
