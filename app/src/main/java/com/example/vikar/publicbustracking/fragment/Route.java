package com.example.vikar.publicbustracking.fragment;


import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.adapter.RouteAdapter;
import com.example.vikar.publicbustracking.model.BusModel;
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

    private AutoCompleteTextView svOrigin;
    private AutoCompleteTextView svDestination;
    private ImageView btnSearch;
    private RecyclerView rvData;
    private ArrayList<RuteModel> ruteList;
    private ArrayList<StationModel> stationList;
    private ArrayList<String> stationListString;
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
        stationListString = new ArrayList<>();
        ruteList = new ArrayList<>();

        svOrigin = (AutoCompleteTextView) v.findViewById(R.id.svOrigin);
        svDestination = (AutoCompleteTextView) v.findViewById(R.id.svDestination);
        rvData = (RecyclerView) v.findViewById(R.id.rvData);
        btnSearch = (ImageView) v.findViewById(R.id.btnSearch);

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(llManager);
        mAdapter = new RouteAdapter(getActivity(), ruteList, stationList);
        rvData.setAdapter(mAdapter);
        loadStation();
        loadRoute("", "");

        svOrigin.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stationListString));
        svDestination.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stationListString));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!svDestination.getText().toString().isEmpty() &&
                        !svOrigin.getText().toString().isEmpty()) {
                    loadRoute(svOrigin.getText().toString(), svDestination.getText().toString());
                } else {
                    loadRoute("","");
                }
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
                    stationListString.add(rute.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadRoute(final String origin, final String destination) {
        Constant.refRute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ruteList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final RuteModel rute = ds.getValue(RuteModel.class);
                    if(rute.getOrigin().contains(origin) && rute.getDestination().contains(destination)) {
                        ruteList.add(rute);
                        mAdapter.notifyDataSetChanged();
                    }

                    final String[] stations = rute.getId_station().split(",");
                    Constant.refStation.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds:dataSnapshot.getChildren()) {
                                StationModel model = ds.getValue(StationModel.class);

                                for (String s:stations) {
                                    Log.d("stasiunStatis", s);
                                    if (model.getId_station() == Integer.parseInt(s)) {
                                        Log.d("stasiunEquals", model.getName());
                                        if(rute.getOrigin().equals(origin)) {
                                            ruteList.add(rute);
                                            mAdapter.notifyDataSetChanged();
                                            return;
                                        }else if(rute.getDestination().equals(destination)) {
                                            ruteList.add(rute);
                                            mAdapter.notifyDataSetChanged();
                                            return;
                                        }
                                    }
                                }
                            }
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
