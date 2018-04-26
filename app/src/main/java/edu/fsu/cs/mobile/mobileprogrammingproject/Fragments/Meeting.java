package edu.fsu.cs.mobile.mobileprogrammingproject.Fragments;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Meeting {
    private String creator;
    private String meeting;
    private String topic;
    private String description;
    private Date startTime;
    private Date endTime;
    private Double longitude;
    private Double latitude;


    protected @ServerTimestamp // this going to be ok for created date?
    Date time;


    public Meeting() {
        super();
        // Must have a public no-argument constructor ?
    }

    Meeting(String theCreator, String theMeetingLocationName, String theTopic, String theDescription, Date theStartTime, Date theEndTime, Double theLatitude, Double theLongitude) {
            super();
            creator = theCreator;
            meeting = theMeetingLocationName;
            topic = theTopic;
            description = theDescription;
            startTime = theStartTime;
            endTime = theEndTime;
            latitude = theLatitude;
            longitude = theLongitude;
            time = null;

    }

    public String getCreator() {
        return creator;
    }

    public String getMeeting() {
        return meeting;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {return startTime; }

    public Date getEndTime() {return endTime; }

    public Double getLatitude() {return latitude; }

    public Double getLongitude() {return longitude; }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "\"" + this.topic + "\"";
    }
}
