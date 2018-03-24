package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    static final String FIREBASE_TABLE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
        //DatabaseReference myRef = database.getReference(FIREBASE_TABLE);
        //Intent i = new Intent(MainActivity.this, MapsActivity.class);
        //startActivity(i);
    }
}
