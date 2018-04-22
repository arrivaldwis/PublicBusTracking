package com.example.vikar.publicbustracking.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.adapter.RouteAdapter;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;
import com.example.vikar.publicbustracking.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Route extends Fragment {

    private EditText svCari;
    private RecyclerView rvData;
    private ArrayList<RuteModel> ruteList;
    private ArrayList<StationModel> stationList;
    private RouteAdapter mAdapter;

    public Route() {
        // Required empty public constructor
    }

    public static Route newInstance() {
        Route fragment = new Route();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_route, container, false);
        stationList = new ArrayList<>();
        ruteList = new ArrayList<>();

        svCari = (EditText) v.findViewById(R.id.svCari);
        rvData = (RecyclerView) v.findViewById(R.id.rvData);

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(llManager);
        mAdapter = new RouteAdapter(getActivity(), ruteList, stationList);
        rvData.setAdapter(mAdapter);
        loadStation();
        loadRoute("");

        svCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadRoute(svCari.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    private void loadStation() {
        Constant.refStation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StationModel rute = ds.getValue(StationModel.class);
                    stationList.add(rute);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadRoute(final String filter) {
        Constant.refRute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ruteList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RuteModel rute = ds.getValue(RuteModel.class);
                    if(rute.getDestination().contains(filter)) {
                        ruteList.add(rute);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
