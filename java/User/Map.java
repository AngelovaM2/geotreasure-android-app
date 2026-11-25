package com.example.geotreasure.User;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.geotreasure.Database.Cache;
import com.example.geotreasure.Database.CacheDetection;
import com.example.geotreasure.Database.DatabaseHandler;
import com.example.geotreasure.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity {
    protected List<Cache> cacheListGeneral = new ArrayList<>();
    protected List<Cache> cacheListPicked = new ArrayList<>();
    protected List<Cache> cacheListMade = new ArrayList<>();
    protected CacheDetection checker;
    protected boolean has_changed = false;
    protected LatLng current_loc = new LatLng(0, 0);

    protected FirebaseUser current_user;

    protected BitmapDescriptor getMarkerIcon(int markerIcon) {
        //make drawable into bitmap
        Drawable drawable = getResources().getDrawable(markerIcon);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        //Make bitmap into bitmapDescriptor
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }
    //Marker placing functionality
    protected void placeMarkers(GoogleMap googleMap) {
        BitmapDescriptor placedIcon = getMarkerIcon(R.drawable.placed);
        BitmapDescriptor pickedIcon = getMarkerIcon(R.drawable.picked);
        for (Cache cache : cacheListGeneral) {
            for (Cache picked : cacheListPicked) {
                if (picked.getName().equals(cache.getName())) {
                    LatLng loc = cache.getLoc();
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title(cache.getName())
                            .icon(pickedIcon));
                } else {
                    LatLng loc = cache.getLoc();
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title(cache.getName())
                            .icon(placedIcon));
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseHandler a = new DatabaseHandler(getApplicationContext());

        //read a list of all available caches
        a.readCaches();
        cacheListGeneral = a.cacheListGeneral;

        //read a list of all picked caches for that user
        a.readCachesPicked(current_user.getUid());
        cacheListPicked = a.cacheListPicked;

        //read a list of a made caches for that user
        a.readCachesMade(current_user.getUid());
        cacheListMade = a.cacheListMade;


        checker = new CacheDetection();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Map.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
    }

}