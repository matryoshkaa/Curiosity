package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SymptomLogger extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    ListView symptomListView;

    List symptoms = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_logger);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        symptomListView = (ListView) findViewById(R.id.symptomListView);



        try{
            addSymptoms();


        }
        catch (IOException e) {
            System.out.println("Aaaaaaaaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }
        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(SymptomLogger.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(SymptomLogger.this,HealthWeight.class);
                startActivity(intent);
            }
        });

    }

    void addSymptoms() throws IOException {
        BufferedReader bfReader = new BufferedReader(new FileReader("src\\main\\assets\\symptoms.txt"));
        String line = bfReader.readLine();
        while(line!=null){
            symptoms.add(line);
            line = bfReader.readLine();
        }
        bfReader.close();

        for(int i=0; i<symptoms.size(); i++){
            System.out.println(symptoms.get(i));
        }
    }
}