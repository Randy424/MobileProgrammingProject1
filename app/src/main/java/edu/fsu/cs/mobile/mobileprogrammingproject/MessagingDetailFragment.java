package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessagingDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessagingDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagingDetailFragment extends Fragment {
    private ListView lv;

    private final String TAG = "messagedetailtag";
    private FirebaseFirestore db;
    private String myUsersEmail;
    private ArrayList<String> myConversations;

    OnCompleteListener<QuerySnapshot> queryListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    //Message mMessage = document.toObject(Message.class);
                    myConversations.add(document.getId());

                    // do stuff in here to show message stuffTextView

                    Log.d(TAG, document.getId() + " => " + document.getData());

                }
                populateConvoList();
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        }
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MessagingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment ProfileDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagingDetailFragment newInstance(String daEmail) {
        MessagingDetailFragment fragment = new MessagingDetailFragment();
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
        final Map<String, Object> dummyMap= new HashMap<>();
        dummyMap.put("dummy", "dummy");

        myConversations = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        myUsersEmail = getArguments().getString("email");
        View myView = inflater.inflate(R.layout.fragment_messaging_detail, container, false);
        TextView yourEmailText = (TextView) myView.findViewById(R.id.yourOwnEmail);
        final String testReceiver = "adstew96@gmail.com";
        final String testMessage = "< " + myUsersEmail + " started a conversation >";
        yourEmailText.setText("I see you are: " + getArguments().getString("email"));
        Button sendButt = (Button) myView.findViewById(R.id.sendMessageButton);
        final EditText recipientEditText = (EditText) myView.findViewById(R.id.targetRecipient);

        /*OnCompleteListener<DocumentSnapshot> convoListener = new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        //Message mMessage = document.toObject(Message.class);
                        //document.get()

                        // do stuff in here to show message stuffTextView

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
        }};*/

        // use this for something or take it out


        db.collection("conversations")
                .document(myUsersEmail)
                .collection("contacts")
                .get().addOnCompleteListener(queryListener);

        /*db.collection("conversations")
                .document(myUsersEmail)
                .collection("contacts")
                .get().addOnCompleteListener(queryListener);*
        //for myConversationIds.length
        //Toast.makeText(getApplicationContext(), "")*/
        lv = myView.findViewById(R.id.convoListView);
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                myConversations);
        lv.setAdapter(arrayAdapter);*/




        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientEmail = recipientEditText.getText().toString();
                if (recipientEmail.equals(myUsersEmail)) {
                    recipientEditText.setError("You might want to talk to someone about that!");
                }
                else if(recipientEmail.equals("")) // if someone hits send with no sender typed in is ok to check as null here??
                {
                    recipientEditText.setError("Please enter an email to start a conversation");

                } else {
                    recipientEditText.setError(null);
                    DocumentReference newMessageRef = db.collection("messages") // Should only run if we dont have a conversation id yet
                            .document();

                    Map<String, Object> messageIdMap = new HashMap<>();
                    String messageId = newMessageRef.getId();
                    messageIdMap.put("id", messageId);



                /*Map<String, Object> messageContentMap = new HashMap<>();
                messageContentMap.put("content", testMessage);
                messageContentMap.put("timestamp", FieldValue.serverTimestamp());*/


                    db.collection("conversations") // convert document to non-virtual
                            .document(myUsersEmail)
                            .collection("contacts")
                            .document(recipientEmail)
                            .set(dummyMap);

                    newMessageRef.set(new Message(myUsersEmail, recipientEmail, testMessage));//send, rec, content));


                    db.collection("conversations") // put into my sent area
                            .document(myUsersEmail) // Should only run if we dont have a conversation id yet
                            .collection("contacts")
                            .document(recipientEmail)
                            .collection("sent")
                            .document(messageId)
                            .set(messageIdMap, SetOptions.merge());


                    db.collection("conversations") // convert document to non-virtual
                            .document(recipientEmail)
                            .collection("contacts")
                            .document(myUsersEmail)
                            .set(dummyMap);

                    db.collection("conversations") // put in appropriate recieve area
                            .document(recipientEmail) // Should only run if we dont have a conversation id yet
                            .collection("contacts")
                            .document(myUsersEmail)
                            .collection("received")
                            .document(messageId)
                            .set(messageIdMap, SetOptions.merge());

                    //.document();newMessageRef.getId()

                    //.set();
                    //.set(new Message(myUsersEmail, recipientEditText.getText().toString(), testMessage), SetOptions.merge());


                /*//, testMessage), SetOptions.merge());
                /*db.collection("conversations")
                        .document(myUsersEmail + "_" + recipientEditText.getText().toString())
                        .set(new Message(myUsersEmail, recipientEditText.getText().toString(), testMessage), SetOptions.merge());
                db.collection("messages")
                        .document(myUsersEmail + "_" + recipientEditText.getText().toString())
                        .set(new Message(myUsersEmail, recipientEditText.getText().toString(), testMessage), SetOptions.merge());*/
                    myConversations.clear();
                    db.collection("conversations")
                            .document(myUsersEmail)
                            .collection("contacts")
                            .get().addOnCompleteListener(queryListener);
                }
            }
        });



        /*db.collection("messages")
                .whereEqualTo("sender", myUsersEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Set<String> conversations = new Set<>();
                            ArrayList<LatLng> positionInfo = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Message mMessage = document.toObject(Message.class);
                                if(mMessage.getReceiver().equals(myUsersEmail)) {
                                    conversations.add(mMessage.getSender());
                                }
                                else if (mMessage.getSender().equals(myUsersEmail)){
                                    conversations.add(mMessage.getReceiver());
                                }

                                // do stuff in here to show message stuffTextView
                                //options.position(new LatLng(document.getDouble("latitude"),document.getDouble("longitude")));
                                //options.title(document.getId());
                                //googleMap.addMarker(options);
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), "I AM IN THE ONITEMCLICK OF ONITEMCLICKLISTENER with: "+ lv.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
                mListener.loadConversationFragment(myUsersEmail, lv.getItemAtPosition(i).toString());
            }
        });
        return myView;
    }


    public void populateConvoList() {
        lv = getView().getRootView().findViewById(R.id.convoListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                myConversations);
        lv.setAdapter(arrayAdapter);
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
        void loadConversationFragment(String firstEmail, String secondEmail);
        void onFragmentInteraction(Uri uri);
    }
}
