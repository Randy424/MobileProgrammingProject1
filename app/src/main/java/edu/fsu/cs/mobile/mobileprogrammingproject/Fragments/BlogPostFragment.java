package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Objects;

import edu.fsu.cs.mobile.mobileprogrammingproject.Posts;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;


public class BlogPostFragment extends Fragment {

    private FirebaseFirestore db;

    private EditText textTitle;
    private EditText textDesc;


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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_post, container, false);
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        Button myButton = rootView.findViewById(R.id.submit);

        textTitle = rootView.findViewById(R.id.title);
        textDesc = rootView.findViewById(R.id.desc);
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setClickable(true);


        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.submit: {
                        //Do error checking too

                        String PostTitle = textTitle.getText().toString().trim();
                        String PostDesc = textDesc.getText().toString().trim();
                        String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();


                        Toast.makeText(getActivity(), "POSTING..." + PostTitle, Toast.LENGTH_LONG).show();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface OnFragmentInteractionListener {
    }
}
