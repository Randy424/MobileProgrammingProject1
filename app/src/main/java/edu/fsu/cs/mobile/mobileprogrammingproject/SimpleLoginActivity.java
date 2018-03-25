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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(this, mDatabase.toString(), Toast.LENGTH_SHORT).show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = (EditText) findViewById(R.id.email);
                mPassword =(EditText) findViewById(R.id.password);
                Verify(mEmail.getText().toString(),mPassword.getText().toString());
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

    void Verify(String field, String password)
    {
        final String pass = password;
        final String entry = field; //converting to final allows for inner class usage.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    if(entry.equals((String) messageSnapshot.child("email").getValue()) && pass.equals((String) messageSnapshot.child("password").getValue()))
                    {
                        Intent i = new Intent(SimpleLoginActivity.this, MapsActivity.class);
                        Toast.makeText(getApplicationContext(),  "IN THE ON DATA CHANGE (ADD VALUE EVENT LISTENER)", Toast.LENGTH_SHORT).show();
                        MapsActivity.updateMarkers(dataSnapshot);
                        startActivity(i);
                    }
                    //String name = (String) messageSnapshot.child("name").getValue();
                    //String message = (String) messageSnapshot.child("message").getValue();
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } // changed type here from original example

        });
    }
}
