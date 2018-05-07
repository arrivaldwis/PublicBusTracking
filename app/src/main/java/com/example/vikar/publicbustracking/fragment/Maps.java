package com.example.vikar.publicbustracking.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Constant.refRute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    stationsList.clear();
                    RuteModel rute = ds.getValue(RuteModel.class);

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
                                        stationsList.add(new LatLng(model.getLatitude(), model.getLongitude()));
                                        stationMarker = new LatLng(model.getLatitude(), model.getLongitude());
                                        mMap.addMarker(new MarkerOptions().position(stationMarker).title(model.getName()).snippet("Station Id: "+model.getId_station()));
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

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(stationMarker));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
