package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.BlogFeedFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.BlogPostFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.ConversationFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.CreateMeetingFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.MessagingDetailFragment;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.OptionsFragment;
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
        OptionsFragment.OnFragmentInteractionListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        CreateMeetingFragment.OnFragmentInteractionListener,
        TimePickerFragment.OnFragmentInteractionListener,
         GoogleMap.OnMapLongClickListener{

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;
    private String usersEmail;
    private final String TAG = "ProfileActivity";
    static private final MarkerOptions options = new MarkerOptions();
    private String filterOption;
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private TextView mFriendsText;
    private String major;


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

        filterOption = PreferenceManager.getDefaultSharedPreferences(this).getString("MapFilter", "ALL");
        final LatLng schoolLocate = new LatLng(30.445349, -84.299542);



        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                //restricting entries by proximity

                                if (findDistance(new LatLng(document.getDouble("latitude"),
                                        document.getDouble("longitude")), schoolLocate) <= 1000) {
                                    if(filterOption.equals("ALL")) {

                                        addIfValid(new LatLng(document.getDouble("latitude"),

                                                        document.getDouble("longitude")),

                                                document.getId(),

                                                googleMap);

                                    }
                                    else
                                    {

                                        DocumentReference docRef2 = db.collection("users").document(email);
                                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document != null && document.exists()) {
                                                        major = document.getData().get("major").toString();

                                                        Log.d("Major", document.getString("major"));
                                                        Log.d("User Major", major);
                                                        if (major.equals(document.getString("major"))) {

                                                            addIfValid(new LatLng(document.getDouble("latitude"),

                                                                            document.getDouble("longitude")),

                                                                    document.getId(),

                                                                    googleMap);

                                                        }
                                                    } else {
                                                        Log.d("logger", "No such document");
                                                    }
                                                } else {
                                                    Log.d("fail", "get failed with ", task.getException());
                                                }
                                            }



                                        });


                                    }

                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



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
            case R.id.action_meeting: {

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.outsideFrag, CreateMeetingFragment.newInstance(usersEmail), CreateMeetingFragment.class.getCanonicalName())
                        .commit();
                return true;
            }
            case R.id.action_messaging: { // add check to see if im currently viwing this fragment

                FragmentManager fm = getSupportFragmentManager();
                //Fragment previousMessageFragment = fm.findFragmentByTag(MessagingDetailFragment.class.getCanonicalName());
                //if(previousMessageFragment == null) {
                    fm.beginTransaction()
                            .add(R.id.outsideFrag, MessagingDetailFragment.newInstance(usersEmail), MessagingDetailFragment.class.getCanonicalName())
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
                        .add(R.id.outsideFrag, BlogPostFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            case R.id.action_Options: {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.outsideFrag, OptionsFragment.newInstance())
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

    public void showStartTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
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
    @Override
    public void updateDisplayedStartTime (TimePicker view, int hourOfDay, int minute) {
        TextView timeDisplayer = this.findViewById(R.id.selectedTimeTV);
        timeDisplayer.setText(Integer.toString(hourOfDay) + ":H, " + Integer.toString(minute) + ":M");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }
    public double findDistance(LatLng i, LatLng ii){

        double lat1, lat2, lon1, lon2, el1, el2;


        lat1 = i.latitude;
        lat2 = ii.latitude;
        lon1 = i.longitude;
        lon2 = ii.longitude;

        el1 = 1;
        el2 = 1;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
