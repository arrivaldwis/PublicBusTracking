package com.example.vikar.publicbustracking.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 4/6/2018.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>{
    Context context;
    List<RuteModel> routeList;
    List<StationModel> stationList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_idrute;
        public TextView tv_origin;
        public TextView tv_destination;
        public TextView tv_rute_search;
        public TextView tv_past;
        public CardView cv_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_idrute = itemView.findViewById(R.id.tv_idrute);
            tv_origin = itemView.findViewById(R.id.tv_origin);
            tv_destination = itemView.findViewById(R.id.tv_destination);
            tv_rute_search = itemView.findViewById(R.id.tv_rute_search);
            tv_past = itemView.findViewById(R.id.tv_past);
            cv_view = itemView.findViewById(R.id.cv_view);
        }
    }

    public RouteAdapter(Context context, List<RuteModel> routeList, List<StationModel> stationList) {
        this.context = context;
        this.routeList = routeList;
        this.stationList = stationList;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route,parent,false);
        return new ViewHolder(v);
    }

    private ArrayList<StationModel> modelStation;
    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        final RuteModel post = routeList.get(position);

        holder.tv_idrute.setText(post.getId_rute()+"");

        final String[] station = post.getId_station().split(",");
        holder.tv_origin.setText(post.getOrigin().toUpperCase());
        holder.tv_destination.setText(post.getDestination().toUpperCase());

        int i = 0;
        for (String s:station) {
            for (StationModel m:
                    stationList) {
                if(s.equals(String.valueOf(m.getId_station()))) {
                    i++;
                }
            }
        }

        holder.tv_past.setText("Past "+i+" stations");
        holder.cv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail(post, station);
            }
        });
    }

    private StationsAdapter mAdapter;

    private void dialogDetail(RuteModel post, String[] station) {
        modelStation = new ArrayList<>();
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_detail_route);
        TextView tv_origin = dialog.findViewById(R.id.tv_origin);
        TextView tv_destination = dialog.findViewById(R.id.tv_destination);
        TextView tv_idrute = dialog.findViewById(R.id.tv_idrute);
        RecyclerView rv_stations = dialog.findViewById(R.id.rv_stations);

        for (String s:station) {
            for (StationModel m:
                    stationList) {
                if(s.equals(String.valueOf(m.getId_station()))) {
                    modelStation.add(m);
                }
            }
        }

        LinearLayoutManager llManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_stations.setLayoutManager(llManager);
        mAdapter = new StationsAdapter(context, modelStation);
        rv_stations.setAdapter(mAdapter);

        tv_idrute.setText(post.getId_rute()+"");
        tv_origin.setText(post.getOrigin());
        tv_destination.setText(post.getDestination());

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
