package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConversationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {
    final ArrayList<String> sentMsgs = new ArrayList<>();
    final ArrayList<String> recMsgs = new ArrayList<>();
    public final String TAG = "convoTag";
    private ListView lv;
    private FirebaseFirestore db;
    ArrayList <Message> msgArrList;//= new ArrayList<>();

    String myUserEmail;
    String otherUserEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     * @return A new instance of fragment ConversationFragment.
     */
    // TODO: Rename and change types and number of parameters
    // COULD EXPAND THIS TO ACCOMODATE GROUP CHATS
    public static ConversationFragment newInstance(String myEmail, String receiverEmail) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle b = new Bundle();
        b.putString("myEmail", myEmail);
        b.putString("recEmail", receiverEmail);

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
        View myView = inflater.inflate(R.layout.fragment_conversation, container, false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        db = FirebaseFirestore.getInstance();

        Bundle b = getArguments();
        myUserEmail = b.getString("myEmail");
        otherUserEmail = b.getString("recEmail");

        //TextView displayUserTextView =  myView.findViewById(R.id.convoUser);
        //displayUserTextView.setText(myUserEmail);
        //TextView displayOtherUserTextView = myView.findViewById(R.id.cOtherUser);
        //displayOtherUserTextView.setText(otherUserEmail);

        msgArrList = new ArrayList<>();

        //final ArrayList<String> sentMsgs = new ArrayList<>();
        //final ArrayList<String> recMsgs = new ArrayList<>();



        // first lets get all messages i sent this person
        // this will call a chain of functions
        getSentMsgs(); // refactor to have a better name

        getConvoMessages();

        /*ArrayList<>
        db.collection("conversations")
                .document(myUsersEmail)
                .collection("contacts")
                .get().addOnCompleteListener(queryListener);*/



        Button sendButt = (Button) myView.findViewById(R.id.convoSendMessageButton);
        sendButt.setBackgroundColor(Color.GREEN);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText usrInput = (EditText) getView().getRootView().findViewById(R.id.convoTargetMessage);

                if(isEmpty(usrInput))
                    usrInput.setError("Please enter a message before sending!");
                else {
                    DocumentReference newMessageRef =  db.collection("messages") // Should only run if we dont have a conversation id yet
                            .document();
                    newMessageRef.set(new Message(myUserEmail, otherUserEmail, usrInput.getText().toString().trim()));
                    usrInput.setError(null);
                    msgArrList.clear();
                    getConvoMessages();
                }


            }
        });
        return myView;
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    public void getSentMsgs() {
        db.collection("conversations")
                .document(myUserEmail)
                .collection("contacts")
                .document(otherUserEmail)
                .collection("sent")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                sentMsgs.add(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            getRecdMsgs();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void getRecdMsgs() {
        db.collection("conversations")
                .document(myUserEmail)
                .collection("contacts")
                .document(otherUserEmail)
                .collection("received")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                recMsgs.add(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            //getConvoMessages();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void getConvoMessages() {
        db.collection("messages")
                .whereEqualTo("sender", myUserEmail)
                .whereEqualTo("receiver", otherUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(getActivity(), "IN THE COMPLETE LISTENER FOR FIRST CONVO MESSAGE PART", Toast.LENGTH_SHORT).show();
                            for (DocumentSnapshot document : task.getResult()) {
<<<<<<< HEAD
                                if( document.exists()){

                                    Message tempMsg = document.toObject(Message.class);
                                    tempMsg.time = document.getDate("time");
                                    //Toast.makeText(getActivity(), "1: " + tempMsg.getContent() + " and timestamp now is: " + tempMsg.time.toString(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getActivity(), "1: " + tempMsg.getContent(), Toast.LENGTH_SHORT).show();
                                    msgArrList.add(tempMsg);
                                    //recMsgs.add(document.getId());
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
=======

                                Message tempMsg = document.toObject(Message.class);
                                tempMsg.time = document.getDate("time");
                               // Toast.makeText(getActivity(), "1: " + tempMsg.getContent() + " and timestamp now is: " + tempMsg.time.toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "1: " + tempMsg.getContent(), Toast.LENGTH_SHORT).show();
                                msgArrList.add(tempMsg);
                                //recMsgs.add(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
>>>>>>> master
                            }
                            db.collection("messages")
                                    .whereEqualTo("receiver", myUserEmail)
                                    .whereEqualTo("sender", otherUserEmail)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                //Toast.makeText(getActivity(), "IN THE COMPLETE LISTENER FOR SECOND CONVO MESSAGE PART", Toast.LENGTH_SHORT).show();
                                                for (DocumentSnapshot document_2 : task.getResult()) {
<<<<<<< HEAD
                                                    if (document_2.exists()) {
                                                        Message tempMsg2 = document_2.toObject(Message.class);
                                                        tempMsg2.time = document_2.getDate("time");
                                                        //Toast.makeText(getActivity(), "2: " + tempMsg2.getContent() + " and timestamp now is: " + tempMsg2.time.toString(), Toast.LENGTH_SHORT).show();
                                                        msgArrList.add(tempMsg2);
                                                        //recMsgs.add(document_2.getId());
                                                        Log.d(TAG, document_2.getId() + " => " + document_2.getData());

                                                    }
=======
                                                    Message tempMsg2 = document_2.toObject(Message.class);
                                                    tempMsg2.time = document_2.getDate("time");
                                                    //Toast.makeText(getActivity(), "2: " + tempMsg2.getContent() + " and timestamp now is: " + tempMsg2.time.toString(), Toast.LENGTH_SHORT).show();
                                                    msgArrList.add(tempMsg2);
                                                    //recMsgs.add(document_2.getId());
                                                    Log.d(TAG, document_2.getId() + " => " + document_2.getData());


>>>>>>> master

                                                }
                                                //Toast.makeText(getActivity(), "IN DEEPEST LISTENER SIZE OF WHAT I BUILT IS:" + Integer.toString(msgArrList.size()), Toast.LENGTH_SHORT).show();
                                                printConversation(msgArrList);
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void printConversation(final ArrayList<Message> allMsgs){
        Collections.sort(allMsgs, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if(o1.getTime() != null || o2.getTime() != null)
                    return o1.getTime().compareTo(o2.getTime());
                return 0;
            }
        });

        ListView lv2 = getView().getRootView().findViewById(R.id.currentConvo);
        //List<Message> msgAsList = allMsgs;

        // could make an add method here where i pass in one message and it appends
        // adapter.notifyDataSetChanged();
        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(getActivity(), android.R.layout.simple_list_item_1, allMsgs){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(allMsgs.get(position).getSender().equals(myUserEmail))
                //if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.LTGRAY);
                    view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFB6B546"));

                    view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
                return view;
            }
        };


        //ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this,
                //android.R.layout.simple_list_item_1, allMsgs.toArray());
        //for(int i = 0; i < allMsgs.size(); i++) {
        //    Toast.makeText(getActivity(), allMsgs.get(i).getContent(), Toast.LENGTH_SHORT).show();
        //}


        lv2.setAdapter(adapter);


        //Toast.makeText(getActivity(), "JUST SET ADAPTER", Toast.LENGTH_SHORT).show();
        //for(int i = 0; i < allMsgs.size(); i++) {
        //    Toast.makeText(getActivity(), allMsgs.get(i).getContent(), Toast.LENGTH_SHORT).show();
        //}
        //if (allMsgs.size() == 0)

            //Toast.makeText(getActivity(), "AT THE END ALLMSGS SIZE IS 0", Toast.LENGTH_SHORT).show();

        //FragmentTransaction ftr = getFragmentManager().beginTransaction();

        //ftr.detach(ConversationFragment.this).attach(ConversationFragment.this).commit();

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
