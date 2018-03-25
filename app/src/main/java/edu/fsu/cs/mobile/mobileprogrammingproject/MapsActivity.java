package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
        latlngsMap.put(new LatLng(-27.4698, 153.0251), "Ron");

        //for storing lat and long for google maps use
        latlngs.add(new LatLng(-27.4698, 153.0251)); //some latitude and logitude value

        schoolLocate = new LatLng(30.445349, -84.299542);


        //printing markers on map
        for (LatLng point : dbLatLngs) {
            options.position(point);
            options.title("KIM JON IL");
            options.snippet("ILLING IT OUT");
            googleMap.addMarker(options);
        }

        // Add a marker in Sydney and move the camera
        LatLng custom = new LatLng(20, 85);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(custom));

        mMap.setOnInfoWindowClickListener(this);


        mMap.addCircle(new CircleOptions()
                .center(sydney)
                .radius(3000000)
                .strokeWidth(0f)
                .fillColor(0x550000FF));
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();


    }

    static public boolean updateMarkers(DataSnapshot allData) {
        ArrayList<LatLng> newLatLngs = new ArrayList<>();
        for (DataSnapshot messageSnapshot: allData.getChildren()) {
            newLatLngs.add(new LatLng(Double.parseDouble(messageSnapshot.child("latitude").getValue().toString()), Double.parseDouble(messageSnapshot.child("longitude").getValue().toString())));
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
        return Math.sqrt((i.latitude-ii.latitude)*(i.latitude-ii.latitude) + (i.longitude-ii.longitude)*(i.longitude-i.longitude));

    }
}
