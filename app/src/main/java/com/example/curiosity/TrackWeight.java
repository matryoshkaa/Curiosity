package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TrackWeight extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;
    TextView weight;
    Button save_weight;
    Button weight_analytic;


    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    String userId,name;
    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    Query query;

    String username;
    String user;
    String pet;
    String petWeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        weight=(TextView)findViewById(R.id.weight);
        save_weight=(Button)findViewById(R.id.save_weight);
        weight_analytic=(Button)findViewById(R.id.weight_analytic);


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackWeight.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackWeight.this,HealthWeight.class);
                startActivity(intent);
            }
        });



        db= FirebaseFirestore.getInstance();



        //firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            userId = firebaseUser.getUid();
            documentReference = db.collection("Users").document(userId);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(TrackWeight.this, Login.class));
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
                if(documentSnapshot.exists()){
                    name = documentSnapshot.getString("User Name");
                }
                else
                {
                    name=user;

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



    };

    public void onButtonClick(String pet){

        save_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                String petWeight = weight.getText().toString();

                Map<String, String> userMap=new HashMap<>();

                userMap.put("date",currentDate);
                userMap.put("weight",petWeight);

                if(pet!=null && !pet.equals("")){
                db.collection("Users")
                        .document(userId)
                        .collection("Pets")
                        .document(pet)
                        .collection("Weight")
                        .add(userMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(TrackWeight.this,"Weight has been added!",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();

                        Toast.makeText(TrackWeight.this,"Error: "+error,Toast.LENGTH_LONG).show();
                    }
                });
                }else
                    Toast.makeText(TrackWeight.this, "No pet has been added to track weight", Toast.LENGTH_SHORT).show();
            }
        });

        weight_analytic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                if (pet!= null && !pet.equals("")) {

                    db.collection("Users").document(userId).collection("Pets").document(pet).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                                petWeight = documentSnapshot.getString("Ideal Weight");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "FAILURE " + e.getMessage());
                        }
                    });

                }

                if(pet!=null && !pet.equals("")){
                    Intent intent=new Intent(TrackWeight.this,AnalyseWeight.class);
                    intent.putExtra("REF", pet);
                    intent.putExtra("IDEAL_WEIGHT", petWeight);
                    startActivity(intent);
            }
                else
                    Toast.makeText(TrackWeight.this, "No pet has been added to analyse weight", Toast.LENGTH_SHORT).show();

            }
        });




    }
}