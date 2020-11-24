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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    String idealWeight;
    String latestWeight;

    Map<String, Object> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        weight=(TextView)findViewById(R.id.weight);
        save_weight=(Button)findViewById(R.id.save_weight);
        weight_analytic=(Button)findViewById(R.id.weight_analytic);

        user=getIntent().getStringExtra("USERID");
        pet=getIntent().getStringExtra("REF");

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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


        if (pet!= null && !pet.equals("")) {

            db.collection("Users").document(user).collection("Pets").document(pet).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                        idealWeight = documentSnapshot.getString("Weight");
                    onButtonClick(idealWeight);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "FAILURE " + e.getMessage());
                }
            });

        }

        if (pet != null && pet != "") {

            db.collection("Users")
                    .document(user)
                    .collection("Pets")
                    .document(pet)
                    .collection("Weight").orderBy("date", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            map = document.getData();
                            latestWeight = map.get("weight").toString();
                            System.out.println("LATEST WEIGHT "+document.getData());
                        }
                    } else {
                        System.out.println("Error getting documents: ");
                    }
                }
            });
        }




    };

    public void onButtonClick(String idealWeight){

        weight_analytic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(pet!=null && !pet.equals("")){

                    Intent intent=new Intent(TrackWeight.this,AnalyseWeight.class);
                    intent.putExtra("REF", pet);
                    intent.putExtra("WEIGHT", idealWeight);
                    intent.putExtra("LATESTWEIGHT", latestWeight);

                    startActivity(intent);
            }
                else
                    Toast.makeText(TrackWeight.this, "No pet has been added to analyse weight", Toast.LENGTH_SHORT).show();

            }
        });


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
                            .document(user)
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



    }
}