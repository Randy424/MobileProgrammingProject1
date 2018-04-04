package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.phoneList;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.userList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener{
    private DatabaseReference mDatabase;
    public HashMap<String, String> currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MapsActivity.dbLatLngs.clear();

                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren() ){
                    MapsActivity.dbLatLngs.add(new LatLng(Double.parseDouble(messageSnapshot.child("latitude").getValue().toString()), Double.parseDouble(messageSnapshot.child("longitude").getValue().toString())));
                    MapsActivity.dbName.add(messageSnapshot.child("name").getValue().toString());
                    MapsActivity.dbPhone.add(messageSnapshot.child("phone").getValue().toString());
                    MapsActivity.dbMajor.add(messageSnapshot.child("major").getValue().toString());

                    userList.put((String) messageSnapshot.child("phone").getValue(), new User((String) messageSnapshot.child("name").getValue(),
                            (String) messageSnapshot.child("email").getValue(),(String) messageSnapshot.child("password").getValue(),(String) messageSnapshot.child("longitude").getValue(),
                            (String) messageSnapshot.child("latitude").getValue(), (String) messageSnapshot.child("major").getValue() ));

                    phoneList.add((String) messageSnapshot.child("phone").getValue());
                }
            }


            @Override
            public void onCancelled(DatabaseError firebaseError) { } // changed type here from original example

        });

        ProfileFragment profile = new ProfileFragment();
        Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);

        if(getIntent().getAction().equals("MAPS"))
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, profile).commit();
        else
        startActivity(i);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
