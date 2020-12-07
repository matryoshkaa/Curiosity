package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Diagnosis extends AppCompatActivity {

    TextView finalDiagnosis;
    ImageButton back_button;
    ImageButton settings_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        finalDiagnosis = (TextView)findViewById(R.id.finalDiagnosis);
        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        String top = getIntent().getStringExtra("TOP");
        String sec =getIntent().getStringExtra("SEC");
        String third =getIntent().getStringExtra("THIRD");

        finalDiagnosis.setText("Most probable disease: "+top+"\n"+"It could also be: "+sec+" or "+third);






        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Diagnosis.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Diagnosis.this,SymptomLogger.class);
                startActivity(intent);
            }
        });


    }
}
