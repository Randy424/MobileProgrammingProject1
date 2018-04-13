package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

public class BlogFeedFragment extends Fragment {

    String[] values = {"Test 1", "Test 2", "Test 3","Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9","Test 1", "Test 2", "Test 3","Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9"};
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecylerViewAdapter recylerViewAdapter;

    private FirebaseFirestore db;
    private List<Post> post;

    private OnFragmentInteractionListener mListener;

    public BlogFeedFragment() {
        // Required empty public constructor
    }


    public static BlogFeedFragment newInstance() {
        BlogFeedFragment fragment = new BlogFeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            db.collection("Blog_Post").get();
//        } catch(Exception e)
//        {
//            messageBox("doStuff", e.getMessage());
//        }
//
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_feed, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setClickable(true);
        // Inflate the layout for this fragment


        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recylerViewAdapter = new RecylerViewAdapter(getActivity(), values);
        recyclerView.setAdapter(recylerViewAdapter);



       // LinearLayoutManager llm = new LinearLayoutManager(getContext());
      //  rv.setLayoutManager(llm);

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

        void onFragmentInteraction(Uri uri);
    }

   public class Post {
        String title;
        String desc;
        int photoId;

        Post(String title, String desc) {
            this.title = title;
            this.desc = desc;
           //this.photoId = photoId;
        }
    }
    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    private void initializeData() {
        post = new ArrayList<>();
        post.add(new Post("Emma Wilson", "23 years old"));
        post.add(new Post("Lavery Maiss", "25 years old"));
        post.add(new Post("Lillie Watts", "35 years old"));
    }

    private void messageBox(String method, String message)
    {
        Log.d("EXCEPTION: " + method,  message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(getActivity());
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }

}