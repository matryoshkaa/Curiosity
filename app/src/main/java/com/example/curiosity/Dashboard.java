package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton settings_button;
    ImageButton scan_button;
    ImageButton profile_button;
    ImageButton health_button;
    ImageButton track_button;
    ImageButton pet_button;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    DocumentReference docRef;
    String userId, name;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    String petOne;
    String petTwo;
    String petThree;
    String petFour;
    String petFive;
    String petSix;
    String[]petArray;

    String pet="";
    String username;
    String user;

    String trackerId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        petArray= new String[6];

        settings_button=(ImageButton)findViewById(R.id.settings_button);
        profile_button=(ImageButton)findViewById(R.id.profile_button);
        health_button=(ImageButton)findViewById(R.id.health_button);
        track_button=(ImageButton)findViewById(R.id.track_button);
        pet_button=(ImageButton)findViewById(R.id.pet_button);
        scan_button=(ImageButton)findViewById(R.id.scan_button);


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,Settings.class);
                startActivity(intent);
            }
        });

        health_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,HealthRecords.class);
                startActivity(intent);
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,UserProfile.class);
                startActivity(intent);
            }
        });

        pet_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,Pet.class);
                startActivity(intent);
            }
        });

        scan_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,ScanPet.class);
                startActivity(intent);
            }
        });


        db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            documentReference = db.collection("Users").document(userId);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Dashboard.this, Login.class));
                }
            }
        };


        // google user retrieval
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            user = acct.getDisplayName();
        }

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("User Name");
                } else {
                    name = user;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAILURE " + e.getMessage());
            }
        });


        db.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                    pet = documentSnapshot.getString("Current Pet");
                onButtonClick(pet);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAILURE " + e.getMessage());
            }
        });


        docRef=db.collection("Users").document(userId).collection("Pets").document("PetDocNames");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    petOne=documentSnapshot.getString("PetDocName1");
                    petTwo=documentSnapshot.getString("PetDocName2");
                    petThree=documentSnapshot.getString("PetDocName3");
                    petFour=documentSnapshot.getString("PetDocName4");
                    petFive=documentSnapshot.getString("PetDocName5");
                    petSix=documentSnapshot.getString("PetDocName6");

                    petArray[0]=petOne;
                    petArray[1]=petTwo;
                    petArray[2]=petThree;
                    petArray[3]=petFour;
                    petArray[4]=petFive;
                    petArray[5]=petSix;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAILURE " + e.getMessage());
            }
        });


    }

    public void onButtonClick(String pet){

        if(pet!=null && pet!="") {

            db.collection("Users").document(userId).collection("Pets").document(pet).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                        trackerId = documentSnapshot.getString("Tracker Number");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "FAILURE " + e.getMessage());
                }
            });

        }

        track_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Dashboard.this,TrackPet.class);
                intent.putExtra("REF", pet);
                intent.putExtra("PET_ARRAY", petArray);
                intent.putExtra("PET_TRACKER_ID", trackerId);
                startActivity(intent);
            }
        });


    }
}