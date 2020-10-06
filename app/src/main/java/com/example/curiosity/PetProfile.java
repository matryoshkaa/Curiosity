package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class PetProfile extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    ImageButton back_button;
    ImageButton settings_button;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

     TextView petname;

    public static String petid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petprofile);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        petname=findViewById(R.id.petProfileName);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference =
                fStore.collection("Users")
                        .document(userid).collection("Pets").document("Pet"+petid);

        //pulling data from db


        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                petname.setText(documentSnapshot.getString("Pet Name"));

            }
        });


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(PetProfile.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(PetProfile.this,Pet.class);
                startActivity(intent);
            }
        });
    }
}
