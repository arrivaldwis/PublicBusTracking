package com.example.vikar.publicbustracking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.TrackModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * Created by vikar on 10-Jan-18.
 */

public class Driver extends AppCompatActivity {
    private Button btnlogoutdriver;
    private MyLocationListener mLocationListener;
    private LocationManager mLocationManager;
    private Switch swOn;
    private boolean status = false;
    private EditText tv_id;
    private EditText tv_bus_no;
    private EditText tv_name;
    private EditText tv_email;
    private ImageView img_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        btnlogoutdriver = (Button) findViewById(R.id.btnlogoutdriver);
        tv_id = (EditText) findViewById(R.id.tv_id);
        tv_bus_no = (EditText) findViewById(R.id.tv_bus_no);
        tv_name = (EditText) findViewById(R.id.tv_name);
        tv_email = (EditText) findViewById(R.id.tv_email);
        img_user = (ImageView) findViewById(R.id.img_user);
        swOn = (Switch) findViewById(R.id.switch1);
        btnlogoutdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Driver.this, login.class));
                finish();
            }
        });

        swOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                status = b;
            }
        });

        loadProfile();

        mLocationListener = new MyLocationListener();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if (status) {
                Toast.makeText(getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();

                Constant.refTrack.push().setValue(new TrackModel(Integer.parseInt(id),
                        Integer.parseInt(id_bus), loc.getLatitude(), loc.getLongitude()));

            }
            //handlePostion(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    String id_bus = "";
    String id = "";

    private void loadProfile() {
        SharedPreferences prefs = getSharedPreferences("userSession", MODE_PRIVATE);
        String role = prefs.getString("role", "");
        String email = prefs.getString("email", "");
        String name = prefs.getString("name", "");
        String phone = prefs.getString("phone", "");
        String image = prefs.getString("image", "");
        id = prefs.getString("id", "");
        id_bus = prefs.getString("id_bus", "");

        tv_id.setText(id);
        tv_name.setText(name);
        tv_email.setText(email);
        tv_bus_no.setText(id_bus);
        Picasso.get().load(image).into(img_user);
    }
}