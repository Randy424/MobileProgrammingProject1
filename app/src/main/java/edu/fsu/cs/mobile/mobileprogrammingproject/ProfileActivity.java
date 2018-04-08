package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements ProfilePreviewFragment.OnFragmentInteractionListener, ProfileActivityFragment.MyProfileListener, ProfileDetailFragment.OnFragmentInteractionListener{
    private final int REQUEST_LOCATION_PERMISSION = 7;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean mTrackingLocation;
    //private DatabaseReference mDatabase;
    private String usersEmail;



    private final String FIREBASE_TABLE = "users";

    private FirebaseFirestore db;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(mTrackingLocation) {
                Location myLocation = locationResult.getLastLocation();
                if (myLocation != null) {
                    Toast.makeText(getApplicationContext(), "Updating Location Info In FireSTORE", Toast.LENGTH_SHORT).show();


                    // TODO MIGHT WANT TO ADD ON SUCCESS AND ON FAIL LISTENERS HERE< CODE IS IN THE DOCUMENTATION
                    Map<String, Object> locMap = new HashMap<>();
                    locMap.put("latitude", myLocation.getLatitude());
                    locMap.put("longitude", myLocation.getLongitude());


                    db.collection(FIREBASE_TABLE)
                            .document(usersEmail)
                            .set(locMap, SetOptions.merge());

                }

            }
        }
    };




    public static Intent newInstance(Context context, FirebaseUser user) {

        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra("userEmail", user.getEmail()); // Could pass in entire user instead here
        String myNum = user.getPhoneNumber();
        // TODO MAKE Myser parseable so i can pass it in an extra

        //if myNum != null
                //i.putExtr
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        assert(i != null);

        usersEmail = i.getStringExtra("userEmail");
        assert(usersEmail != null);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        startTracking();

        getSupportFragmentManager().beginTransaction().add(R.id.outerFrag, ProfileActivityFragment.newInstance()).commit();


    }

    public void startTracking(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);

            Toast.makeText(getApplicationContext(), "IN THE PERMISSION IF CHECK", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);

        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override// TODO CLUTTER FOR NOW, HOOKED UP WITH PROFILEPREVIEWFRAGMENT
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProfPreviewClick(String daEmail) {
        getSupportFragmentManager().beginTransaction().replace(R.id.outerFrag, ProfileDetailFragment.newInstance(daEmail)).commit();
    }
}
