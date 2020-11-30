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
        symptoms.add("Dandruff");
        symptoms.add("Decrease in appetite");
        symptoms.add("Decreased physical activity");
        symptoms.add("Dental disease");
        symptoms.add("Depression");
        symptoms.add("Deteriorated vision");
        symptoms.add("Diarrhea");
        symptoms.add("Difficulty breathing");
        symptoms.add("Difficulty defecating");
        symptoms.add("Difficulty eating or swallowing");
        symptoms.add("Difficulty hearing");
        symptoms.add("Difficulty urinating");
        symptoms.add("Disheveled coat");
        symptoms.add("Disorientation");
        symptoms.add("Drooling");
        symptoms.add("Dropped jaw");
        symptoms.add("Dry cough");
        symptoms.add("Ear discharge");
        symptoms.add("Enlarged lymph nodes");
        symptoms.add("Excessive itching");
        symptoms.add("Excessive licking");
        symptoms.add("Excessive panting");
        symptoms.add("Excessive urination");
        symptoms.add("Excessive winking");
        symptoms.add("Fever");
        symptoms.add("Flea dirt on skin (they look like tiny black dots)");
        symptoms.add("Frequent sneezing");
        symptoms.add("Gagging");
        symptoms.add("General discomfort");
        symptoms.add("Grating in the joint when your dog moves");
        symptoms.add("Hair loss");
        symptoms.add("Hives");
        symptoms.add("Inability to Jump");
        symptoms.add("Increase in appetite");
        symptoms.add("Increased vocalization");
        symptoms.add("Inflamed and irritated skin");
        symptoms.add("Inflammation around the abdominal area");
        symptoms.add("Inflammation of the gums");
        symptoms.add("Inflammation of the mouth");
        symptoms.add("Intolerance to exercise");
        symptoms.add("Joint pain");
        symptoms.add("Lackluster coat and skin");
        symptoms.add("Lameness");
        symptoms.add("Lethargy");
        symptoms.add("Licking around the urinary area (often because of pain)");
        symptoms.add("Licking at the knee");
        symptoms.add("Limited range of motion");
        symptoms.add("Looseness in the joint");
        symptoms.add("Loss of balance");
        symptoms.add("Loss of eyesight");
        symptoms.add("Loss of pigment in the nose");
        symptoms.add("Muscle pain");
        symptoms.add("Narrow stance");
        symptoms.add("Nasal and oral ulcers");
        symptoms.add("Nasal discharge");
        symptoms.add("Nausea");
        symptoms.add("Odor in the ear");
        symptoms.add("Open-mouth breathing");
        symptoms.add("Oral odor");
        symptoms.add("Pale or inflamed gums");
        symptoms.add("Paralysis");
        symptoms.add("Pica");
        symptoms.add("Rapid breathing");
        symptoms.add("Ravenous hunger");
        symptoms.add("Red eyes");
        symptoms.add("Red or pale gums");
        symptoms.add("Respiratory distress");
        symptoms.add("Restlessness");
        symptoms.add("Rubbing eyes");
        symptoms.add("Runny eyes");
        symptoms.add("Runny nose");
        symptoms.add("Salivating");
        symptoms.add("Scabs");
        symptoms.add("Segments, larvae or eggs of tapeworms in the feces");
        symptoms.add("Seizures");
        symptoms.add("Shivering");
        symptoms.add("Skin infections");
        symptoms.add("Skin lesions");
        symptoms.add("Skin redness");
        symptoms.add("Sneezing");
        symptoms.add("Sore muscles");
        symptoms.add("Sores on skin");
        symptoms.add("Squinting");
        symptoms.add("Stiffness");
        symptoms.add("Strong cough with a honking sound");
        symptoms.add("Swelling");
        symptoms.add("Swollen and painful eyes");
        symptoms.add("Ulcers on face");
        symptoms.add("Ulcers on feet");
        symptoms.add("Unexplained bleeding or discharge from body");
        symptoms.add("Unusually scared of water");
        symptoms.add("Urinating in unusual places");
        symptoms.add("Urinating outside of litter box");
        symptoms.add("Vision problems");
        symptoms.add("Vomiting");
        symptoms.add("Weakness");
        symptoms.add("Weakness in hind legs");
        symptoms.add("Weight loss");
        symptoms.add("Wet cough");
        symptoms.add("Wounds that don’t heal");
        symptoms.add("Worms in vomit");

    }
}