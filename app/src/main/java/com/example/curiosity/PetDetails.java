package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PetDetails extends AppCompatActivity {


    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;
    TextView scannedid, petbreed, petdob, pettype, petname, ownerphone, ownername;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petdetails);
        scannedid = findViewById(R.id.scannedID);
        petbreed = findViewById(R.id.petBreed);
        petdob = findViewById(R.id.petDOB);
        pettype= findViewById(R.id.petType);
        petname = findViewById(R.id.petName);
        ownerphone= findViewById(R.id.ownerPhone);
        ownername= findViewById(R.id.ownerName);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();




        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userid);

        //pulling data from db
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                ownername.setText(documentSnapshot.getString("User Name"));
                String phoneExists = documentSnapshot.getString("Phone");
                if(TextUtils.isEmpty(phoneExists)){}else{ownerphone.setText(documentSnapshot.getString("Phone")); }
            }
        });


        String petid;
        Intent i = this.getIntent();
        if(i!=null){
            String strdata = i.getExtras().getString("source");
            if(strdata.equals("petprofile")){
                petid = i.getStringExtra("petid");
                DocumentReference documentReference1 = fStore.collection("Users").document(userid).collection("Pets").document("" + petid);
                //pulling data from db
                documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                        scannedid.setText(documentSnapshot.getString("Pet ID"));;
                        petbreed.setText(documentSnapshot.getString("Pet Breed"));;
                        petdob.setText(documentSnapshot.getString("Pet DOB"));
                        pettype.setText(documentSnapshot.getString("Pet Type"));
                        petname.setText(documentSnapshot.getString("Pet Name"));;
                    }
                });
            }
            if(strdata.equals("scanpet")){
                petid = i.getStringExtra("petid");
                DocumentReference documentReference1 = fStore.collection("Users").document(userid).collection("Pets").document("" + petid);
                //pulling data from db
                documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                        scannedid.setText(documentSnapshot.getString("Pet ID"));;
                        petbreed.setText(documentSnapshot.getString("Pet Breed"));;
                        petdob.setText(documentSnapshot.getString("Pet DOB"));
                        pettype.setText(documentSnapshot.getString("Pet Type"));
                        petname.setText(documentSnapshot.getString("Pet Name"));;
                    }
                });
            }
        }





        //back button
        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(PetDetails.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(PetDetails.this,Dashboard.class);
                startActivity(intent);
            }
        });




        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();


    }



}