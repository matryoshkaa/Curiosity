package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UpcomingCheckupRecords extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;
    RecyclerView checkupList;


    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    String userId, name;

    DocumentReference documentReference;
    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference ref;
    private CheckupAdapter adapter;

    FirebaseFirestore fStore; //for data retrieval

    Query query;

    String username;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_checkup_records);
        db = FirebaseFirestore.getInstance();


        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            documentReference = db.collection("Users").document(userId);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(UpcomingCheckupRecords.this, Login.class));
                }
            }
        };


        //update your PetDocNames document
        DocumentReference documentReference1 = fStore.collection("Users").document("UlR5kZuOyjS2kuj5CmqXAm0IF0C2");
        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document1 = task.getResult();
                    if (document1.exists()) {

                        //getting currentpetid
                        String currentpetid = document1.getData().get("Current Pet").toString();
                        Log.d("boop currentpet", currentpetid);

                        //get petrecord id
                        String petrecordid="UMN1DnQPG6rny7g8kx8Y";

                        DocumentReference documentReference2 = fStore.collection("Users")
                                .document("UlR5kZuOyjS2kuj5CmqXAm0IF0C2")
                                .collection("Pets")
                                .document(currentpetid)
                                .collection("Check Up Records")
                                .document(petrecordid);
                        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document2 = task.getResult();
                                    if (document2.exists()) {
                                        String summary = document2.getData().get("summary").toString();
                                        Log.d("boop summary", summary);
                                    }
                                }
                            }
                        });



                    } else {
//                                Log.d("TAG", "No such document");
                    }
                } else {
//                            Log.d("?TAG", "get failed with ", task.getException());
                }
            }
        });

//
//        // google user retrieval
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            user = acct.getDisplayName();
//        }
//
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    name = documentSnapshot.getString("User Name");
//                } else {
//                    name = user;
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "FAILURE " + e.getMessage());
//            }
//        });
//
//        ref=db.collection("Users")
//                .document(userId)
//                .collection("Pets")
//                .document("Berry")
//                .collection("Check Up Records");
//


        back_button = (ImageButton) findViewById(R.id.back_button);
        settings_button = (ImageButton) findViewById(R.id.settings_button);

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpcomingCheckupRecords.this, Settings.class);
                startActivity(intent);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpcomingCheckupRecords.this, CheckupRecords.class);
                startActivity(intent);
            }
        });


    }


//
//    private void setUpRecyclerView(){
//        //Query query=ref.whereEqualTo("status","Past").orderBy("date");
//
//        Date currentDate=new Date();
//
//        Query query=ref.whereGreaterThanOrEqualTo("date",currentDate)
//                .orderBy("date");
//
//        //check if date in record is before today's date
//
//        FirestoreRecyclerOptions<Checkup> checkups=new FirestoreRecyclerOptions.Builder<Checkup>()
//                .setQuery(query,Checkup.class).build();
//
//        adapter=new CheckupAdapter(checkups);
//
//        checkupList=(RecyclerView) findViewById(R.id.checkupList);
//        checkupList.setHasFixedSize(true);
//        checkupList.setLayoutManager(new LinearLayoutManager(this));
//        checkupList.setAdapter(adapter);
//
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
}