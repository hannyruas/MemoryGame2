package com.example.hannybuns.memorygame6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.object.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HannyBuns on 4/16/2018.
 */

public class CardListAdapterWithCache extends ArrayAdapter<Card> {
    private Context mContext;
    ArrayList<Card> mylist;

    public CardListAdapterWithCache(Context context,  ArrayList<Card> list) {
        super(context, R.layout.card_layout, list);
        mContext = context;
        this.mylist = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card Card = mylist.get(position);
        CardViewHolder holder;

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.card_layout, parent, false);

            holder = new CardViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        }
        else{
            holder = (CardViewHolder) convertView.getTag();
        }

        holder.populate(Card);

        return convertView;
    }
    public ArrayList<ImageView> getAllImg(){
        return (ArrayList<ImageView>) CardViewHolder.all_img;
    }

    public Context getContext() {
        return mContext;
    }

    static class CardViewHolder {
        public ImageView img;
        public static List<ImageView> all_img=new ArrayList<>();

        void populate(Card p) {
            img.setImageResource(p.getIv_back());
            all_img.add(img);
        }
    }

}
