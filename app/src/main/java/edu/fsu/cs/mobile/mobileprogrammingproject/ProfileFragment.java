package edu.fsu.cs.mobile.mobileprogrammingproject;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.FindMajor;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.FindName;
import static edu.fsu.cs.mobile.mobileprogrammingproject.User.userList;
public class ProfileFragment extends Fragment {
    static String key;
    private DatabaseReference mDatabase;
    private OnFragmentInteractionListener mListener;
    private Button back;
    private TextView name, phone, major;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = new String("empty");
        key = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UserProfile", "null");
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Textview updates
        name = (TextView) view.findViewById(R.id.nameText);
        phone = (TextView) view.findViewById(R.id.phoneText);
        major = (TextView) view.findViewById(R.id.majorText);
        name.setText(userList.get(key).name);
        major.setText(userList.get(key).major);
        phone.setText(key);
        //Back Button
        back = (Button) view.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                startActivity(i);
            }
        });
        return view;
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
}