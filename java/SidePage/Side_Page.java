package com.example.geotreasure.SidePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.geotreasure.Connectives;
import com.example.geotreasure.Placer.Placer_Home;
import com.example.geotreasure.Player.Player_Home;
import com.example.geotreasure.R;

public class Side_Page extends AppCompatActivity {

    TextView mEditProfile, mCacheHistory, mLeaderboard, mNearestCaches;
    ImageButton mBack;

    AppCompatButton mSwitch;
    //SidePage functionality
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_page);

        mEditProfile = findViewById(R.id.edit_profile);
        mCacheHistory = findViewById(R.id.cache_history);
        mLeaderboard = findViewById(R.id.leaderboard);
        mNearestCaches = findViewById(R.id.nearest_caches);
        mBack = findViewById(R.id.go_back_side);

        mBack.setOnClickListener(view -> finish());

        mSwitch = findViewById(R.id.switch_mode);

        //Mode Switching button
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getApplicationContext().equals(Placer_Home.class)) {
                    System.out.println("Currently in placer");
                    Intent intent = new Intent(getApplicationContext(), Player_Home.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Placer_Home.class);
                    startActivity(intent);
                }
            }
        });
        //SidePage buttons for user functionality
        mCacheHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cache_History.class);
                startActivity(intent);
            }
        });
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
        mLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Leaderboard.class);
                startActivity(intent);
            }
        });
        mNearestCaches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Nearest_Caches.class);
                startActivity(intent);
            }
        });
        //Internet connectivity
        Connectives connectionDetector = new Connectives(this);
        if (!connectionDetector.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet connection");
            builder.setMessage("Please check your internet connection and try again.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
}
