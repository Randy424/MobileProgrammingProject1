package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Posts;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;


public class BlogPostFragment extends Fragment{

    private ImageButton imageBtn;
    private FirebaseFirestore db;
//    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri uri = null;
    private EditText textTitle;
    private EditText textDesc;
    private Button postBtn;



    private OnFragmentInteractionListener mListener;

    public BlogPostFragment() {
        // Required empty public constructor
    }


    public static BlogPostFragment newInstance() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_post, container, false);
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        Button myButton = (Button) rootView.findViewById(R.id.submit);
//        View imageButton = rootView.findViewById(R.id.imageButton);
        textTitle = (EditText) rootView.findViewById(R.id.title);
        textDesc = (EditText) rootView.findViewById(R.id.desc);
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setClickable(true);



        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch(v.getId()) {

                    case R.id.submit:{
                        //Do error checking too

               // Toast.makeText(getActivity(), "POSTING...", Toast.LENGTH_LONG).show();
                 String PostTitle = textTitle.getText().toString().trim();
                 String PostDesc = textDesc.getText().toString().trim();
                 String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
               //String usersEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                Toast.makeText(getActivity(), "POSTING..."+PostTitle, Toast.LENGTH_LONG).show();
                Map<String, Object> messageIdMap = new HashMap<>();
                Posts myPost = new Posts(PostTitle, PostDesc, email);

                db.collection("Blog_Post")
                        .document()
                        .set(myPost, SetOptions.merge());
                    }
                    break;

                }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
