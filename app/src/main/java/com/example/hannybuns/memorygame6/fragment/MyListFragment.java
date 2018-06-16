package com.example.hannybuns.memorygame6.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.ListFragment;


import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.adapter.PlayersListAdapter;
import com.example.hannybuns.memorygame6.service.DatabaseHelper;


import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends ListFragment {
    List<String> players;
    DatabaseHelper mydb;

    public MyListFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players=new ArrayList<String>();
        mydb = new DatabaseHelper(getActivity());
        players = getData();
        PlayersListAdapter adapter = new PlayersListAdapter(getActivity(), R.layout.player_listitem, players);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        return view;
    }


    public List<String> getData() {
        Cursor data = mydb.getRecords();
        if(data.getCount()==0)
            return null;
        ArrayList<String> playersName=new ArrayList<>();
        while (data.moveToNext())
            playersName.add(data.getString(0)+") "+data.getString(1)+", birthday: " + data.getString(2)+ "points:" +data.getString(3));
        return playersName;
    }
}


