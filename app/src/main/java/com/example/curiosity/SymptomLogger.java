package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SymptomLogger extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    ListView symptomListView;

    private static final String TAG = "SymptomLogger";


    List symptoms = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_logger);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        symptomListView = (ListView) findViewById(R.id.symptomListView);

        addSymptoms();

//        try{
//            addSymptoms();
//
//
//        }
//        catch (IOException e) {
//            System.out.println("Aaaaaaaaaaaaaaaaaaaaaaaa");
//            e.printStackTrace();
//        }



        symptomListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter(SymptomLogger.this, android.R.layout.simple_list_item_checked, symptoms);

        symptomListView.setAdapter(adapter);




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



//    void addSymptoms() throws IOException {
//        Scanner s = new Scanner (new File("D:\\321\\symptoms"));
//        //System.out.println(line);
//        while(s.hasNext()){
//            symptoms.add(s.next());
//
//        }
//        s.close();
//
//        for(int i=0; i<symptoms.size(); i++){
//            System.out.println(symptoms.get(i));
//        }
//    }

    void addSymptoms(){
        symptoms.add("Abscesses");
        symptoms.add("Abdominal pain and discomfort");
        symptoms.add("Abnormal lumps or masses");
        symptoms.add("Abnormal appearance or inflammation of the eye");
        symptoms.add("Abnormal eye movements");
        symptoms.add("Abnormal fatigue");
        symptoms.add("Abnormal odor");
        symptoms.add("Abnormal thirst");
        symptoms.add("Abnormal walking");
        symptoms.add("Accelerated heart rate");
        symptoms.add("Aggressive scratching");
        symptoms.add("Aggressiveness");
        symptoms.add("Altered behavior");
        symptoms.add("An inflamed third eyelid that is covering a part of the infected eye");
        symptoms.add("Anorexia");
        symptoms.add("Blood in vomit");
        symptoms.add("Bloody diarrhea");
        symptoms.add("Bloody urine");
        symptoms.add("Bow-legged appearance");
        symptoms.add("Change in bowel or bladder habits");
        symptoms.add("Clear, green or yellow discharge coming from the eyes");
        symptoms.add("Cloudy eyes");
        symptoms.add("Congestion");
        symptoms.add("Constant scratching");
        symptoms.add("Cough");
        symptoms.add("Crying");
        symptoms.add("Crying when urinating");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
        symptoms.add("test");
    }
}