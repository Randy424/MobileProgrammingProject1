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
    public static Vector<String> phoneList = new Vector<>();
    public static HashMap<String, User> userList = new HashMap<>();
    public Map<String, Boolean> stars = new HashMap<>();

    public User() {

        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String name, String email, String password, String longitude, String latitutde) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.longitude = longitude;
        this.latitutde = latitutde;
    }

}
