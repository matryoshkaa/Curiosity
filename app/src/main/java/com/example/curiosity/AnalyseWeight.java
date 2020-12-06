package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

    //weight range variable
    double underweight;
    double slightlyUnderweight;
    double slightlyOverweight;
    double overweight;

    TextView analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        analysis=(TextView)findViewById(R.id.analysis);

        db = FirebaseFirestore.getInstance();

        //get selected pet ID
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

        if(idealWeight!=null && !idealWeight.equals(""))
            if(latestWeight!=null && !latestWeight.equals("")){

                idealPetWeight=Double.parseDouble(idealWeight);
                latestPetWeight=Double.parseDouble(latestWeight);

                System.out.println("PET CURRENT WEIGHT "+latestPetWeight);

                underweight=(idealPetWeight/2);
                slightlyUnderweight=(underweight*1.5);
                slightlyOverweight=(underweight*2.5);
                overweight=(underweight*3.5);

                if(latestPetWeight==idealPetWeight)
                    analysis.setText(R.string.ideal);
                else if(latestPetWeight>underweight&&latestPetWeight<slightlyUnderweight)
                    analysis.setText(R.string.underweight);
                else if(latestPetWeight>slightlyUnderweight&&latestPetWeight<idealPetWeight)
                    analysis.setText(R.string.slightUnderweight);
                else if(latestPetWeight>idealPetWeight&&latestPetWeight<slightlyOverweight)
                    analysis.setText(R.string.slightOverweight);
                else if(latestPetWeight>slightlyOverweight&&latestPetWeight<overweight)
                    analysis.setText(R.string.overweight);
                else if(latestPetWeight<underweight)
                    analysis.setText(R.string.tooUnderweight);
                else if(latestPetWeight>overweight)
                    analysis.setText(R.string.tooOverweight);
                db.collection("Users")
                        .document(userId)
                        .collection("Pets")
                        .document(currentPet)
                        .collection("Weight").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size()==0)
                                Toast.makeText(AnalyseWeight.this, "No information to display", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


            }
            else
                Toast.makeText(AnalyseWeight.this, "Do not have enough weight information to conduct analysis", Toast.LENGTH_SHORT).show();

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
                Intent intent=new Intent(AnalyseWeight.this,TrackWeight.class);
                startActivity(intent);
            }
        });

    }



}
