package edu.fsu.cs.mobile.mobileprogrammingproject;

import java.util.Map;

/**
 * Created by Aaron Stewart on 5/3/2018.
 */

public class MapPost {
    String title;
    Map<String,Boolean> classes;

    public MapPost(String title, Map<String,Boolean> classes) {
        this.title = title;
        this.classes = classes;
    }
}

