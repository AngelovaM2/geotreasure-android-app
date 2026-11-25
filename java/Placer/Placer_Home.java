package com.example.geotreasure.Placer;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.geotreasure.R;
import com.example.geotreasure.SidePage.Side_Page;
import com.example.geotreasure.User.Map;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Placer_Home extends Map implements OnMapReadyCallback {
    private FloatingActionButton addcache, mHamburgerIcon;
    private LatLng current_loc = new LatLng(0, 0);
    //Map update
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placer_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * An activity that displays a Google map with a marker (pin) to indicate a particular location.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        addcache = findViewById(R.id.plusFloatingButton);
        mHamburgerIcon = findViewById(R.id.profileFloatingButton);

        addcache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Placer_Home.this, Cache_Configuration.class);
                intent.putExtra("Lat", current_loc.latitude);
                intent.putExtra("Lng", current_loc.longitude);
                startActivity(intent);
            }
        });

        mHamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Placer_Home.this, Side_Page.class);
                startActivity(intent);
            }
        });


        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {
                placeMarkers(googleMap);
                current_loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (!has_changed &&
                        current_loc.longitude != 0 &&
                        current_loc.longitude != 0) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(current_loc));
                    has_changed = true;
                }
            }
        });

    }
}