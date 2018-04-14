package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

import static com.google.firebase.auth.FirebaseAuth.getInstance;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db2;
    private String currentUser;
    private String clickedUser;
    private TextView mEnterMajor;
    private EditText mMajorEdit;
    private Button mSubmit;
    private TextView mCurrentMajor;
    private TextView mCurrentName;


    private OnFragmentInteractionListener mListener;
    static private Button mFriendButton;

    public ProfileDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     *
     * @return A new instance of fragment ProfileDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDetailFragment newInstance(String daEmail) {
        ProfileDetailFragment fragment = new ProfileDetailFragment();
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
        db2 = FirebaseFirestore.getInstance();
        clickedUser = getArguments().getString("email");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        //Assign views to variables
        TextView emailTextView = (TextView) myView.findViewById(R.id.nameTextview);
        mCurrentMajor = myView.findViewById(R.id.currentMajorTextview);
        mCurrentName = myView.findViewById(R.id.nameTextview);
        mCurrentName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        mFriendButton = myView.findViewById(R.id.friendButton);
        mMajorEdit = myView.findViewById(R.id.major_Edittext);
        mEnterMajor = myView.findViewById(R.id.enterMajorTextview);
        mSubmit = myView.findViewById(R.id.submitButton);

        if (clickedUser != currentUser) {
            mEnterMajor.setVisibility(myView.INVISIBLE);
            mEnterMajor.setClickable(false);
            mMajorEdit.setVisibility(myView.INVISIBLE);
            mMajorEdit.setClickable(false);
            mSubmit.setVisibility(myView.INVISIBLE);
            mSubmit.setClickable(false);
        } else {
            mSubmit.setOnClickListener(this);
        }

        DocumentReference docRef = db2.collection("users").document(currentUser).collection("friends")
                .document(clickedUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        changeButton(mFriendButton);
                    } else {
                        Log.d("logger", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRef2 = db2.collection("users").document(clickedUser).collection("major")
                .document("major");
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        changeMajor(document.getData());
                    } else {
                        Log.d("logger", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });


        mFriendButton.setOnClickListener(this);
        emailTextView.setText(clickedUser);

        return myView;
    }

    public void changeButton(Button FriendButton) {
        FriendButton.setText(R.string.friendAdded);

    }

    public void changeMajor(Map<String, Object> majormap) {
        mCurrentMajor.setText(majormap.get("major").toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.friendButton:
                Map<String, Object> update = new HashMap<>();
                update.put(clickedUser, true);
                mFriendButton.setText(R.string.friendAdded);
                db2.collection("users").document(currentUser)
                        .collection("friends").document(clickedUser)
                        .set(update, SetOptions.merge());
                TextView tv = (TextView) getView().getRootView().findViewById(R.id.friendsText);
                ProfileActivityFragment.totFriendCount = ProfileActivityFragment.totFriendCount + 1;
                tv.setText("Number of friends: " + Integer.toString(ProfileActivityFragment.totFriendCount));
                break;
            case R.id.submitButton:
                Map<String, Object> update2 = new HashMap<>();
                update2.put("major", mMajorEdit.getText().toString());
                mCurrentMajor.setText(mMajorEdit.getText().toString());
                db2.collection("users").document(currentUser)
                        .collection("major").document("major").set(update2, SetOptions.merge());


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) { // TODO DO WE NEED THIS OR CLUTTER?
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
