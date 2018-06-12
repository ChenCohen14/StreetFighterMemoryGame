package com.example.win10.streetfighter;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class ScoreListFragment extends Fragment {

    DBAssistant mDBAssistant;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.score_list_fragment,container,false);

        ListView listView = view.findViewById(R.id.listScores);
        TextView textView = new TextView(view.getContext());
        textView.setText(R.string.top_ten);

        listView.addHeaderView(textView);
        listView.setBackgroundColor(Color.parseColor("#ffffff"));

        mDBAssistant = new DBAssistant(view.getContext());

        Cursor data = mDBAssistant.getTop10();
        ArrayList<Player> listDataPlayers = new ArrayList<>();
        while(data.moveToNext()){

            listDataPlayers.add(new Player(data.getString(1),data.getDouble(2)));
        }

        PlayerEntry adapter = new PlayerEntry(view.getContext(),R.layout.adapter_view_layout,listDataPlayers);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
