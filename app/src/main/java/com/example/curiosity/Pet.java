package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    private static final String TAG = "MyActivity";
    ImageButton back_button;
    ImageButton settings_button;
    FloatingActionButton addPetButton;
    Button temporarynfcButton;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

    ImageView peticon1, peticon2, peticon3, peticon4, peticon5, peticon6;
    ImageView[] peticons = {peticon1, peticon2, peticon3, peticon4, peticon5, peticon6};

    TextView petname1, petname2, petname3, petname4, petname5, petname6;
    TextView[] petnames = {petname1, petname2, petname3, petname4, petname5, petname6};

    TextView pets;
    TextView numberofpets;
    String nop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        addPetButton=findViewById(R.id.addpettbutton);
        temporarynfcButton=findViewById(R.id.temporaryNFCButton);
        pets = findViewById(R.id.petHeader);
        numberofpets=findViewById(R.id.numofpets);

        peticon1 = (ImageView) findViewById(R.id.peticon1);
        peticon2 = (ImageView) findViewById(R.id.peticon2);
        peticon3 = (ImageView) findViewById(R.id.peticon3);
        peticon4 = (ImageView) findViewById(R.id.peticon4);
        peticon5 = (ImageView) findViewById(R.id.peticon5);
        peticon6 = (ImageView) findViewById(R.id.peticon6);

        peticons[0]=peticon1;
        peticons[1]=peticon2;
        peticons[2]=peticon3;
        peticons[3]=peticon4;
        peticons[4]=peticon5;
        peticons[5]=peticon6;

        petname1 = findViewById(R.id.petname1);
        petname2 = findViewById(R.id.petname2);
        petname3 = findViewById(R.id.petname3);
        petname4 = findViewById(R.id.petname4);
        petname5 = findViewById(R.id.petname5);
        petname6 = findViewById(R.id.petname6);


        petnames[0] = petname1;
        petnames[1] = petname2;
        petnames[2] = petname3;
        petnames[3] = petname4;
        petnames[4] = petname5;
        petnames[5] = petname6;


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        userid = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference =
                fStore.collection("Users")
                        .document(userid);

        //pulling data from db
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                numberofpets.setText(documentSnapshot.getString("Number of Pets"));
                 nop = numberofpets.getText().toString();
                 int numofpets = Integer.parseInt(nop);
                        System.out.println(numberofpets);
                        for (int i = 0; i < numofpets;i++){
                     peticons[i].setVisibility(View.VISIBLE);
                     petnames[i].setVisibility(View.VISIBLE);
                        }
            }
        });
for (int i = 0;i<6;i++) {
    DocumentReference documentReference1 =
            fStore.collection("Users")
                    .document(userid).collection("Pets").document("Pet"+(i+1));

    //pulling data from db
    int finalI = i;
    documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                petnames[finalI].setText(documentSnapshot.getString("Pet Name"));
        }
    });
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

        peticon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="1";
                Intent intent=new Intent(Pet.this, PetProfile.class);

                startActivity(intent);
            }
        });
        peticon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="2";

                Intent intent=new Intent(Pet.this, PetProfile.class);
                startActivity(intent);
            }
        });
        peticon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="3";

                Intent intent=new Intent(Pet.this, PetProfile.class);
                startActivity(intent);
            }
        });
        peticon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="4";

                Intent intent=new Intent(Pet.this, PetProfile.class);
                startActivity(intent);
            }
        });
        peticon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="5";

                Intent intent=new Intent(Pet.this, PetProfile.class);
                startActivity(intent);
            }
        });
        peticon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetProfile.petid="6";
                Intent intent=new Intent(Pet.this, PetProfile.class);
                startActivity(intent);
            }
        });
    }


        }

