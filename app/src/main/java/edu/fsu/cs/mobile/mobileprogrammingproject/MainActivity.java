package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static edu.fsu.cs.mobile.mobileprogrammingproject.User.phoneList;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.userList;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //static final String FIREBASE_TABLE = "";
    private DatabaseReference mDatabase;

    private Button mFirebaseBtn;
    public HashMap<String, String> currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mFirebaseBtn = (Button) findViewById(R.id.firebase_btn);

        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("I am the best");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "Im working", Toast.LENGTH_LONG).show();
                //1 - Create child in root object
                //2 Assign some value to the child object

                mDatabase.child("Name").setValue("Dustin");


            }
        });

        if(PreferenceManager.getDefaultSharedPreferences(this).getString("Profile", "null") == "null") // IF NOTHING STORED
        {
            //Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);
            //startActivity(i);
            Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);
            startActivity(i);
        }
        else
        {

            Bundle bundle = getIntent().getBundleExtra("xy");   //<< get Bundle from Intent
            int value = bundle.getInt("myData");//<extract values from Bundle using key
        }


        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        //DatabaseReference myRef = database.getReference();
        //DatabaseReference childRef = myRef.child
        //Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
        //DatabaseReference myRef = database.getReference(FIREBASE_TABLE);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MapsActivity.dbLatLngs.clear();
                //MapsActivity.updateMarkers(dataSnapshot);
                //String name = (String) messageSnapshot.child("name").getValue();
                //String message = (String) messageSnapshot.child("message").getValue();
                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren() ){
                    MapsActivity.dbLatLngs.add(new LatLng(Double.parseDouble(messageSnapshot.child("latitude").getValue().toString()), Double.parseDouble(messageSnapshot.child("longitude").getValue().toString())));


                userList.put((String) messageSnapshot.child("phone").getValue(), new User((String) messageSnapshot.child("name").getValue(),
                        (String) messageSnapshot.child("email").getValue(),(String) messageSnapshot.child("password").getValue(),(String) messageSnapshot.child("longitude").getValue(),
                        (String) messageSnapshot.child("latitude").getValue(), (String) messageSnapshot.child("major").getValue() ));

                    phoneList.add((String) messageSnapshot.child("phone").getValue());
                }
            }


        @Override
        public void onCancelled(DatabaseError firebaseError) { } // changed type here from original example

    });

        Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);
        startActivity(i);


    }
}
