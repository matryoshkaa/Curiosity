package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Pet extends AppCompatActivity {


    ImageButton back_button;
    ImageButton settings_button;
    FloatingActionButton addPetButton;
    Button temporarynfcButton;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

    ImageView peticon1, peticon2, peticon3, peticon4, peticon5, peticon6;
    ImageView[] peticons = {peticon1, peticon2, peticon3, peticon4, peticon5, peticon6};

    int numberofpets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        addPetButton=findViewById(R.id.addpettbutton);
        temporarynfcButton=findViewById(R.id.temporaryNFCButton);

        peticon1 = (ImageView) findViewById(R.id.peticon1);
        peticon2 = (ImageView) findViewById(R.id.peticon2);
        peticon3 = (ImageView) findViewById(R.id.peticon3);
        peticon4 = (ImageView) findViewById(R.id.peticon4);
        peticon5 = (ImageView) findViewById(R.id.peticon5);
        peticon6 = (ImageView) findViewById(R.id.peticon6);

        peticons[0]=peticon1;
        peticons[0].setVisibility(View.VISIBLE);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Users").document(userid);
        //pulling data from db
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                numberofpets = Integer.parseInt(documentSnapshot.getString("Number of Pets"));

            }
        });
        System.out.println(numberofpets);
        for (int i = 0; i < numberofpets;i++){
//            peticons[i].setVisibility(View.VISIBLE);
        }
        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Pet.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Pet.this,Dashboard.class);
                startActivity(intent);
            }
        });

        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Pet.this, AddPet.class);
                startActivity(intent);
            }
        });
        temporarynfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Pet.this, NFCPetDetails.class);
                startActivity(intent);
            }
        });
    }


        }

