package com.example.vikar.publicbustracking.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.BusModel;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.TrackModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusTracking extends Fragment implements OnMapReadyCallback {

    private AutoCompleteTextView tvBus;
    private Button btnTrack;
    private GoogleMap mMap;
    private ArrayList<String> busList;
    private ArrayAdapter<String> adapter;

    public BusTracking() {
        // Required empty public constructor
    }

    public static BusTracking newInstance() {
        BusTracking fragment = new BusTracking();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus_tracking, container, false);
        busList = new ArrayList<>();
        tvBus = v.findViewById(R.id.tvBus);
        btnTrack = v.findViewById(R.id.btnTrack);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trackBus(Integer.parseInt(tvBus.getText().toString()));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadBus();

        return v;
    }

    private void loadBus() {
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, busList);

        Constant.refBus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BusModel model = ds.getValue(BusModel.class);
                    busList.add(model.getId_bus() + "");
                }

                tvBus.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    private void trackBus(final int id_bus) {
        mMap.clear();
        Constant.refBus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final BusModel model = ds.getValue(BusModel.class);
                    if (model.getId_bus() == id_bus) {
                        Constant.refRute.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    RuteModel rute = ds.getValue(RuteModel.class);
                                    if (rute.getId_rute() == model.getId_rute()) {
                                        Constant.refTrack.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mMap.clear();
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    TrackModel track = ds.getValue(TrackModel.class);
                                                    if (track.getId_bus() == model.getId_bus()) {
                                                        mMap.addMarker(new MarkerOptions()
                                                                .position(new LatLng(track.getLatitude(), track.getLongitude()))
                                                                .title(model.getId_bus() + "").snippet("Bus Route No: " + model.getId_rute())
                                                                .icon(Constant.bitmapFromDrawable(getActivity(), R.drawable.ic_directions_bus_black_24dp)));
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(track.getLatitude(), track.getLongitude())));
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(track.getLatitude(), track.getLongitude()), 15.0f));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private void getEstimation() {
        try {
            String distance = "";
            String userLat = data.getLatitude();
            String userLong = data.getLongitude();
            float distanceInMeters = 0;
            if (!userLat.isEmpty() && !userLong.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()) {
                float[] results = new float[1];
                Location.distanceBetween(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(userLat), Double.parseDouble(userLong), results);
                distanceInMeters = results[0];
            }

            try {
                float dist = distanceInMeters / 1000;
                distance = String.format(java.util.Locale.US, "%.2f", dist);
            } catch (Exception ex) {
            }

            if (!userLat.isEmpty() && !userLong.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()) {
                boolean isWithin10km = distanceInMeters < 10000;
                if (isWithin10km) {
                }
                holder.tvJarak.setText(distance+" km");
            }
        } catch (Exception ex) {
        }
    }*/

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
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
