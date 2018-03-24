package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    //static final String FIREBASE_TABLE = "";
    private DatabaseReference mDatabase;

    private Button mFirebaseBtn;

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


        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        //DatabaseReference myRef = database.getReference();
        //DatabaseReference childRef = myRef.child
        //Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
        //DatabaseReference myRef = database.getReference(FIREBASE_TABLE);
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }
}
