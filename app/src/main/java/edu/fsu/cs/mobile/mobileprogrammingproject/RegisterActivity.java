package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText email, name, password, major, phone;
    private DatabaseReference mDatabase;
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        major = (EditText) findViewById(R.id.major);
        phone = (EditText) findViewById(R.id.phone);



    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(getApplicationContext(), "Setting it to NULL in my if check", Toast.LENGTH_SHORT).show();
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    /*public void resetClick(View view)
    {
        emp_id.setText("");
        name.setText("");
        email.setText("");
        access_code.setText("");
        confirm_code.setText("");

        gender.clearCheck();
        agree.setChecked(false);
        department.setSelection(0);
    }*/

    public void registerClick(View view) {


        //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location lastLoc = getLastKnownLocation();




                // lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        MyUser user = new MyUser(email.getText().toString().trim(),
                name.getText().toString().trim(),
                password.getText().toString().trim(),
                major.getText().toString().trim(),
                lastLoc,
                phone.getText().toString().trim());

        Map<String, Object > postValues = user.toMap();
        if (lastLoc == null) {
            Toast.makeText(getApplicationContext(), "Location is null", Toast.LENGTH_SHORT).show();
        }

        mDatabase.child(phone.getText().toString().trim()).setValue(postValues);
        //mDatabase.child(phone.getText().toString().trim()).setValue(user);

        //mDatabase.child(email.getText().toString()).setValue("Dustin");
    }

    private boolean isEmpty(EditText et) {
        if (et.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

}
