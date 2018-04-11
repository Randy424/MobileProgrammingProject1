package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;


public class BlogPostFragment extends Fragment {

    private ImageButton imageBtn;
    private FirebaseFirestore db;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;
    //private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BlogPostFragment() {
        // Required empty public constructor
    }


    public static BlogPostFragment newInstance(String param1, String param2) {
        BlogPostFragment fragment = new BlogPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        postBtn = (Button) getView().findViewById(R.id.submit);
        textTitle = (EditText) getView().findViewById(R.id.title);
        textDesc = (EditText) getView().findViewById(R.id.desc);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_post, container, false);
        // Inflate the layout for this fragment

        View myButton = rootView.findViewById(R.id.submit);
        View imageButton = rootView.findViewById(R.id.imageButton);
        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myClickMethod(v);
            }

        });

        return rootView;
    }

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

    public void myClickMethod(View v) {

        switch(v.getId()) {
            case R.id.imageButton: {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
            break;

            case R.id.submit:{
                Toast.makeText(getActivity(), "POSTING...", Toast.LENGTH_LONG).show();
                final String PostTitle = textTitle.getText().toString().trim();
                final String PostDesc = textDesc.getText().toString().trim();
               String usersEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                Map<String, Object> messageIdMap = new HashMap<>();
                messageIdMap.put("title", PostTitle);
                messageIdMap.put("description", PostDesc);

                db.collection("Blog_Post")
                        .document(usersEmail)
                        .set(PostTitle, SetOptions.merge());
            }
            break;

            // Just like you were doing
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
