package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.Meeting;
import edu.fsu.cs.mobile.mobileprogrammingproject.Fragments.Message;


/**

 */
public class MeetingDetailFragment extends Fragment {
    private FirebaseFirestore db;
    Uri filePath;
    private final static int PICK_IMAGE_REQUEST = 234;
    StorageReference storageRef;

    private static final String TAG = MeetingDetailFragment.class.getCanonicalName();
    private ListView lv;
    private static final int READ_REQUEST_CODE = 42;
    private static final int ACTIVITY_CHOOSE_FILE = 69;
    private ArrayList<String> myFileList;
    private ArrayList<String> myFileNameList;


    private OnFragmentInteractionListener mListener;

    String myMeetingId;
    Meeting myMeeting;

    public MeetingDetailFragment() {
        // Required empty public constructor - do we really need this?
    }


    public static MeetingDetailFragment newInstance(String meetId) {
        MeetingDetailFragment fragment = new MeetingDetailFragment();
        Bundle args = new Bundle();
        args.putString("meetingId", meetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getUriToAdd() {
        new ChooserDialog().with(getActivity())
                .withStartFile(null)
                .withChosenListener(new ChooserDialog.Result() {
                    @Override
                    public void onChoosePath(String s, File file) {
                        Toast.makeText(getActivity(), "File: " + s, Toast.LENGTH_SHORT).show();
                        filePath = Uri.fromFile(file);
                        uploadFile(file);
                    }
                })
                .build()
                .show();
    }

    private void uploadFile(final File theFile) {
        StorageReference uploadRef = storageRef.child("meetingFiles/" + myMeetingId + "/" + filePath.getLastPathSegment());

        Log.d(TAG, "NEWLY_PRINTED_URI: " + filePath.toString());
        UploadTask uploadTask = uploadRef.putFile(filePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), "UPLOAD FAILED, " + exception.toString() + ", PLEASE TRY AGAIN!", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getContext(), filePath.getLastPathSegment() + " was uploaded successfully!", Toast.LENGTH_SHORT).show();
                addFileToFirestore(new FirebaseStorageDocument(FirebaseAuth.getInstance().getCurrentUser().getEmail(), theFile.getName(), myMeetingId, downloadUrl.toString()));
            }
        });
    }

    private void addFileToFirestore(FirebaseStorageDocument theDocument) {
        db.collection("files")
                .document(myMeetingId).collection("files").document().set(theDocument);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_meeting_detail, container, false);

        myMeetingId = getArguments().getString("meetingId");

        storageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();


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
                StorageReference itemRef = storageRef.child("meetingFiles/" + myMeetingId + "/" + myFileNameList.get(i));
                Toast.makeText(getContext(), "Downloading:, " + myFileNameList.get(i), Toast.LENGTH_SHORT).show();
                try {
                    final File localFile = File.createTempFile("image", "jpg");


                itemRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Toast.makeText(getContext(), "File Successfully Downloaded", Toast.LENGTH_SHORT).show();
                        /*Uri fileUri = FileProvider.getUriForFile(getActivity(),
                                getString(R.string.file_provider_authority),
                                localFile);
                        Intent openItemIntent = new Intent(Intent.ACTION_VIEW, fileUri);
                        startActivity(openItemIntent);*/
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "File failed to download! Please try again!", Toast.LENGTH_SHORT).show();
                        // Handle any errors
                    }
                });


                } catch (Exception e) {
                }
            }
        });


        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //mListener.loadConversationFragment(myUsersEmail, lv.getItemAtPosition(i)
                        //.toString());
            }
        });*/


        myFileList = new ArrayList<>();
        myFileNameList = new ArrayList<>();

        db.collection("files").document(myMeetingId).collection("files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.exists()) {
                                    FirebaseStorageDocument tempDoc = document.toObject(FirebaseStorageDocument.class);
                                    myFileList.add(tempDoc.toString());
                                    myFileNameList.add(tempDoc.getFilename());
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    populateFileList();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        FloatingActionButton fab = myView.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUriToAdd();
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

    private void populateFileList() {
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