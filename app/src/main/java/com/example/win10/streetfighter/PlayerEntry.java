package com.example.win10.streetfighter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;




public class PlayerEntry extends ArrayAdapter<Player> {


    private Context context;
    private int resource;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public PlayerEntry(Context context, int resource, ArrayList<Player> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the player information
        String name = getItem(position).getplayerName();
        Double score = getItem(position).getScore();



        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView tvName = (TextView) convertView.findViewById(R.id.listPlayeName);
        TextView tvScore = (TextView) convertView.findViewById(R.id.listPlayerScore);

        tvName.setText(name);
        tvScore.setText(score.toString());

        return convertView;
    }
}


