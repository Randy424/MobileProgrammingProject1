package edu.fsu.cs.mobile.mobileprogrammingproject;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Randy Bruno-Piverger on 4/12/2018.
 */

public class Posts {

    private String title;
    private String desc;
    protected @ServerTimestamp Date time;

public Posts(){
    super();
}

public Posts(String title, String desc) {
        super();
        this.title = title;
        this.desc = desc;
        this.time = null;
        //this.photoId = photoId;
    }


    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    public Date getTime(){
        return time;
    }


}
