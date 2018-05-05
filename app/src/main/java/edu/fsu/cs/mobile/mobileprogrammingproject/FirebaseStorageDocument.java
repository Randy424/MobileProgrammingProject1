package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.net.Uri;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

public class FirebaseStorageDocument implements Serializable{
    private String author;
    private String filename;
    private String meetingId;
    private String downloadUrl;
    protected @ServerTimestamp
    Date time;

    public FirebaseStorageDocument() {
        super();
        // Must have a public no-argument constructor ?
    }

    FirebaseStorageDocument(String theAuthor, String theFilename, String theMeetingId, String theDownloadUrl) {
        super();
        author = theAuthor;
        filename = theFilename;
        meetingId = theMeetingId;
        downloadUrl = theDownloadUrl;
        time = null;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getAuthor() {
        return author;
    }

    public String getFilename() {
        return filename;
    }

    public Date getTime() {
        return time;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public String toString() {
        return "\"" + this.filename + "\"";
    }

}
