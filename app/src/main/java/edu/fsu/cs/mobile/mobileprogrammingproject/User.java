package edu.fsu.cs.mobile.mobileprogrammingproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class User {
    public String mEmail;
    public String mName;
    public String mPassword;
    public String mMajor;
    public String mPhone;
    public String mLatitude;
    public String mLongitude;

    public static Vector<String> phoneList = new Vector<>();
    public static HashMap<String, User> userList = new HashMap<>();
    public Map<String, Boolean> stars = new HashMap<>();



    /*public User() { // WE NEED THIS?

        // Default constructor required for calls to DataSnapshot.getValue(Post.class)

    }*/

    public static String FindName(String key)

    {
        return userList.get(key).mName;
    }

    public static String FindLongitude(String key)
    {
        return userList.get(key).mLongitude;
    }

    public static String FindLatitude(String key)
    {
        return userList.get(key).mLatitude;
    }

    public static String FindEmail(String key)
    {
        return userList.get(key).mEmail;
    }

    public static String FindPassword(String key)
    {
        return userList.get(key).mPassword;
    }

    public static String FindMajor(String key)
    {
        return userList.get(key).mMajor;
    }



    public User(String email, String name, String password, String major, String phone, String latitude, String longitude) {
        mEmail = email;
        mName = name;
        mPassword = password;
        mMajor = major;
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

        return result;
    }
}
