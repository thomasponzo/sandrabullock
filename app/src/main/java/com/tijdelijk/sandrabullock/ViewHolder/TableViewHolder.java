package com.tijdelijk.sandrabullock.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tijdelijk.sandrabullock.R;

public class TableViewHolder extends RecyclerView.ViewHolder {

    public TextView movie;
    public TextView year;
    public TextView role;


    public TableViewHolder(View itemView) {
        super(itemView);

        movie = itemView.findViewById(R.id.moviename);
        year =  itemView.findViewById(R.id.year);
        role = itemView.findViewById(R.id.role);
    }
}
