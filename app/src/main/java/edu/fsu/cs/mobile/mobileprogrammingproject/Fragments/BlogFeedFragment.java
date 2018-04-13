package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Queue;

import edu.fsu.cs.mobile.mobileprogrammingproject.Posts;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;
import edu.fsu.cs.mobile.mobileprogrammingproject.RecylerViewAdapter;

import static android.content.ContentValues.TAG;

public class BlogFeedFragment extends Fragment {



    ArrayList<String> title;
    ArrayList<String> desc;
    ArrayList<String> time;
    ArrayList<String> email;
    String[] titleFinal;
    String[] descFinal;
    String[] timeFinal;
    String[] emailFinal;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecylerViewAdapter recylerViewAdapter;


    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore db;


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
        Log.d(TAG, "On create working: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_feed, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setClickable(true);
        // Inflate the layout for this fragment


            title = new ArrayList<>();
            desc = new ArrayList<>();
            time = new ArrayList<>();
            email = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        try {
            Log.d(TAG, "Reached Try");

            db.collection("Blog_Post")
                    .orderBy("time")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    Log.d("Database Content", document.getId() + " => " + document.getData());

                                    Posts l = document.toObject(Posts.class);

                                    Log.d("L Content", document.getId() + " => " + l.getTitle());

                                  //  entries.add(l);
                                    title.add(l.getTitle());
                                    desc.add(l.getDesc());
                                    time.add(l.getTime().toString());
                                    email.add(l.getEmail());
                                }


                                titleFinal = title.toArray(new String[title.size()]);
                                descFinal = desc.toArray(new String[desc.size()]);
                                timeFinal = time.toArray(new String[time.size()]);
                                emailFinal = email.toArray(new String[email.size()]);

                                recyclerView = (RecyclerView) getView().getRootView().findViewById(R.id.cardView);
                                layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                recylerViewAdapter = new RecylerViewAdapter(getActivity(), titleFinal, descFinal, timeFinal, emailFinal);
                                recyclerView.setAdapter(recylerViewAdapter);


                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } catch(Exception e)
        {
            messageBox("doStuff", e.getMessage());
            Log.d(TAG, "Error getting documents: ");
        }

        if (getArguments() != null) {
        }




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

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.

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