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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private MyProfileListener mListener;
    private TextView mFriendsText;
    private String major;

    static int totFriendCount;



    @Override
    public void onMapLongClick(LatLng latLng) {



    }

    public interface MyProfileListener {
        void onProfPreviewClick(String derp);
    }

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyProfileListener) {
            mListener = (MyProfileListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyProfileListener");
        }
    }


    private FirebaseFirestore db;
    private final String TAG = "DERP TAG";
    static private final MarkerOptions options = new MarkerOptions();

    private void addIfValid(LatLng coord, String userEmail, GoogleMap ourMap) {
        options.position(coord);
        options.title(userEmail);
        ourMap.addMarker(options);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final LatLng schoolLocate = new LatLng(30.445349, -84.299542);

        DocumentReference docRef2 = db.collection("users").document(email).collection("major")
                .document("major");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                       major = document.getData().get("major").toString();
                    } else {
                        Log.d("logger", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });


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
                                    if(major.equals("ALL")) {

                                    }
                                    else
                                    {
                                        if (major == document.getString("major")) {

                                        }
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

    public static ProfileActivityFragment newInstance() {
        return new ProfileActivityFragment();
    }

    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);
        myView.setBackgroundColor(Color.DKGRAY);
        myView.setClickable(true);

        /*CardView profPreview = myView.findViewById(R.id.profile_preview_card);
        profPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onProfPreviewClick(ProfilePreviewFragment.thisUsersEmail);
            }
        });

        mFriendsText = myView.findViewById(R.id.friendsText);
        getFriendCount();*/


/*
        //Starts profile fragment with email as an argument that gets set to @thisUsersEmail
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().add(R.id.profile_preview_card,
                ProfilePreviewFragment.newInstance(Objects.requireNonNull(FirebaseAuth
                        .getInstance().getCurrentUser()).getEmail())).commit();*/
        return myView;
    }

    /*private void getFriendCount() {
        db.collection("users").document(Objects.requireNonNull(Objects
                .requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()))
                .collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot ignored : task.getResult()) {
                                count++;
                            }
                            updateFriends(count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }*/

    /*private void updateFriends(int total) {
        totFriendCount = total;
        mFriendsText.append(" " + Integer.toString(totFriendCount));
    }

*/
    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO DO SOMETHING WHEN YOU FIRE THIS

        Toast.makeText(getContext(), "Info window clicked",
                Toast.LENGTH_SHORT).show();

        /*assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.profile_preview_card,
                ProfilePreviewFragment.newInstance(marker.getTitle())).commit();*/
        return true;
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
