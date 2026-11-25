package com.example.geotreasure.Placer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geotreasure.Database.DatabaseHandler;
import com.example.geotreasure.R;

import java.util.Random;

public class Cache_Configuration extends AppCompatActivity {
    //Cache details configuration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Double lat = intent.getDoubleExtra("Lat", 0);
        Double lng = intent.getDoubleExtra("Lng", 0);

        System.out.println("Lat:" + lat + "Lng:" + lng);
        setContentView(R.layout.activity_cache_configuration);
        EditText cachename = findViewById(R.id.cache_name);
        Spinner puzzletype = findViewById(R.id.puzzle_type);
        TextView puzzleView = findViewById(R.id.puzzle_view);
        TextView currentLoc = findViewById(R.id.kur_loc);
        Button location = findViewById(R.id.locationbtn);
        EditText points = findViewById(R.id.cache_points);
        EditText description = findViewById(R.id.cache_description);
        Button post = findViewById(R.id.postbtn);
        ImageButton back = findViewById(R.id.goBack);
        String loc = "Current";

        back.setOnClickListener(view -> finish());

        String[] typePuzzle = {"          Math Quiz", "          Trivia Quiz"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Cache_Configuration.this, R.layout.item_file, typePuzzle);
        adapter.setDropDownViewResource(R.layout.item_file);
        puzzletype.setAdapter(adapter);

        //Puzzle selection

        puzzletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String value = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(Cache_Configuration.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Random a = new Random();
        DatabaseHandler base = new DatabaseHandler(getApplicationContext());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLoc.setText(loc);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int type = 0;
                switch (puzzletype.getSelectedItem().toString()) {
                    case "          Math Quiz":
                        type = 1;
                        break;
                    case "          Trivia Quiz":
                        type = 2;
                        break;
                    default:
                        type = 0;
                        break;
                }
                base.uploadCache(String.valueOf(a.nextInt(1000000)),
                        lat,
                        lng,
                        cachename.getText().toString(),
                        description.getText().toString(),
                        type,
                        Integer.parseInt(points.getText().toString()));
            }
        });
    }
}