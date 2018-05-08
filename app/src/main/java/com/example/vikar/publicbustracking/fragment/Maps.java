package com.example.vikar.publicbustracking.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Maps extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    public Maps() {
        // Required empty public constructor
    }

    public static Maps newInstance() {
        Maps fragment = new Maps();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    LatLng stationMarker = null;
    ArrayList<LatLng> stationsList = new ArrayList<>();
    int warna = 0;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mMap.setMyLocationEnabled(true);
        }

        Constant.refRute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds: dataSnapshot.getChildren()) {
                    stationsList.clear();
                    final RuteModel rute = ds.getValue(RuteModel.class);

                    final String[] stationList = rute.getId_station().split(",");
                    for (String s:stationList) {
                        Log.d("Data rute", s);
                    }

                    Constant.refStation.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                StationModel model = ds.getValue(StationModel.class);

                                for (String s:stationList) {
                                    if(String.valueOf(model.getId_station()).equals(s)) {
                                        Log.d("LatLongTest", model.getId_station()+" - "+model.getLatitude()+":"+model.getLongitude());
                                        stationsList.add(new LatLng(model.getLatitude(), model.getLongitude()));
                                        stationMarker = new LatLng(model.getLatitude(), model.getLongitude());
                                        mMap.addMarker(new MarkerOptions().position(stationMarker).title(model.getName()).snippet("Bus Route No: "+rute.getId_rute()));
                                    }
                                }
                            }

                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                            PolylineOptions options = new PolylineOptions().width(5).color(color).geodesic(true);
                            for (int z = 0; z < stationsList.size(); z++) {
                                LatLng point = stationsList.get(z);
                                options.add(point);
                            }

                            mMap.addPolyline(options);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stationMarker, 15.0f));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    warna++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
