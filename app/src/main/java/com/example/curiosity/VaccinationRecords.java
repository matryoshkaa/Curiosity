package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class VaccinationRecords extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    Button pastVaccines;
    Button upcomingVaccines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_records);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        pastVaccines=(Button)findViewById(R.id.pastVaccines);
        upcomingVaccines=(Button)findViewById(R.id.upcomingVaccines);

        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(VaccinationRecords.this,Settings.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(VaccinationRecords.this,HealthRecords.class);
                startActivity(intent);
            }
        });

        pastVaccines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(VaccinationRecords.this,PastVaccination.class);
                startActivity(intent);
            }
        });

        upcomingVaccines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(VaccinationRecords.this,UpcomingVaccinations.class);
                startActivity(intent);
            }
        });
    }
}