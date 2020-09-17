package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Dashboard extends AppCompatActivity {

    ImageButton settings_button;
    ImageButton scan_button;
    ImageButton profile_button;
    ImageButton health_button;
    ImageButton track_button;
    ImageButton pet_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        settings_button=(ImageButton)findViewById(R.id.settings_button);
        profile_button=(ImageButton)findViewById(R.id.profile_button);
        health_button=(ImageButton)findViewById(R.id.health_button);
        track_button=(ImageButton)findViewById(R.id.track_button);
        pet_button=(ImageButton)findViewById(R.id.pet_button);
        scan_button=(ImageButton)findViewById(R.id.scan_button);


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,Settings.class);
                startActivity(intent);
            }
        });

        health_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,HealthRecords.class);
                startActivity(intent);
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,UserProfile.class);
                startActivity(intent);
            }
        });

        track_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,TrackPet.class);
                startActivity(intent);
            }
        });

        pet_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,Pet.class);
                startActivity(intent);
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,ScanPet.class);
                startActivity(intent);
            }
        });
    }
}