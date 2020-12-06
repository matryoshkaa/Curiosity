package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
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


public class PetProfile extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

    String petnamex, pettype, petbreed, petDOB, petstatus;
    TextView petname;
    TextView status;
    Button markPetAsLost;
    Button transferPet;
    Button deletePet;
    Button viewDetails;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petprofile);

        back_button = (ImageButton) findViewById(R.id.back_button);
        settings_button = (ImageButton) findViewById(R.id.settings_button);
        petname = findViewById(R.id.petProfileName);
        markPetAsLost = findViewById(R.id.markPetAsLost);
        status = findViewById(R.id.status);
        transferPet = findViewById(R.id.transferOwnership);
        deletePet = findViewById(R.id.deletepet);
        viewDetails = findViewById(R.id.ViewPetDetailsButton);
        String petName, petId;
        Bundle extras = getIntent().getExtras();
        petName = extras.getString("petname");
        petname.setText(petName);
        petId = extras.getString("petid");

        //firestore variables
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userid = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets").document("" + petId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable
                status.setText(documentSnapshot.getString("Status"));
                String statusText = status.getText().toString().toLowerCase();
                if (statusText.equals("lost")) markPetAsLost.setText("mark pet as found");


            }
        });


        markPetAsLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = markPetAsLost.getText().toString().toLowerCase();

                switch (buttonText) {
                    case "mark pet as found": {
                        markPetAsLost.setText("Mark pet as lost");
                        Map<String, String> statusmap = new HashMap<>();
                        statusmap.put("Status", "Found");
                        documentReference.set(statusmap, SetOptions.merge());

                        break;
                    }
                    case "mark pet as lost": {
                        markPetAsLost.setText("Mark pet as found");
                        Map<String, String> statusmap = new HashMap<>();
                        statusmap.put("Status", "Lost");
                        documentReference.set(statusmap, SetOptions.merge());
                        break;
                    }
                }
            }
        });

        intent = new Intent(PetProfile.this, TransferPet.class);
        DocumentReference documentReference1 = fStore.collection("Users").document(userid).collection("Pets").document("" + petId);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable
                petnamex = documentSnapshot.getString("Pet Name");
                pettype = documentSnapshot.getString("Pet Type");
                petbreed = documentSnapshot.getString("Pet Breed");
                petstatus = documentSnapshot.getString("Status");

                petDOB = documentSnapshot.getString("Pet DOB");
                intent.putExtra("petname", petnamex);
                intent.putExtra("pettype", pettype);
                intent.putExtra("petbreed", petbreed);
                intent.putExtra("petstatus", petstatus);
                intent.putExtra("petdob", petDOB);

            }
        });

        transferPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petid, petdocid, nop;
                Bundle extras = getIntent().getExtras();
                petid = extras.getString("petid");
                petdocid = extras.getString("petdocid");
                nop = extras.getString("nop");
                intent.putExtra("petid", petid);
                intent.putExtra("petdocid", petdocid);
                intent.putExtra("nop", nop);
                startActivity(intent);
            }
        });

        deletePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Users").document(userid);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int nop = Integer.parseInt(document.getData().get("Number of Pets").toString());
                                DeletePet(petId, nop);

                                Intent intent=new Intent(PetProfile.this,Dashboard.class);
                                startActivity(intent);
                            }
                        }
                    }
                });


            }
        });

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PetProfile.this,PetDetails.class);

                String petid;
                Bundle extras = getIntent().getExtras();
                petid = extras.getString("petid");
                intent.putExtra("petid", petid);
                startActivity(intent);
            }
        });


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetProfile.this, Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetProfile.this, Pet.class);
                startActivity(intent);
            }
        });
    }

    private void DeletePet(String petid, int nop) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();

        //Reduce your number of pets by 1
        NOPminusone(petid, nop);


    }
    private void NOPminusone(String petid, int nop) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();

        nop--;
        HashMap<String, String> update = new HashMap<>();
        update.put("Number of Pets", Integer.toString(nop));
        DocumentReference documentReference = fStore.collection("Users").document(userid);
        documentReference.set(update,SetOptions.merge());

        UpdatePetDocNames(petid, nop);
    }

    private void UpdatePetDocNames(String petid, int nop) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userid = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets").document("PetDocNames");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //get PetDocNames list
                        ArrayList<String> PetDocNames = new ArrayList<>();
                        int numberofpets = nop+1;
                        for (int i = 1; i <= numberofpets;i++)
                        {
                            PetDocNames.add(document.getData().get("PetDocName"+i).toString());
                        }

                        //remove pet from hashmap
                        PetDocNames.remove(petid);
//
                        //rewrite PetDocNames document
                        HashMap<String, String> updatedPetDocNames = new HashMap<>();
                        for (int i = 1; i <= nop;i++) //docnamenum
                        {
                            updatedPetDocNames.put("PetDocName"+i,PetDocNames.get(i-1));
                        }
                        DocumentReference doc = fStore.collection("Users").document(userid).collection("Pets").document("PetDocNames");
                        doc.set(updatedPetDocNames,SetOptions.merge());

                        //removing unused PetDocNames field
                        Map<String, Object> removal = new HashMap<>();
                        removal.put("PetDocName"+numberofpets, FieldValue.delete());
                        doc.update(removal);


                    }
                }
            }
        });

        //deleting pet from your database
        fStore.collection("Users").document(userid).collection("Pets").document(petid).delete();

    }


}
