package com.example.hannybuns.memorygame6.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.fragment.MyListFragment;
import com.example.hannybuns.memorygame6.fragment.MyMapsFragment;

public class RecordsActivity extends AppCompatActivity {

    String fragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        Intent i = getIntent();
        fragmentType =  getIntent().getStringExtra("fragment_type");
        
        
        if(fragmentType.equals("LIST")) {
            MyListFragment listFragment = new MyListFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, listFragment, listFragment.getTag()).commit();
        }

        if(fragmentType.equals("MAP")) {
            MyMapsFragment mapFragment = new MyMapsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment, mapFragment, mapFragment.getTag()).commit();

        }

    }


}
