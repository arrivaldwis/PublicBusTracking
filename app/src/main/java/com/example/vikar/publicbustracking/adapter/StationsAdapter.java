package com.example.vikar.publicbustracking.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.RuteModel;
import com.example.vikar.publicbustracking.model.StationModel;

import java.util.List;

/**
 * Created by Leonardo on 4/6/2018.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.ViewHolder>{
    Context context;
    List<StationModel> stationList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_origin;
        public ImageView img_point;
        public View line;

        public ViewHolder(View itemView) {
            super(itemView);
            img_point = itemView.findViewById(R.id.img_point);
            tv_origin = itemView.findViewById(R.id.tv_origin);
            line = itemView.findViewById(R.id.line);
        }
    }

    public StationsAdapter(Context context, List<StationModel> stationList) {
        this.context = context;
        this.stationList = stationList;
    }

    @Override
    public StationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StationsAdapter.ViewHolder holder, int position) {
        final StationModel post = stationList.get(position);
        holder.tv_origin.setText(post.getName());

        if(position == getItemCount()-1) {
            holder.img_point.setImageDrawable(context.getResources().getDrawable(R.drawable.dest));
            holder.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }
}
