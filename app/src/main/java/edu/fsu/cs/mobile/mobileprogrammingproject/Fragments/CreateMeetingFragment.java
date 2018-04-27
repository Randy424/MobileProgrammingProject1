package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateMeetingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateMeetingFragment extends Fragment {
    private FirebaseFirestore db;
    private String myUsersEmail;
    /*private int lastTimePicker;
    private int startHour;
    private int startMinute;
    private int finishHour;
    private int finishMinute;*/
    public static int timeFlag;

    private OnFragmentInteractionListener mListener;

    public CreateMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment CreateMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateMeetingFragment newInstance(String myEmail) {
        CreateMeetingFragment fragment = new CreateMeetingFragment();
        Bundle args = new Bundle();
        args.putString("myEmail", myEmail);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_create_meeting, container, false);
        timeFlag = 0;
        Button startTimeButton = myView.findViewById(R.id.startTimeButt);
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showTimePickerDialog();
                timeFlag = 1;
            }
        });

        final TextView topicTV = myView.findViewById(R.id.topicTV);
        final TextView descriptionTV = myView.findViewById(R.id.descriptionTV);

        Button endTimeButton = myView.findViewById(R.id.endTimeButt);
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showTimePickerDialog();
                timeFlag = 2;
            }
        });

        Button meetDayButton = myView.findViewById(R.id.meetDateButt);
        meetDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showDatePickerDialog();

            }
        });

        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        db = FirebaseFirestore.getInstance();
        myUsersEmail = getArguments().getString("myEmail");

        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Dirac Science Library");
        spinnerArray.add("Strozier Library");
        spinnerArray.add("Mobile Lab");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) myView.findViewById(R.id.spinner);
        sItems.setAdapter(adapter);



        Button createMeetingButton = myView.findViewById(R.id.createMeetingButton);
        createMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Meeting meet = new Meeting("TEST EMAIL", "TEST MEETING NAME", "TEST TOPIC", "TEST DESCRIPTION", new Date(), new Date());
                DocumentReference newMessageRef = db.collection("meetings")
                        .document();

                //Spinner spinner = (Spinner)findViewById(R.id.spinner);
                String locName = sItems.getSelectedItem().toString();
                Double thisLat, thisLong;
                if(locName.equals("Dirac Science Library")) {
                    thisLat = 30.445;
                    thisLong = -84.3003;
                }
                else if(locName.equals("Strozier Library")) {
                    thisLat = 30.3328;
                    thisLong = -84.295;
                }
                else if(locName.equals("Mobile Lab")) {
                    thisLat = 30.44623;
                    thisLong = -84.29974;
                }
                else {
                    locName = "No Meeting Location Selected";
                    thisLat = 99.999;
                    thisLong = 99.999;
                }


                /*Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DATE, dayOfMonth);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date date = cal.getTime();*/
                newMessageRef.set(new Meeting(myUsersEmail, locName, topicTV.getText().toString().trim(),
                        descriptionTV.getText().toString().trim(), new Date(), new Date(), thisLat, thisLong));
            }
        });
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

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
        public void showTimePickerDialog();
        public void showDatePickerDialog();

    }
}
