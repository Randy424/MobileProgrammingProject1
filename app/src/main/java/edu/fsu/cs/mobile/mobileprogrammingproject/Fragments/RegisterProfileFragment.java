package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.ProfileActivity;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String currentUser;
    private EditText mEmailEdit;
    private EditText mClassesEdit;
    private EditText mYearEdit;
    private EditText mNameEdit;
    private OnFragmentInteractionListener mListener;
    private Button mSubmit;
    private Button mClassAdd;
    private FirebaseFirestore db;
    private AutoCompleteTextView mMajorEdit;
    private Map<String, Object> classMap;
    public RegisterProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterProfileFragment newInstance(String param1, String param2) {
        RegisterProfileFragment fragment = new RegisterProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register_profile, container, false);
        view.setBackgroundColor(Color.WHITE);
        view.setClickable(true);

        //Set instance of firestore database
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //Sets up Autocomplete For Majors
        String[] majorArr = getResources().getStringArray(R.array.majors);
        List<String> majorList = Arrays.asList(majorArr);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, majorList);
        mMajorEdit = (AutoCompleteTextView) view.findViewById(R.id.autocompleteView);
        mMajorEdit.setAdapter(adapter);

        //Setting Buttons and EditTexts
        mSubmit = view.findViewById(R.id.submitButton);
        mClassAdd = view.findViewById(R.id.classButton);
        mClassesEdit = view.findViewById(R.id.classesEdit);
        mYearEdit = view.findViewById(R.id.yearEdit);
        mNameEdit = view.findViewById(R.id.nameEdit);

        //Initializes map for nested classes
        classMap = new HashMap<>();
        //Calls overrided onClick for this Button views
        mSubmit.setOnClickListener(this);
        mClassAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //Submit info to database when clicked here
            case R.id.submitButton:
                if(mMajorEdit.getText().toString().matches(""))
                {mMajorEdit.setError("Must add major"); break;}
                addEditData(mYearEdit, "year");
                addEditData(mNameEdit, "name");
                addMajorData(mMajorEdit, "major");
                submitClasses();
                ProfileActivityFragment frag = new ProfileActivityFragment();
                getFragmentManager().beginTransaction().addToBackStack(null).hide(RegisterProfileFragment.this).
                add(R.id.outerFrag,frag).commit();
                getFragmentManager().beginTransaction().replace(R.id.recent_feed_card, BlogFeedFragment.newInstance(), "outermostFrag2").commit();
                break;
            case R.id.classButton:
                addClassData(mClassesEdit);
                Toast.makeText(getActivity(), "Class Added!", Toast.LENGTH_SHORT).show();
                mClassesEdit.setText("");

        }

    }

    /**
     *
     * @param editText edittext to pass in to put String data in database
     * @param docname name of document that will be stored under the Users collection.
     */
    public void addEditData(EditText editText, String docname)
    {
        Map<String, Object> update2 = new HashMap<>();
        update2.put(docname, editText.getText().toString());
        db.collection("users").document(currentUser).set(update2, SetOptions.merge());
    }

    public void addClassData(EditText editText)
    {
        classMap.put(editText.getText().toString(), true);

    }

    public void submitClasses(){
        db.collection("users").document(currentUser).set(classMap, SetOptions.merge());
    }

    /**
     *
     * @param auto autocomplete view to pass in String data in database
     * @param docname name of document that will be stored under the Users collection.
     */
    public void addMajorData(AutoCompleteTextView auto, String docname)
    {
        Map<String, Object> update2 = new HashMap<>();
        update2.put(docname, auto.getText().toString());
        db.collection("users").document(currentUser)
                .set(update2, SetOptions.merge());
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
        void onFragmentInteraction(Uri uri);
    }
}
