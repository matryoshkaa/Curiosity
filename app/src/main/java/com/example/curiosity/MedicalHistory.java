package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MedicalHistory extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;

    TextView petName;
    TextView birthDate;
    TextView breed;
    TextView gender;
    TextView weight;
    TextView owner;
    TextView allergies;
    TextView conditions;
    TextView vet;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    String userId,name;
    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    Query query;
    Map<String, Object> map;

    String username;
    String user;
    String pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        petName=(TextView) findViewById(R.id.petName);
        birthDate=(TextView) findViewById(R.id.birthDate);
        breed=(TextView) findViewById(R.id.breed);
        gender=(TextView) findViewById(R.id.gender);
        weight=(TextView) findViewById(R.id.weight);
        owner=(TextView) findViewById(R.id.owner);
        allergies=(TextView) findViewById(R.id.allergies);
        conditions=(TextView) findViewById(R.id.conditions);
        vet=(TextView) findViewById(R.id.vet);

        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MedicalHistory.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MedicalHistory.this,HealthWeight.class);
                startActivity(intent);
            }
        });

        user=getIntent().getStringExtra("USERID");
        pet=getIntent().getStringExtra("REF");

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        if (pet != null && pet != "") {

            db.collection("Users")
                    .document(user)
                    .collection("Pets")
                    .document(pet)
                    .collection("Heath Records")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            map = document.getData();
                            String petName=map.get("PetName").toString();
                            String birthDate=map.get("BirthDate").toString();
                            String breed=map.get("Breed").toString();
                            String gender=map.get("Gender").toString();
                            String weight=map.get("Weight").toString();
                            String owner=map.get("Owner").toString();
                            String allergies=map.get("Allergies").toString();
                            String conditions=map.get("Disorders").toString();
                            String vet=map.get("Vet").toString();
                            displayInformation(petName,birthDate,breed,gender,weight,owner,allergies,conditions,vet);
                        }
                    } else {
                        System.out.println("Error getting documents: ");
                    }
                }
            });
        }


    }

    public void displayInformation(String name,String dob,String petBreed,String petGender,String petWeight,String petOwner,String petAllergies,String disorders,String petVet){
        petName.setText(name);
        birthDate.setText(dob);
        breed.setText(petBreed);
        gender.setText(petGender);
        weight.setText(petWeight);
        owner.setText(petOwner);
        allergies.setText(petAllergies);
        conditions.setText(disorders);
        vet.setText(petVet);

    }
}