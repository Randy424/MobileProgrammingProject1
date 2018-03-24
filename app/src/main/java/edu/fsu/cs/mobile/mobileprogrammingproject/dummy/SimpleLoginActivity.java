package edu.fsu.cs.mobile.mobileprogrammingproject.dummy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    public boolean verified;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = (EditText) findViewById(R.id.email);
                mPassword =(EditText) findViewById(R.id.password);
                if(Verify(mEmail.getText().toString(), "email"))
                    Toast.makeText(getApplicationContext(), "It's a match!", Toast.LENGTH_SHORT).show();
                Verify(mPassword.getText().toString(), "password");
            }
        });


    }

    boolean Verify(String field, final String key)
    {
        final String entry = field; //converting to final allows for inner class usage.
        verified = false;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    if(entry.equals((String) messageSnapshot.child(key).getValue()))
                        verified = true;
                    //String name = (String) messageSnapshot.child("name").getValue();
                    //String message = (String) messageSnapshot.child("message").getValue();
                }

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } // changed type here from original example

        });
        return verified;

    }
}
