package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransferPet extends AppCompatActivity {
    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String yuserid;

    TextView yourid;
    TextView theirid;

    Button copyIdButton;
    Button transferButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferpet);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        transferButton= (Button) findViewById(R.id.transferButton);
        theirid = findViewById(R.id.transferId);

        //firestore variables
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        yuserid = fAuth.getCurrentUser().getUid(); //your user id


        String petid, petname, pettype, petbreed, petDOB, petstatus, petdocid, ynop;
        Bundle extras = getIntent().getExtras();
        petid= extras.getString("petid");
        petname= extras.getString("petname");
        pettype= extras.getString("pettype");
        petbreed= extras.getString("petbreed");
        petDOB= extras.getString("petdob");
        petstatus= extras.getString("petstatus");
        petdocid= extras.getString("petdocid");
        ynop= extras.getString("nop"); //ynop = your number of pets


        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference;

                //CHANGES TO YOUR DATABASE RECORDS
                //get Pet Data


                //reduce your number of pets by 1
                documentReference = fStore.collection("Users").document(yuserid);
                String updated_ynop = Integer.toString(Integer.parseInt(ynop)-1);
                Log.d("boop updated nop",updated_ynop);

                HashMap<String, String> ynop = new HashMap<>();
                ynop.put("Number of Pets",updated_ynop);
                documentReference.set(ynop, SetOptions.merge());

                //update your PetDocNames document
                documentReference = fStore.collection("Users").document(yuserid).collection("Pets").document("PetDocNames");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //get PetDocNames list
                                ArrayList<String> yPetDocNames = new ArrayList<>();
                                int numberofpets = Integer.parseInt(updated_ynop)+1;
                                for (int i = 1; i <= numberofpets;i++)
                                {
                                    yPetDocNames.add(document.getData().get("PetDocName"+i).toString());
                                }

                                //remove pet from hashmap
                                yPetDocNames.remove(petid);
//
                                //rewrite PetDocNames document
                                HashMap<String, String> updatedYPetDocNames = new HashMap<>();
                                for (int i = 1; i <= Integer.parseInt(updated_ynop);i++) //docnamenum
                                {
                                    updatedYPetDocNames.put("PetDocName"+i,yPetDocNames.get(i-1));
                                }
                                DocumentReference doc = fStore.collection("Users").document(yuserid).collection("Pets").document("PetDocNames");
                                doc.set(updatedYPetDocNames,SetOptions.merge());

                                //removing unused PetDocNames field
                                 Map<String, Object> removal = new HashMap<>();
                                 removal.put("PetDocName"+numberofpets, FieldValue.delete());
                                 doc.update(removal);

                            } else {
//                                Log.d("TAG", "No such document");
                            }
                        } else {
//                            Log.d("?TAG", "get failed with ", task.getException());
                        }
                    }
                });

                //deleting pet from your database
                fStore.collection("Users").document(yuserid).collection("Pets").document(petid).delete();


                //CHANGES TO THEIR DATABASE RECORDS
                //get their used id from textfield
                String tuserid; //their user id
                tuserid = theirid.getText().toString();
                Log.d("boop their user id",tuserid);

                //using data snapshot to edit their data
                DocumentReference docRef = fStore.collection("Users").document(tuserid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                //increase their number of pets by 1
                                Log.d("boop their nop", "DocumentSnapshot data: " + document.getData().get("Number of Pets").toString());
                                int tnop_ = Integer.parseInt(document.getData().get("Number of Pets").toString());
                                String updated_tnop = Integer.toString(tnop_+1);
                                Log.d("boop updated nop",updated_tnop);

                                HashMap<String, String> tnop = new HashMap<>();
                                tnop.put("Number of Pets",updated_tnop);
                                docRef.set(tnop, SetOptions.merge());

                                //add pet record to their PetDocNames document
                                DocumentReference documentReference = fStore.collection("Users").document(tuserid).collection("Pets").document(petid);
                                Map<String, String> addTPetDocName = new HashMap<>();
                                addTPetDocName.put("PetDocName"+updated_tnop, petid);
                                documentReference.set(addTPetDocName,SetOptions.merge());

                                //adding pet to their record
                                HashMap<String, String> addpet = new HashMap<>();
                                addpet.put("Pet ID",petid);
                                addpet.put("Pet Breed",petbreed);
                                addpet.put("Pet Type",pettype);
                                addpet.put("Pet DOB",petDOB);
                                addpet.put("Pet Name",petname);
                                addpet.put("Status",petstatus);
                                documentReference.set(addpet, SetOptions.merge());

                            } else {
//                                Log.d("TAG", "No such document");
                            }
                        } else {
//                            Log.d("?TAG", "get failed with ", task.getException());
                        }
                    }
                });


            }
        });
        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TransferPet.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TransferPet.this,Pet.class);
                startActivity(intent);
            }
        });

    }

    private void NOPplusone(String tuserid) {
        DocumentReference documentReference = fStore.collection("Users").document(tuserid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable

                String tnop_ = documentSnapshot.getString("Number of Pets");
                String updated_tnop = Integer.toString(Integer.parseInt(tnop_)+1);

                HashMap<String, String> tnop = new HashMap<>();
                tnop.put("Number of Pets",updated_tnop);
                documentReference.set(tnop, SetOptions.merge());
            }
        });

    }


}




