package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagingDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessagingDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagingDetailFragment extends Fragment {
    private final String TAG = "messagedetailtag";
    private FirebaseFirestore db;
    private String myUsersEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessagingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment ProfileDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagingDetailFragment newInstance(String daEmail) {
        MessagingDetailFragment fragment = new MessagingDetailFragment();
        Bundle b = new Bundle();
        b.putString("email", daEmail);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        myUsersEmail = getArguments().getString("email");
        View myView = inflater.inflate(R.layout.fragment_messaging_detail, container, false);
        TextView yourEmailText = (TextView) myView.findViewById(R.id.yourOwnEmail);
        final String testReceiver = "adstew96@gmail.com";
        final String testMessage = "First Message WOOHOO!";
        yourEmailText.setText("I see you are: " + getArguments().getString("email"));
        Button sendButt = (Button) myView.findViewById(R.id.sendMessageButton);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("messages")
                        .document(myUsersEmail + "_" + testReceiver)
                        .set(new Message(myUsersEmail, testReceiver, testMessage), SetOptions.merge());
            }
        });



        /*db.collection("messages")
                //.whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            /Set<String> conversations = new Set<>();
                            ArrayList<LatLng> positionInfo = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Message mMessage = document.toObject(Message.class);
                                if(mMessage.getReceiver().equals(myUsersEmail)) {
                                    conversations.add(mMessage.getSender());
                                }
                                else if (mMessage.getSender().equals(myUsersEmail)){
                                    conversations.add(mMessage.getReceiver());
                                }

                                // do stuff in here to show message stuffTextView
                                //options.position(new LatLng(document.getDouble("latitude"),document.getDouble("longitude")));
                                //options.title(document.getId());
                                //googleMap.addMarker(options);
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
