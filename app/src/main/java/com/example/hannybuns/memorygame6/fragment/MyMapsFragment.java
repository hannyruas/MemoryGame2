package com.example.hannybuns.memorygame6.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hannybuns.memorygame6.R;
import com.example.hannybuns.memorygame6.object.MyLocation;
import com.example.hannybuns.memorygame6.service.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapsFragment  extends Fragment implements OnMapReadyCallback {

    MyLocation myloca;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    DatabaseHelper myDB;

    public MyMapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_my_maps, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        //MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity().getApplicationContext());
        myloca = new MyLocation(this.getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myDB = new DatabaseHelper(getActivity());
        LatLng myLoc=null;
        //get from here all the locations in the db
        Cursor res = myDB.getALLData();
        if (res.getCount() == 0){
            showMessage("Error","Nothing found");
        } else {
            while (res.moveToNext()) {
                myLoc= new LatLng(res.getDouble(4),res.getDouble(5));
                googleMap.addMarker(new MarkerOptions().position(myLoc).title(res.getString(0)).snippet("Points: "+res.getString(2)));
            }
        }
        myloca.getCurrentLocation(this.getContext());
        LatLng myLocation= new LatLng(myloca.getCurrentLocation().getLatitude(),myloca.getCurrentLocation().getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLocation ).title("HOP").snippet("HOP"));

        CameraPosition Liberty = CameraPosition.builder().target(myLocation).zoom(16).bearing(0).tilt(45).build();
        //CameraPosition Liberty = CameraPosition.builder().target(new LatLng(myloca.getCurrentLocation().getLatitude(),myloca.getCurrentLocation().getLongitude()) ).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.689247,-74.0445502),16));

    }
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
