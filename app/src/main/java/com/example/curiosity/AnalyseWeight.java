package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

    Query query;
    String username;
    String user;

    LineGraphSeries series;
    GraphView graph;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries();

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

        collectionReference=db.collection("Users")
                .document(userId)
                .collection("Pets")
                .document("Berry")
                .collection("Weight");

        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    WeightData node = documentSnapshot.toObject(WeightData.class);
                    node.setDocumentId(documentSnapshot.getId());
                    String documentId = node.getDocumentId();
                    String date = node.getDate();
                    String weight = node.getWeight();

                    SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                    Date d = null;
                    try {
                        d = f.parse(date);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    long milliseconds = d.getTime();
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(milliseconds,  Integer. parseInt(weight)),
                        });
                        graph.addSeries(series);

                }
            }

        });





        setUpGraph();



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


    public void setUpGraph() {
        ref=db.collection("Users")
                .document(userId)
                .collection("Pets")
                .document("Berry")
                .collection("Weight")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            WeightData node = documentSnapshot.toObject(WeightData.class);
                            node.setDocumentId(documentSnapshot.getId());
                            String documentId = node.getDocumentId();
                            String date = node.getDate();
                            String weight = node.getWeight();

                            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
                            Date d = null;
                            try {
                                d = f.parse(date);
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                            long milliseconds = d.getTime();
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                    new DataPoint(milliseconds,  Integer. parseInt(weight)),
                            });
                            graph.addSeries(series);



                        }
                    }

                });

}


}