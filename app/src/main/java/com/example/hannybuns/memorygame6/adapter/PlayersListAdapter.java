package com.example.hannybuns.memorygame6.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hannybuns.memorygame6.R;


public class PlayersListAdapter extends ArrayAdapter<String>{

    private Context context;
    private List<String> items;

    public PlayersListAdapter(Context context, int resource, List<String> players) {
        super(context,resource, players);
        this.context = context;
        this.items = players;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        String player = items.get(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.player_listitem, null);

        TextView name = (TextView)view.findViewById(R.id.player);

        name.setText(player);

        return view;
    }
}