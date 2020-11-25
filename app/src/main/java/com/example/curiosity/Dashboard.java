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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    String pet="";
    String username;
    String user;

    String trackerId="";

    Map<String, Object> map;
    List<Collection<Object>> sortedRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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



    }

    public void onButtonClick(String pet) {


        if (pet != null && !pet.equals("")) {

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

        sortedRecord=new ArrayList<java.util.Collection<Object>>();

        if (pet != null && pet != "") {

            db.collection("Users")
                    .document(userId)
                    .collection("Pets")
                    .document(pet)
                    .collection("Location").orderBy("Date", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            map = document.getData();
                            String date = map.get("Date").toString();
                            String latitude = map.get("Latitude").toString();
                            String longitude = map.get("Longitude").toString();
                            sortedRecord.add(map.values());

                        }
                    } else {
                        System.out.println("Error getting documents: ");
                    }
                }
            });
        }

            track_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Dashboard.this, TrackPet.class);
                    intent.putExtra("REF", pet);
                    intent.putExtra("PET_TRACKER_ID", trackerId);

                    if(sortedRecord!=null && !sortedRecord.isEmpty())
                        intent.putExtra("LATEST_COORDINATES", sortedRecord.get(0).toString());
                    else
                        intent.putExtra("LATEST_COORDINATES", "");

                    startActivity(intent);
                }
            });



    }

}