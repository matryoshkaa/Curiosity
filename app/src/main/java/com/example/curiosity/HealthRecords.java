package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HealthRecords extends AppCompatActivity {

    ImageButton back_button;       //will activate this after the pages for them are made
    ImageButton settings_button;

    ImageButton vaccinationRec_button;
    ImageButton checkup_button;
    ImageButton healthWeight_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);

        vaccinationRec_button=(ImageButton)findViewById(R.id.vacc_rec_button);
        checkup_button=(ImageButton)findViewById(R.id.checkup_button);
        healthWeight_button=(ImageButton)findViewById(R.id.health_weight_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        back_button=(ImageButton)findViewById(R.id.back_button);

        vaccinationRec_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(HealthRecords.this,VaccinationRecords.class);
                startActivity(intent);
            }
        });

        checkup_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(HealthRecords.this,CheckupRecords.class);
                startActivity(intent);
            }
        });

        healthWeight_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(HealthRecords.this,HealthWeight.class);
                startActivity(intent);
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(HealthRecords.this,Settings.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(HealthRecords.this,Dashboard.class);
                startActivity(intent);
            }
        });

    }
}