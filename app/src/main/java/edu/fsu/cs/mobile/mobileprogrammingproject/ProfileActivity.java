package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.BlogFeedFragment;

public class ProfileActivity extends AppCompatActivity implements ProfilePreviewFragment.OnFragmentInteractionListener,
        ProfileActivityFragment.MyProfileListener,
        ProfileDetailFragment.OnFragmentInteractionListener,
        MessagingDetailFragment.OnFragmentInteractionListener,
        BlogFeedFragment.OnFragmentInteractionListener{ // ADDED THIS BECAUSE OF TEMPLATE IN AUTOMADE FRAGMENT
    // IS IT AN ISSUE ALL THESE SHARING ONE METHOD IN THIS ACTIVITY?

    private final int REQUEST_LOCATION_PERMISSION = 7;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean mTrackingLocation;
    //private DatabaseReference mDatabase;
    private String usersEmail;

    @Override
    public void onBackPressed() { // can consolidate alot of these func calls to fm here

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        }
        else {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(fm.findFragmentByTag("outermostFrag"))
                    .commit();
            fm.popBackStack();
        }

    }


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

        getSupportFragmentManager().beginTransaction().add(R.id.outerFrag, ProfileActivityFragment.newInstance(), "outermostFrag").addToBackStack(null).commit();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_messaging: {
                FragmentManager fm = getSupportFragmentManager();
                // INSERT LOGIC TO START MESSAGING DETAIL FRAGMENT
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(fm.findFragmentByTag("outermostFrag"))
                        .commit();

                fm.beginTransaction()
                        .replace(R.id.outerFrag, MessagingDetailFragment.newInstance(usersEmail))
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            case R.id.action_Feed: {

                BlogFeedFragment Feed = new BlogFeedFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(fm.findFragmentByTag("outermostFrag"))
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.outerFrag, BlogFeedFragment.newInstance())
                        .addToBackStack(null).commit();
                return true;
            }

            case R.id.action_logout: {
                // INSERT LOGIC TO LOGUT
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                db.collection("users").document(usersEmail).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Logout", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Logout", "Error deleting document", e);
                            }
                        });
                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    /*@Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.av_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_menu);
        ActionMenuView amView =
                (ActionMenuView) MenuItemCompat.getActionView(menuItem);

        Menu menuObject = amView.getMenu();
        inflater.inflate(R.menu.sub_menu, menuObject);

        amView.setOnMenuItemClickListener( new OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item){
                TextView textView = (TextView) findViewById(R.id.action_nme);

                switch (item.getItemId()) {

                    case R.id.action_email:
                        textView.setText("Email clicked");
                        return true;
                    case R.id.action_forum:
                        textView.setText("Forum clicked");
                        return true;
                    case R.id.action_comment:
                        textView.setText("Comment clicked");
                        return true;
                    case R.id.action_setting:
                        textView.setText("Settings clicked");
                        return true;

                    default:
                        return true;
                }

            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView textView = (TextView) findViewById(R.id.action_nme);

        switch (item.getItemId()) {

            case R.id.action_settings:
                textView.setText("Settings clicked");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }*/



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
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fm.findFragmentByTag("outermostFrag"))
                .commit();

        fm.beginTransaction()
                .add(R.id.outerFrag, ProfileDetailFragment.newInstance(daEmail))
                .addToBackStack(null)
                .commit();
    }
}
