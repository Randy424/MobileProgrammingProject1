package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by D on 3/24/2018.
 */

public class MyUser {
    private String mEmail;
    private String mName;
    private String mPassword;
    private String mMajor;
    private String mPhone;
    //private Location mLocation;
    private String mLatitude;
    private String mLongitude;

    public MyUser(String email, String name, String password, String major, String phone, String latitude, String longitude) {
        mEmail = email;
        mName = name;
        mPassword = password;
        mMajor = major;
        //mLocation = location;
        mPhone = phone;
        mLatitude = latitude;
        mLongitude = longitude;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", mEmail);
        result.put("name", mName);
        result.put("password", mPassword);
        result.put("major", mMajor);
        result.put("phone", mPhone);
        result.put("latitude", mLatitude);
        result.put("longitude", mLongitude);



        //result.put("lat", Double.toString(mLocation.getLatitude()));
        //result.put("long", Double.toString(mLocation.getLongitude()));



        return result;
    }

}
