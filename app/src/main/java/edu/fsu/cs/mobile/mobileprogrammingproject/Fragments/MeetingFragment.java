package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingFragment extends Fragment {
    private static final String TAG = MeetingFragment.class.getCanonicalName();
    private FirebaseFirestore db;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Meeting> meetArrList;
    private ArrayList<String> meetIdList;

    private ListView listOfMeetings;

    public MeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingFragment newInstance(String myEmail) {


        MeetingFragment fragment = new MeetingFragment();
        Bundle b = new Bundle();
        b.putString("myEmail", myEmail);
        fragment.setArguments(b);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void populateMeetings(ArrayList<Meeting> myMeetList) {
        ArrayAdapter<Meeting> adapter = new ArrayAdapter<Meeting>(getActivity(),
                android.R.layout.simple_list_item_1, myMeetList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);

                if(position % 2 == 0)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#FFB6B546"));

                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.LTGRAY);
                }
                return view;
            }
        };

        listOfMeetings.setAdapter(adapter);
    }

    private void getMeetings() {
        db.collection("meetings") // GETTING MESSAGES CURRENT USER SENT TO THE OTHER
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    meetIdList.add(document.getId());
                                    Meeting tempMeeting = document.toObject(Meeting.class);
                                    meetArrList.add(tempMeeting);
                                    /*Message tempMsg = document.toObject(Message.class);
                                    tempMsg.time = document.getDate("time");

                                    msgArrList.add(tempMsg);*/

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                            populateMeetings(meetArrList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_meeting, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        db = FirebaseFirestore.getInstance();
        meetArrList = new ArrayList<>();
        meetIdList = new ArrayList<>();
        listOfMeetings = myView.findViewById(R.id.meetList);
        getMeetings();
        FloatingActionButton fab = myView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.loadCreateMeet();
            }
        });

        listOfMeetings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mListener.loadMeetingDetailFragment( listOfMeetings.getItemAtPosition(i), meetIdList.get(i));
            }
        });

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
        void loadMeetingDetailFragment(Object meetingObject, String meetId);
        void loadCreateMeet();
        void onFragmentInteraction(Uri uri);
    }
}
