package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AnalyseWeight extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    CollectionReference collectionReference;
    String userId,name;
    private FirebaseFirestore db;
    FirebaseUser firebaseUser;
    Task<QuerySnapshot> ref;

    String user;
    String currentPet;
    String idealWeight;
    String latestWeight;

    double idealPetWeight;
    double latestPetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        db = FirebaseFirestore.getInstance();

        //get selected pet ID and ideal weight
        currentPet = getIntent().getStringExtra("REF");
        idealWeight = getIntent().getStringExtra("WEIGHT");
        latestWeight = getIntent().getStringExtra("LATESTWEIGHT");

        System.out.println("ID AND WEIGHT "+currentPet +" "+idealWeight);

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
                    startActivity(new Intent(AnalyseWeight.this, Login.class));
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

        if(idealWeight!=null && idealWeight!="")
            if(latestWeight!=null && latestWeight!=""){

            idealPetWeight=Double.parseDouble(idealWeight);
            latestPetWeight=Double.parseDouble(latestWeight);

            if(latestPetWeight>idealPetWeight)
                System.out.println("YOUR PET HEAVY");
            else
                System.out.println("YOUR PET NO EAT ANYTHING");
            }

        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(AnalyseWeight.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(AnalyseWeight.this,HealthWeight.class);
                startActivity(intent);
            }
        });

    }



}
