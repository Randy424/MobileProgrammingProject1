package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.content.Context;
import android.graphics.Color;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.fsu.cs.mobile.mobileprogrammingproject.R;


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

    private FirebaseFirestore db;
    private String myUsersEmail;
    private ArrayList<String> myConversations;

    OnCompleteListener<QuerySnapshot> queryListener = new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            String TAG = "messagedetailtag";
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



    private OnFragmentInteractionListener mListener;

    public MessagingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     *
     * @return A new instance of fragment ProfileDetailFragment.
     */

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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("dummy", "dummy");

        myConversations = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        assert getArguments() != null;
        myUsersEmail = getArguments().getString("email");

        View myView = inflater.inflate(R.layout.fragment_messaging_detail, container,
                false);
        myView.setBackgroundColor(Color.WHITE);
        myView.setClickable(true);

        TextView yourEmailText = myView.findViewById(R.id.yourOwnEmail);
        final String testMessage = "< " + myUsersEmail + " started a conversation >";
        yourEmailText.setText(String.format("I see you are: %s", getArguments()
                .getString("email")));
        Button sendButt = myView.findViewById(R.id.sendMessageButton);
        final EditText recipientEditText = myView.findViewById(R.id.targetRecipient);


        db.collection("conversations")
                .document(myUsersEmail)
                .collection("contacts")
                .get().addOnCompleteListener(queryListener);


        lv = myView.findViewById(R.id.convoListView);


        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientEmail = recipientEditText.getText().toString().trim();
                if (recipientEmail.equals(myUsersEmail)) {
                    recipientEditText.setError("You might want to talk to someone about that!");
                } else if (recipientEmail.equals(""))
                {
                    recipientEditText.setError("Please enter an email to start a conversation");

                } else { // Should only run if we dont have a conversation id yet
                    recipientEditText.setError(null);
                    DocumentReference newMessageRef = db.collection("messages")
                            .document();

                    Map<String, Object> messageIdMap = new HashMap<>();
                    String messageId = newMessageRef.getId();
                    messageIdMap.put("id", messageId);


                    db.collection("conversations") // convert document to non-virtual
                            .document(myUsersEmail)
                            .collection("contacts")
                            .document(recipientEmail)
                            .set(dummyMap);

                    newMessageRef.set(new Message(myUsersEmail, recipientEmail, testMessage));


                    db.collection("conversations")      // put into my sent area
                            .document(myUsersEmail)        // Should only run if we dont have a
                            .collection("contacts")     // conversation id yet
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
                            .document(recipientEmail) // Should only run if we dont have
                            .collection("contacts")// a conversation id yet
                            .document(myUsersEmail)
                            .collection("received")
                            .document(messageId)
                            .set(messageIdMap, SetOptions.merge());

                    myConversations.clear();
                    db.collection("conversations")
                            .document(myUsersEmail)
                            .collection("contacts")
                            .get().addOnCompleteListener(queryListener);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mListener.loadConversationFragment(myUsersEmail, lv.getItemAtPosition(i)
                        .toString());
            }
        });
        return myView;
    }


    public void populateConvoList() {
        lv = Objects.requireNonNull(getView()).getRootView().findViewById(R.id.convoListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1,
                myConversations);
        lv.setAdapter(arrayAdapter);
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
        void loadConversationFragment(String firstEmail, String secondEmail);

    }
}
