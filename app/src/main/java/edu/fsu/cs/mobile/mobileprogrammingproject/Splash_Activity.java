package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class Splash_Activity extends Activity {


    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent mainIntent = new Intent(Splash_Activity.this, MainActivity.class);
        Splash_Activity.this.startActivity(mainIntent);
        Splash_Activity.this.finish();
    }




}


