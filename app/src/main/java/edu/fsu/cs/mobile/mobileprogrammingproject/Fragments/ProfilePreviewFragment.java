package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.fsu.cs.mobile.mobileprogrammingproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilePreviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilePreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePreviewFragment extends Fragment {
    static String thisUsersEmail;

    public ProfilePreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //param param1 Parameter 1.
     * //param param2 Parameter 2.
     *
     * @return A new instance of fragment ProfilePreviewFragment.
     */

    public static ProfilePreviewFragment newInstance(String id) {
        ProfilePreviewFragment fragment = new ProfilePreviewFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_profile_preview, container, false);
        Bundle b = getArguments();
        String id;
        assert b != null;
        id = b.getString("id");
        TextView usersEmailTextView = myView.findViewById(R.id.usersEmail);
        thisUsersEmail = id;
        usersEmailTextView.setText(id);

        return myView;
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
