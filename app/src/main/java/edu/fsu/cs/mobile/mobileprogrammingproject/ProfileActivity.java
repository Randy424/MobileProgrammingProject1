package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.BlogFeedFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.BlogPostFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.ConversationFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.MessagingDetailFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.ProfileActivityFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.ProfileDetailFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.ProfilePreviewFragment;

public class ProfileActivity extends AppCompatActivity implements
        ProfilePreviewFragment.OnFragmentInteractionListener,
        ProfileActivityFragment.MyProfileListener,
        ProfileDetailFragment.OnFragmentInteractionListener,
        MessagingDetailFragment.OnFragmentInteractionListener,
        ConversationFragment.OnFragmentInteractionListener,
        BlogFeedFragment.OnFragmentInteractionListener,
        BlogPostFragment.OnFragmentInteractionListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;
    private String usersEmail;
    private final String TAG = "ProfileActivity";
    static private final MarkerOptions options = new MarkerOptions();


    private FirebaseFirestore db;

    /*@Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (mTrackingLocation) {
                Location myLocation = locationResult.getLastLocation();
                if (myLocation != null) {
                    Toast.makeText(getApplicationContext(),
                            "Updating Location Info In FireSTORE", Toast.LENGTH_SHORT).show();

                    // MIGHT BE GOOD TO HAVE SUCCESS/FAIL LISTENERS HERE!
                    Map<String, Object> locMap = new HashMap<>();
                    locMap.put("latitude", myLocation.getLatitude());
                    locMap.put("longitude", myLocation.getLongitude());

                    String FIREBASE_TABLE = "users";
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
        return i;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                addIfValid(new LatLng(document.getDouble("latitude"),
                                                document.getDouble("longitude")),
                                        document.getId(),
                                        googleMap);
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        LatLng schoolLocate = new LatLng(30.445349, -84.299542);
        float zoomLevel = (float) 14.0;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocate, zoomLevel));


        googleMap.setOnMarkerClickListener(this);


        googleMap.addCircle(new CircleOptions()
                .center(schoolLocate)
                .radius(1000)
                .strokeWidth(0f)
                .fillColor(0x550000FF));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        assert (i != null);

        usersEmail = i.getStringExtra("userEmail");
        assert (usersEmail != null);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        startTracking();

        getSupportFragmentManager().beginTransaction().replace(R.id.outerFrag, ProfileActivityFragment.newInstance(), "outermostFrag").commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.recent_feed_card, BlogFeedFragment.newInstance(), "outermostFrag2").commit();
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_messaging: { // add check to see if im currently viwing this fragment
                FragmentManager fm = getSupportFragmentManager();

                //Fragment previousMessageFragment = fm.findFragmentByTag(MessagingDetailFragment.class.getCanonicalName());
                //if(previousMessageFragment == null) {
                    fm.beginTransaction()
                            .replace(R.id.outsideFrag, MessagingDetailFragment.newInstance(usersEmail), MessagingDetailFragment.class.getCanonicalName())
                            .addToBackStack(null)
                            .commit();
                /*}
                else {
                    fm.beginTransaction()
                            .replace(R.id.outsideFrag, previousMessageFragment, MessagingDetailFragment.class.getCanonicalName())
                            .commit();
                }*/
                /*if (previousMessageFragment == null) {
                    previousMessageFragment = MessagingDetailFragment.newInstance(usersEmail);

                }*/

                /*fm.beginTransaction()
                        .replace(R.id.outerFrag, previousMessageFragment, MessagingDetailFragment.class.getCanonicalName())
                        .addToBackStack(null)
                        .commit();*/


                return true;
            }
            case R.id.action_Post: {


                getSupportFragmentManager().beginTransaction()
                        .add(R.id.outerFrag, BlogPostFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            }

            case R.id.action_logout: {
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
            /*case R.id.action_Feed: {
                BlogFeedFragment Feed = new BlogFeedFragment();

                FragmentManager fm = getSupportFragmentManager();

                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .hide(fm.findFragmentByTag("outermostFrag"))
                        .commit();

                fm.beginTransaction()
                        .add(R.id.outerFrag, Feed)
                        .addToBackStack(null)
                        .commit();
                return true;
            }*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void startTracking() {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int REQUEST_LOCATION_PERMISSION = 7;
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            Toast.makeText(getApplicationContext(), "IN THE PERMISSION CHECK",
                    Toast.LENGTH_SHORT).show();
        } else {
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

    @Override
    public void loadConversationFragment(String firstUserEmail, String secondUserEmail) {
        FragmentManager fm = getSupportFragmentManager();

        //Fragment previousConvoFragment = fm.findFragmentByTag(ConversationFragment.class.getCanonicalName());
        //if(previousConvoFragment == null) {
            fm.beginTransaction()
                    .replace(R.id.outsideFrag, ConversationFragment.newInstance(firstUserEmail, secondUserEmail))
                    .addToBackStack(null)
                    .commit();
        //}
        //else {
        //    fm.beginTransaction()
        //            .replace(R.id.outsideFrag, previousConvoFragment, ConversationFragment.class.getCanonicalName())
        //            .commit();
        //}



    }

    @Override
    public void onProfPreviewClick(String daEmail) {
        FragmentManager fm = getSupportFragmentManager();
        /*fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fm.findFragmentByTag("outermostFrag"))
                .commit();*/

        fm.beginTransaction()
                .add(R.id.outerFrag, ProfileDetailFragment.newInstance(daEmail))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO DO SOMETHING WHEN YOU FIRE THIS

        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();

        /*assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.profile_preview_card,
                ProfilePreviewFragment.newInstance(marker.getTitle())).commit();*/
        return true;
    }

    private void addIfValid(LatLng coord, String userEmail, GoogleMap ourMap) {
        options.position(coord);
        options.title(userEmail);
        ourMap.addMarker(options);
    }
}
