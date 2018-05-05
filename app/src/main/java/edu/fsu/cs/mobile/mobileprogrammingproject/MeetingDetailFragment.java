package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.Meeting;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.Message;


/**

 */
public class MeetingDetailFragment extends Fragment {
    private ListView lv;


    private OnFragmentInteractionListener mListener;
    Meeting myMeeting;

    public MeetingDetailFragment() {
        // Required empty public constructor
    }

    /**
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingDetailFragment newInstance() { // added unnecessary import for this?
        MeetingDetailFragment fragment = new MeetingDetailFragment();
        Bundle args = new Bundle();
        //args.putParcelable("daMeeting", myMeeting);
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
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
        View myView = inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);

        TextView daTopic = myView.findViewById(R.id.mtopicTV);
        daTopic.setText(myMeeting.getTopic());

        TextView daCreator = myView.findViewById(R.id.mCreatorTV);
        daCreator.setText(myMeeting.getCreator());

        TextView daDescription = myView.findViewById(R.id.mDescriptionTV);
        daDescription.setText(myMeeting.getDescription());


        TextView daMeetingLocationName = myView.findViewById(R.id.mMeetingLocationTV);
        daMeetingLocationName.setText(myMeeting.getMeeting());

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(myMeeting.getStartTime());

        TextView daDate = myView.findViewById(R.id.mDateTV);
        daDate.setText(new SimpleDateFormat("EEEE, dd/MM/yyyy").format(myMeeting.getStartTime()));

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(myMeeting.getEndTime());

        TextView daTimes = myView.findViewById(R.id.mTimesTV);
        daTimes.setText(startTime.get(Calendar.HOUR_OF_DAY) + ":" +startTime.get(Calendar.MINUTE) + " - " + endTime.get(Calendar.HOUR_OF_DAY) + ":" + endTime.get(Calendar.MINUTE));


        lv = myView.findViewById(R.id.filesLV);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //mListener.loadConversationFragment(myUsersEmail, lv.getItemAtPosition(i)
                        //.toString());
            }
        });
        ArrayList<String> myFileList = new ArrayList<>();

        myFileList.add("chapter7.pdf");
        myFileList.add("cheat_sheet.pdf");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, myFileList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                //if (allMsgs.get(position).getSender().equals(myUserEmail))
                if(position % 2 == 0)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.BLUE);
                    //view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.GREEN);
                    //view.setBackgroundColor(Color.parseColor("#FFB6B546"));
                    //view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
                return view;
            }
        };

        lv.setAdapter(adapter);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setMeeting(Meeting m) {
        myMeeting = m;
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
