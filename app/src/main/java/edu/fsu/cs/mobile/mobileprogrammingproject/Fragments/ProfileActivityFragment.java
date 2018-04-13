package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private MyProfileListener mListener;
    private TextView mFriendsText;
    public interface MyProfileListener {
        void onProfPreviewClick(String derp); // need this, need diff type for url?
    }



    @Override // needed this? was getting crash on clicking download without it? (FROM OLD HW 4 NOTE)
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyProfileListener) {
            mListener = (MyProfileListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyProfileListener");
        }
    }


    MapView mapView;
    //GoogleMap gmap;
    private FirebaseFirestore db;
    private final String TAG = "DERP TAG";
    static private MarkerOptions options = new MarkerOptions();

    private void addIfValid(LatLng coord, String userEmail, GoogleMap ourMap) {
        options.position(coord);
        options.title(userEmail);
        ourMap.addMarker(options);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) { //  TODO IS MAKING THIS FINAL HERE A PROBLEM?
                                                        // IF SO WHAT IS THE WORKAROUND?

        db.collection("users")
                //.whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<LatLng> positionInfo = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                addIfValid(new LatLng(document.getDouble("latitude"),
                                                document.getDouble("longitude")),
                                        document.getId(),
                                        googleMap);
                                //options.position(new LatLng(document.getDouble("latitude"),document.getDouble("longitude")));
                                //options.title(document.getId());
                                //googleMap.addMarker(options);
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

    public static ProfileActivityFragment newInstance() {
        ProfileActivityFragment fragment = new ProfileActivityFragment();
        //fragment.setRetainInstance(true);

        return fragment;
    }

    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        CardView profPreview = (CardView) myView.findViewById(R.id.profile_preview_card);
        profPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onProfPreviewClick(ProfilePreviewFragment.thisUsersEmail);
            }
        });

        mFriendsText = myView.findViewById(R.id.friendsText);
        getFriendCount();
        mapView =  myView.findViewById(R.id.mapLite);
        mapView.onCreate(null);
        mapView.getMapAsync(this);


        //Starts profile fragment with email as an argument that gets set to @thisUsersEmail
        getFragmentManager().beginTransaction().add(R.id.profile_preview_card, ProfilePreviewFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getEmail())).commit();
        return myView;
    }

    public void getFriendCount()
    {
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            updateFriends(count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }); }

    public void updateFriends(int total)
    {
        mFriendsText.append(" " + Integer.toString(total));}


    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO DO SOMETHING WHEN YOU FIRE THIS

        Toast.makeText(getContext(), "Info window clicked",
                Toast.LENGTH_SHORT).show();
        LatLng target = marker.getPosition();

        getFragmentManager().beginTransaction().replace(R.id.profile_preview_card, ProfilePreviewFragment.newInstance(marker.getTitle())).commit();
        return true;
    }
}
