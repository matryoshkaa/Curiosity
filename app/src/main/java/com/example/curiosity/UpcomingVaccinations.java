package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class UpcomingVaccinations extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;
    RecyclerView vaccineList;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    String userId, name;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference ref;
    private VaccinationAdapter adapter;

    Query query;

    String username;
    String user;
    String currentPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_vaccinations);

        //get selected pet ID
        currentPet = getIntent().getStringExtra("REF");

        //setting up database and user credentials
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
                    startActivity(new Intent(UpcomingVaccinations.this, Login.class));
                }
            }
        };


        // google user retrieval
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            user = acct.getDisplayName();
        }

        //user name retrieval
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

        //setting up document reference
        if(currentPet!=null && !currentPet.equals("")) {

            db.collection("Users")
                    .document(userId)
                    .collection("Pets")
                    .document(currentPet)
                    .collection("Vaccination Records").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().size()==0)
                            Toast.makeText(UpcomingVaccinations.this, "No records to display", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

            ref = db.collection("Users")
                    .document(userId)
                    .collection("Pets")
                    .document(currentPet)
                    .collection("Vaccination Records");

            setUpRecyclerView();
        }
        else
            Toast.makeText(UpcomingVaccinations.this, "No records to display", Toast.LENGTH_SHORT).show();


        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(UpcomingVaccinations.this,Settings.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(UpcomingVaccinations.this,VaccinationRecords.class);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView(){

        Date currentDate=new Date();

        //check if date in record is after today's date
        Query query=ref.whereGreaterThanOrEqualTo("date",currentDate)
                .orderBy("date");

        FirestoreRecyclerOptions<Vaccination> vaccines=new FirestoreRecyclerOptions.Builder<Vaccination>()
                .setQuery(query,Vaccination.class).build();

        adapter=new VaccinationAdapter(vaccines);

        vaccineList=(RecyclerView) findViewById(R.id.vaccineList);
        vaccineList.setHasFixedSize(true);
        vaccineList.setLayoutManager(new LinearLayoutManager(this));
        vaccineList.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();

        if(currentPet!=null && !currentPet.equals(""))
            adapter.startListening();
        else
            Toast.makeText(UpcomingVaccinations.this, "No records to display", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onStop() {
        super.onStop();
        if(currentPet!=null && !currentPet.equals(""))
            adapter.stopListening();
        else
            Toast.makeText(UpcomingVaccinations.this, "No records to display", Toast.LENGTH_SHORT);
    }
}