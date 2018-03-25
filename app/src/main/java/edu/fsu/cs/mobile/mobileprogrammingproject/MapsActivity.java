package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    static private GoogleMap mMap;
    static private MarkerOptions options = new MarkerOptions();
    Map<LatLng,String> latlngsMap = new HashMap<>();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private LatLng schoolLocate;
    static private ArrayList<LatLng> dbLatLngs = new ArrayList<>();
    static private ArrayList<String> dbPhone = new ArrayList<>();
    static private ArrayList<String> dbName = new ArrayList<>();
    static int iterate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), getIntent().getStringExtra("myPhoneNum"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng PreClickValue = new LatLng(0,0);

        //for storing lat and long into dictionary with database key
        //latlngsMap.put(new LatLng(-27.4698, 153.0251), "Ron");

        //for storing lat and long for google maps use
        //latlngs.add(new LatLng(-27.4698, 153.0251)); //some latitude and logitude value

        schoolLocate = new LatLng(30.445349, -84.299542);

        iterate = 0;
        //printing markers on map
        for (LatLng point : dbLatLngs) {
            options.position(point);
            options.title(dbName.get(iterate));
            options.snippet(dbPhone.get(iterate));
            googleMap.addMarker(options);
            iterate++;
        }

        // Add a marker in Sydney and move the camera
        //LatLng custom = new LatLng(20, 85);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        float zoomLevel = (float) 15.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocate, zoomLevel));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        mMap.setOnInfoWindowClickListener(this);


        mMap.addCircle(new CircleOptions()
                .center(schoolLocate)
                .radius(1000)
                .strokeWidth(0f)
                .fillColor(0x550000FF));
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    LatLng target = marker.getPosition();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Profile", "Ready").apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("UserProfile", marker.getSnippet()).apply();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    static public boolean updateMarkers(DataSnapshot allData) {
        //ArrayList<LatLng> newLatLngs = new ArrayList<>();
        for (DataSnapshot messageSnapshot: allData.getChildren()) {
            dbLatLngs.add(new LatLng(Double.parseDouble(messageSnapshot.child("latitude").getValue().toString()), Double.parseDouble(messageSnapshot.child("longitude").getValue().toString())));
            dbName.add(messageSnapshot.child("name").getValue().toString());
            dbPhone.add(messageSnapshot.child("phone").getValue().toString());
            //printing markers on map
            /*while(mMap == null);
            for (LatLng point : newLatLngs) {
                options.position(point);
                options.title("KIM JON IL");
                options.snippet("ILLING IT OUT");
                mMap.addMarker(options);*/
            //}
            //Toast.makeText(getApplicationContext(),  messageSnapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(,  "FINISHED POPULATING aRRAY WITH MARKER INFO", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
