package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

import static edu.fsu.cs.mobile.mobileprogrammingproject.User.phoneList;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.userList;

/**
 * Created by Aaron on 3/24/2018.
 */

public class SimpleLoginActivity extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;
    public LocationCallback mLocationCallback;
    boolean mTrackingLocation;
    private final int REQUEST_LOCATION_PERMISSION = 7;

    private EditText mPassword;
    private EditText mEmail;
    private Button loginButton;
    private DatabaseReference mDatabase;
    private String field;
    private String thisUsersNumber;
    public boolean verified = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("users");
        verified = false;
        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(mTrackingLocation == true) {
                    Location myLocation = locationResult.getLastLocation();
                    if (myLocation != null) {
                        Toast.makeText(getApplicationContext(), "Updating Location Info In Firebase", Toast.LENGTH_SHORT).show();
                        String theNumber = thisUsersNumber;

                        User user = new User(User.userList.get(theNumber).mEmail,
                                User.userList.get(theNumber).mName,
                                User.userList.get(theNumber).mPassword,
                                User.userList.get(theNumber).mMajor,
                                theNumber,
                                Double.toString(myLocation.getLatitude()),
                                Double.toString(myLocation.getLongitude()));

                        Map<String, Object > postValues = user.toMap();


                        mDatabase.child(theNumber).setValue(postValues);
                        showUpdatedLocation(myLocation);

                    }


                }
            }
        };

    }

    private void showUpdatedLocation(Location testLoc) {
        Toast.makeText(getApplicationContext(), "Updated location Latitude/Longitude: " + Double.toString(testLoc.getLatitude()) + '/' + Double.toString(testLoc.getLongitude()), Toast.LENGTH_SHORT).show();
    }
    public void getLocation(){


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

            Toast.makeText(getApplicationContext(), "IN THE IF CHECK", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);
            Intent i = new Intent(SimpleLoginActivity.this ,MapsActivity.class);
            startActivity(i);


        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
    /**
     * Method that stops tracking the device. It removes the location
     * updates, stops the animation and reset the UI.
     */
    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
            Toast.makeText(getApplicationContext(), "STOPPING TRACKING", Toast.LENGTH_SHORT).show();
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        }
    }
    boolean Verify(String field, String password)
    {
            for (int i = 0; i < phoneList.size(); i++)
            {
                if(userList.get(phoneList.get(i)).mEmail.equals(field) && userList.get(phoneList.get(i)).mPassword.equals(password)) {
                    if (!mTrackingLocation) {
                        getLocation();
                    } else {
                        stopTrackingLocation();
                    }
                    thisUsersNumber = phoneList.get(i);
                    return true;

                }

            }
            return false;

    }
}
