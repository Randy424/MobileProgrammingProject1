package edu.fsu.cs.mobile.mobileprogrammingproject;



import com.google.firebase.database.Exclude;



import java.util.HashMap;

import java.util.Map;

import java.util.Vector;



/**

 * Created by Aaron on 3/25/2018.

 */



public class User {

    public String name;

    public String email;

    public String password;

    public String longitude;

    public String latitutde;

    public String major;

    public static Vector<String> phoneList = new Vector<>();

    public static HashMap<String, User> userList = new HashMap<>();

    public Map<String, Boolean> stars = new HashMap<>();



    public User() {



        // Default constructor required for calls to DataSnapshot.getValue(Post.class)

    }



    public static String FindName(String key)

    {

        return userList.get(key).name;

    }

    public static String FindLongitude(String key)

    {

        return userList.get(key).longitude;

    }

    public static String FindLatitude(String key)

    {

        return userList.get(key).latitutde;

    }

    public static String FindEmail(String key)

    {

        return userList.get(key).email;

    }

    public static String FindPassword(String key)

    {

        return userList.get(key).password;

    }

    public static String FindMajor(String key)

    {

        return userList.get(key).major;

    }





    public User(String name, String email, String password, String longitude, String latitutde, String major) {

        this.name = name;

        this.email = email;

        this.password = password;

        this.longitude = longitude;

        this.latitutde = latitutde;

        this.major = major;

    }



}
