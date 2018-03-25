package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener{
    //static final String FIREBASE_TABLE = "";
    private DatabaseReference mDatabase;

    public HashMap<String, String> currentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("I am the best");
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if((PreferenceManager.getDefaultSharedPreferences(this).getString("Profile", "null")) == "null") // IF NOTHING STORED
        {
            String x = PreferenceManager.getDefaultSharedPreferences(this).getString("Profile", "null");

            Log.d("MAIN LOG", x);

            Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);

            startActivity(i);
        }
        else
        {


            ProfileFragment profile = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, profile).commit();


//            FragmentManager manager = getFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.add(R.id.fragment_frame, profile);
//            transaction.commit();


        }
       // Intent i = new Intent(MainActivity.this, SimpleLoginActivity.class);
      //  startActivity(i);


        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        //DatabaseReference myRef = database.getReference();
        //DatabaseReference childRef = myRef.child
        //Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
        //DatabaseReference myRef = database.getReference(FIREBASE_TABLE);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
