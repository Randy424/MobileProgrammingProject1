package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public LocationCallback mLocationCallback;
    EditText email, name, password, major, phone;
    private DatabaseReference mDatabase;
    LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    //public double testLat;
    private final int REQUEST_LOCATION_PERMISSION = 7;
    double currLat, currLong;
    boolean mTrackingLocation;
    boolean regComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regComplete = false;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        major = (EditText) findViewById(R.id.major);
        phone = (EditText) findViewById(R.id.phone);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(mTrackingLocation == true) {
                    Location myLocation = locationResult.getLastLocation();
                    //Toast.makeText(getApplicationContext(), "IN THE onLocationResult", Toast.LENGTH_SHORT).show();
                    if (myLocation != null) {
                        showUpdatedLocation(myLocation);
                        if(regComplete == false) {
                            //Toast.makeText(getApplicationContext(), "Inserting user to database now", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Updating Your Location In Firebase", Toast.LENGTH_SHORT).show();

                            MyUser user = new MyUser(email.getText().toString().trim(),
                                    name.getText().toString().trim(),
                                    password.getText().toString().trim(),
                                    major.getText().toString().trim(),
                                    phone.getText().toString().trim(),
                                    Double.toString(myLocation.getLatitude()),
                                    Double.toString(myLocation.getLongitude()));

                            Map<String, Object > postValues = user.toMap();


                            mDatabase.child(phone.getText().toString().trim()).setValue(postValues);
                        }
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
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "IN THE IF CHECK", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
        (getLocationRequest(), mLocationCallback,
                null /* Looper */);
            Intent i = new Intent(RegisterActivity.this ,MapsActivity.class);

            startActivity(i);


        }
    }


    public void registerClick(View view) {

            if (!mTrackingLocation) {
                Toast.makeText(getApplicationContext(), "INSERTING YOUR INFORMATION INTO OUR SYSTEM", Toast.LENGTH_SHORT).show();
                getLocation();
            } else {
                stopTrackingLocation();
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

    private boolean isEmpty(EditText et) {
        if (et.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

}
