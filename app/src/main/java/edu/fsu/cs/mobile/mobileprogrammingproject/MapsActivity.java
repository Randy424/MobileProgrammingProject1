package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Intent;
import android.preference.PreferenceManager;
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
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    static private MarkerOptions options = new MarkerOptions();
    static public ArrayList<LatLng> dbLatLngs = new ArrayList<>();
    static public ArrayList<String> dbPhone = new ArrayList<>();
    static public ArrayList<String> dbName = new ArrayList<>();
    static public ArrayList<String> dbMajor = new ArrayList<>();
    static int iterate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        LatLng schoolLocate = new LatLng(30.445349, -84.299542);

        iterate = 0;
        //printing markers on map
        iterate = 0;
        //printing markers on map
        for (LatLng point : dbLatLngs) {
            if((findDistance(schoolLocate, point) < 1000)) {
                options.position(point);
                options.title(dbName.get(iterate)+" - "+dbMajor.get(iterate));
                options.snippet(dbPhone.get(iterate));
                googleMap.addMarker(options);
            }
            iterate++;
        }

        float zoomLevel = (float) 15.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocate, zoomLevel));


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
        i.setAction("MAPS");
        startActivity(i);

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
