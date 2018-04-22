package com.example.vikar.publicbustracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;

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
        public TextView tv_rute;
        public TextView tv_rute_search;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_idrute = itemView.findViewById(R.id.tv_idrute);
            tv_rute = itemView.findViewById(R.id.tv_rute);
            tv_rute_search = itemView.findViewById(R.id.tv_rute_search);
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

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        final RuteModel post = routeList.get(position);
        holder.tv_idrute.setText(post.getId_rute()+"");

        String[] station = post.getId_station().split(",");
        holder.tv_rute.setText(post.getOrigin().toUpperCase()+" - "+post.getDestination().toUpperCase());

        String stat = "";
        for (String s:station) {
            for (StationModel m:
                 stationList) {
                if(s.equals(String.valueOf(m.getId_station()))) {
                    stat += m.getName().toUpperCase()+" -> ";
                }
            }
        }

        holder.tv_rute_search.setText(stat.substring(0, stat.length()-3));
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
