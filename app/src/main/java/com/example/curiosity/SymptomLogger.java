package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SymptomLogger extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    Button searchDisease;
    ListView symptomListView;
    TextView diagnosis;
    ArrayAdapter adapter;
    HashMap<String, String[]> canineDiseases = new HashMap<String, String[]>();
    HashMap<String, String[]> felineDiseases = new HashMap<String, String[]>();
    private static final String TAG = "SymptomLogger";


    FirebaseAuth fAuth; //to get user id------------------------
    FirebaseFirestore fStore; //for data retrieval--------------
    String userid;
    String petId;
    String petType;
    private FirebaseFirestore db;

    private FirebaseStorage storage;
    private StorageReference storageReference;



    List symptoms = new ArrayList();

    String [] canineCancer = {"abnormal lumps or masses", "weight loss", "abnormal odor", "Decrease in appetite", "Intolerance to exercise", "Difficulty breathing", "abnormal thirst", "lethargy"};

    String [] canineDiabetes = {"Abnormal thirst", "weight loss", "Excessive urination", "Increase in appetite", "Cloudy eyes", "Deteriorated vision", "Lackluster coat and skint"};

    String[] CPV = {"intolerance to exercise", "Bloody diarrhea", "fever", "weight loss", "general discomfort", "Vomiting", "lethargy", "blood in vomit"};

    String[] canineHeartWorm = {"intolerance to exercise", "weight loss", "cough", "Decrease in appetite", "Abnormal fatigue"};

    String [] kennelCough = {"Strong cough with a honking sound", "Frequent sneezing", "Runny nose", "decrease in appetite", "Lethargy", "abnormal fatigue" ,"fever"};

    String [] canineTapeworm = {"Segments, larvae or eggs of tapeworms in the feces", "Diarrhea", "Nausea", "Abdominal pain and discomfort", "Inflammation around the abdominal area", "Weight loss", "Lethargy", "Weakness", "vomiting", "worms in vomit"};

    String [] canineRabies = {"unusually scared of water", "Aggressiveness", "Dropped jaw", "Seizures", "Paralysis", "Pica", "Fever"};

    String [] canineEarInfection = {"Odor in the ear", "Excessive itching", "ear discharge", "hair loss", "Loss of balance", "Abnormal eye movements", "Difficulty hearing"};

    String [] canineSkinAllergies = {"Excessive itching", "Swelling", "Inflamed and irritated skin", "Vomiting", "Sneezing", "Diarrhea", "Altered behavior", "Hives"};

    String [] canineDistemper = {"Swollen and painful eyes", "Loss of eyesight", "Dry cough", "Wet cough", "Vomiting", "Seizures", "Fever"};

    String [] canineInfluenza = {"cough", "Difficulty breathing", "Nasal discharge", "Fever", "Sneezing", "Runny eyes"};

    String [] canineParasites = {"Excessive itching", "Hair loss","Inflamed and irritated skin", "Scabs"};

    String [] canineHeatstroke = {"Excessive panting", "Salivating", "Restlessness", "Accelerated heart rate", "Vomiting", "Diarrhea", "Red or pale gums", "blood in vomit", "bloody diarrhea"};

    String [] canineLeptospirosis = {"Sore muscles", "Intolerance to exercise", "Fever", "Lethargy", "Decrease in appetite", "abnormal thirst", "excessive urination", "Shivering", "stiffness", "weakness"};

    String [] canineHipDysplasia = {"Limited range of motion", "Decreased physical activity", "Lameness", "Narrow stance", "Grating in the joint when your dog moves", "Looseness in the joint", "lethargy"};

    String [] canineAutoimmune = {"Lameness", "Joint pain", "Muscle pain", "Ulcers on feet", "Loss of pigment in the nose", "abnormal thirst", "excessive urination", "Fever", "Ulcers on face"};

    String [] canineLuxatingPatella = {"Licking at the knee", "Crying", "Lameness", "Abnormal walking", "Bow-legged appearance"};


    String [] felineUTD = {"Abnormal thirst", "Excessive urination", "Bloody urine", "Urinating in unusual places", "Crying when urinating", "Licking around the urinary area (often because of pain)", "Depression", "decrease in appetite", "Vomiting"};

    String [] felineFleas = {"Flea dirt on its skin (they look like tiny black dots)", "Excessive itching", "Excessive licking", "inflamed or irritated skin", "Hair loss", "Skin infections"};

    String [] felineTapeworm = {"weight loss", "Segments, larvae or eggs of tapeworms in the feces", "Vomiting", "Diarrhea", "decrease in appetite"};

    String [] felineEyeDiseases = {"An inflamed third eyelid that is covering a part of the infected eye", "respiratory distress", "Red eyes", "Excessive winking", "Rubbing eyes", "Clear, green or yellow discharge coming from the eyes", "sneezing", "nasal discharge"};

    String [] felineCancer = {"Abnormal lumps or masses", "Wounds that don’t heal", "Change in bowel or bladder habits", "Difficulty eating or swallowing", "Difficulty urinating", "Unexplained bleeding or discharge from body", "Loss of appetite", "weight loss", "difficulty breathing", "Stiffness", "Oral odor", "Ravenous hunger", "coughing", "Difficulty defecating"};

    String [] felineDiabetes = {"Excessive Urination", "weight loss", "Inability to Jump", "Decrease in appetite", "Vomiting", "Lethargy", "weakness in hind legs ", "Abnormal thirst"};

    String [] FIV = {"Enlarged lymph nodes", "Fever", "Weight loss", "Disheveled coat", "Decrease in appetite", "Diarrhea", "Abnormal appearance or inflammation of the eye", "Inflammation of the gums", "Inflammation of the mouth", "Dental disease", "Skin redness", "Wounds that don’t heal", "Sneezing", "Clear, green or yellow discharge coming from the eyes", "urinating outside of litter box", "Altered behavior", "hair loss", "Excessive urination", "Difficulty urinating", "nasal discharge"};

    String [] FeIV = {"weight loss", "Pale or inflamed gums", "Disheveled coat", "Abscesses", "Fever", "Lackluster coat and skin", "Diarrhea", "Seizures", "Altered behavior", "Vision problems", "Enlarged lymph nodes", "vomiting", "Respiratory distress", "Lethargy", "decrease in appetite"};

    String [] felineHeartworm = {"coughing", "Difficulty breathing", "Vomiting", "Lethargy", "Anorexia", "Weight loss", "vomiting", "respiratory distress"};

    String [] felineRabies = {"altered behaviour", "Increased vocalization", "Decrease in appetite", "Weakness", "Disorientation", "Paralysis", "Seizures", "restlessness", "lethargy"};

    String [] felineRingworm = {"Skin lesions", "hair loss", "dandruff", "Skin redness"};

    String [] felineUpperRespiratoryInfection = {"Sneezing", "Congestion", "Runny nose", "Cough", "nasal discharge", "Gagging", "Fever", "decreased appetite", "Rapid breathing", "Nasal and oral ulcers", "Squinting", "Open-mouth breathing", "Depression", "Skin redness", "rubbing eyes"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_logger);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        searchDisease = (Button)findViewById(R.id.searchDisease);
        symptomListView = (ListView) findViewById(R.id.symptomListView);
        diagnosis = (TextView)findViewById(R.id.diagnosis);

        addSymptoms();


        symptomListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        adapter = new ArrayAdapter(SymptomLogger.this, android.R.layout.simple_list_item_checked, symptoms);

        symptomListView.setAdapter(adapter);


        canineDiseases.put("Cancer", canineCancer);
        canineDiseases.put("Diabetes", canineDiabetes);
        canineDiseases.put("CPV", CPV);
        canineDiseases.put("HeartWorm", canineHeartWorm);
        canineDiseases.put("KennelCough", kennelCough);
        canineDiseases.put("Tapeworm", canineTapeworm);
        canineDiseases.put("Rabies", canineRabies);
        canineDiseases.put("EarInfections", canineEarInfection);
        canineDiseases.put("SkinAllergies", canineSkinAllergies);
        canineDiseases.put("CanineDistemper", canineDistemper);
        canineDiseases.put("Influenza", canineInfluenza);
        canineDiseases.put("Parasites", canineParasites);
        canineDiseases.put("Heatstroke", canineHeatstroke);
        canineDiseases.put("Leptospirosis", canineLeptospirosis);
        canineDiseases.put("HipDysplasia", canineHipDysplasia);
        canineDiseases.put("AutoimmuneIssues", canineAutoimmune);
        canineDiseases.put("LuxatingPatella", canineLuxatingPatella);

        felineDiseases.put("UTD", felineUTD);
        felineDiseases.put("Fleas", felineFleas);
        felineDiseases.put("Tapeworms", felineTapeworm);
        felineDiseases.put("EyeDiseases", felineEyeDiseases);
        felineDiseases.put("Cancer", felineCancer);
        felineDiseases.put("Diabetes", felineDiabetes);
        felineDiseases.put("FIV", FIV);
        felineDiseases.put("FeIV", FeIV);
        felineDiseases.put("Rabies", felineRabies);
        felineDiseases.put("Ringworm", felineRingworm);
        felineDiseases.put("UpperRespiratoryInfections", felineUpperRespiratoryInfection);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();



//        DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets")
//                .document(petId);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                petType = (documentSnapshot.getString("Pet Type"));
//                System.out.println(petType);
//            }
//        });


        //on press search button
        searchDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList userSym = new ArrayList();
                int len = symptomListView.getCount();
                SparseBooleanArray sp=symptomListView.getCheckedItemPositions();
                for(int i=0;i<len;i++)
                {
                    if(sp.get(i)){
                        String sym = symptoms.get(i).toString();
                        userSym.add(sym);

                    }
                }

//                for(int i =0; i<userSym.size(); i++){
//                    System.out.println(userSym.get(i));
//                }
            }
        });


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

        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();



    }


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