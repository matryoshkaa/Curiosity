package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UpcomingVaccinations extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_vaccinations);


        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(UpcomingVaccinations.this,Settings.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(UpcomingVaccinations.this,VaccinationRecords.class);
                startActivity(intent);
            }
        });
    }
}