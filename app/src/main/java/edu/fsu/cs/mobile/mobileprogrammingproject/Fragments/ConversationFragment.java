package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConversationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {

    private static final String TAG = MessagingDetailFragment.class.getCanonicalName();

    private FirebaseFirestore db;
    private ArrayList<Message> msgArrList; // STORES MSGS FROM A CONVERSATION AND IS SORTED BY TIMESTAMP

    private String myUserEmail;
    private String otherUserEmail;



    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * param param1 Parameter 1.
     * param param2 Parameter 2.
     *
     * @return A new instance of fragment ConversationFragment.
     */

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_conversation, container,
                false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);
        db = FirebaseFirestore.getInstance();

        Bundle b = getArguments(); // Get email of people in conversation
        assert b != null;
        myUserEmail = b.getString("myEmail");
        otherUserEmail = b.getString("recEmail");


        msgArrList = new ArrayList<>();

        getConvoMessages();


        Button sendButt = myView.findViewById(R.id.convoSendMessageButton);
        sendButt.setBackgroundColor(Color.GREEN);
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText usrInput = Objects.requireNonNull(getView()).getRootView()
                        .findViewById(R.id.convoTargetMessage);

                if (isEmpty(usrInput))
                    usrInput.setError("Please enter a message before sending!");
                else { // Should only run if we dont have a conversation id yet
                    DocumentReference newMessageRef = db.collection("messages")
                            .document();
                    newMessageRef.set(new Message(myUserEmail, otherUserEmail, usrInput.getText()
                            .toString().trim()));
                    usrInput.setError(null);
                    msgArrList.clear();
                    getConvoMessages();
                }


            }
        });
        return myView;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }



    private void getConvoMessages() {
        db.collection("messages") // GETTING MESSAGES CURRENT USER SENT TO THE OTHER
                .whereEqualTo("sender", myUserEmail)
                .whereEqualTo("receiver", otherUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {

                                    Message tempMsg = document.toObject(Message.class);
                                    tempMsg.time = document.getDate("time");

                                    msgArrList.add(tempMsg);

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                            db.collection("messages") // GET RECEIVED MESSAGES
                                    .whereEqualTo("receiver", myUserEmail)
                                    .whereEqualTo("sender", otherUserEmail)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                for (DocumentSnapshot document_2 : task.getResult()) {
                                                    if (document_2.exists()) {
                                                        Message tempMsg2 = document_2
                                                                .toObject(Message.class);
                                                        tempMsg2.time = document_2
                                                                .getDate("time");

                                                        msgArrList.add(tempMsg2);
                                                        Log.d(TAG, document_2.getId()
                                                                + " => " + document_2.getData());
                                                    }
                                                }
                                                printConversation(msgArrList);
                                            } else {
                                                Log.d(TAG, "Error getting documents: ",
                                                        task.getException());
                                            }
                                        }
                                    });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    // CUSTOM COMPARATOR TO SORT BY TIMESTAMP HERE
    // SOMETIMES WEIRD BEHAVIOR, STUFF COULD BE NULL IN HERE FOR NO REASON
    private void printConversation(final ArrayList<Message> allMsgs) {
        Collections.sort(allMsgs, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if (o1.getTime() != null && o2.getTime() != null) {
                    return o1.getTime().compareTo(o2.getTime());
                }
                return 0;
            }
        });

        ListView lv2 = Objects.requireNonNull(getView()).getRootView()
                .findViewById(R.id.currentConvo);

        ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1, allMsgs) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (allMsgs.get(position).getSender().equals(myUserEmail))
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.LTGRAY);
                    view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#FFB6B546"));
                    view.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                }
                return view;
            }
        };

        lv2.setAdapter(adapter);


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
    }
}
