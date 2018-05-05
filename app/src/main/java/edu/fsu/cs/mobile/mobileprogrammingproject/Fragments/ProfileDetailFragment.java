package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetailFragment extends Fragment implements View.OnClickListener {


    private FirebaseFirestore db2;
    private String currentUser;
    private String clickedUser;
    private Button mSendMessage;
    public TextView mCurrentMajor;
    public TextView mClasses;
    public TextView mYear;
    public TextView mName;



    private Button mFriendButton;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db2 = FirebaseFirestore.getInstance();
        assert getArguments() != null;
        clickedUser = getArguments().getString("email");
        currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())
                .getEmail();
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        //Assign views to variables
        mClasses = myView.findViewById(R.id.classesText);
        mCurrentMajor = myView.findViewById(R.id.majortextView);
        mYear = myView.findViewById(R.id.yearText);
        mFriendButton = myView.findViewById(R.id.friendButton);
        mName = myView.findViewById(R.id.nameText);
        mSendMessage = myView.findViewById(R.id.messageButton);
        mSendMessage.setOnClickListener(this);


        /**
         * The following DocumentReference and listener is used to populate the textviews with
         * the associated fields in the database via the PopulateTextview function.
         */
        DocumentReference fieldDoc = db2.collection("users").document(clickedUser);
        fieldDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {

                        PopulateTextview(mCurrentMajor,document,"major");
                        PopulateTextview(mClasses,document,"classes");
                        PopulateTextview(mYear,document,"year");
                        PopulateTextview(mName, document, "name");

                    } else {
                        Log.d("logger", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });

        /**
         * The following DocumentReference and listener is used to determine how to display the
         * addFriend button based on if the friend is already added or not.
         */
        DocumentReference docRef = db2.collection("users").document(clickedUser).collection("friends")
                .document(clickedUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        //changeButton(mFriendButton);
                    } else {
                        Log.d("logger", "No such document");
                    }
                } else {
                    Log.d("fail", "get failed with ", task.getException());
                }
            }
        });

        mFriendButton.setOnClickListener(this);

        return myView;
    }

    private void changeButton(Button FriendButton) {
        FriendButton.setText(R.string.friendAdded);

    }

    /**
     * Assigns field to textview
     * @param textView textview to be populated with field
     * @param doc documentsnapshot
     * @param field key that grabs field from map (where field is stored)
     */
    public void PopulateTextview(TextView textView, DocumentSnapshot doc, String field)
    {
        if(doc.getData().get(field) != null)
        textView.setText(doc.getData().get(field).toString());

        else
        textView.setText(field + " has not been added.");
    }




    @SuppressLint("SetTextI18n")
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
                //TextView tv = Objects.requireNonNull(getView())
                //        .getRootView().findViewById(R.id.friendsText);
                ProfileActivityFragment.totFriendCount = ProfileActivityFragment.totFriendCount + 1;
                //tv.setText("Number of friends: " + Integer
                //        .toString(ProfileActivityFragment.totFriendCount));
                break;
            case R.id.messageButton:
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.outsideFrag, ConversationFragment.newInstance(currentUser, clickedUser), MessagingDetailFragment.class.getCanonicalName())
                        .addToBackStack(null)
                        .commit();
        }
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
    }
}
