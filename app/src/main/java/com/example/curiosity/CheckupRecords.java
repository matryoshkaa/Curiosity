package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class CheckupRecords extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    Button pastCheckups;
    Button upcomingCheckups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkup_records);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        pastCheckups=(Button)findViewById(R.id.pastCheckups);
        upcomingCheckups=(Button)findViewById(R.id.upcomingCheckups);

        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(CheckupRecords.this,Settings.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(CheckupRecords.this,HealthRecords.class);
                startActivity(intent);
            }
        });


        pastCheckups.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(CheckupRecords.this,PastCheckupRecords.class);
                startActivity(intent);
            }
        });

        upcomingCheckups.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(CheckupRecords.this,UpcomingCheckupRecords.class);
                startActivity(intent);
            }
        });
    }
}