
package com.example.curiosity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Pet extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    ImageButton back_button;
    ImageButton settings_button;

    FloatingActionButton addPetButton;

    FirebaseAuth fAuth; //to get user id
    FirebaseFirestore fStore; //for data retrieval
    String userid;

    //image icons for pets
    ImageView peticon1, peticon2, peticon3, peticon4, peticon5, peticon6;
    ImageView [] petIconArray = {peticon1, peticon2, peticon3, peticon4, peticon5, peticon6};

    TextView petname1, petname2, petname3, petname4, petname5, petname6;
    TextView [] petNameArray = {petname1, petname2, petname3, petname4, petname5, petname6};
    String numberofpets;
    Intent intent;
    String [] petIdArray = {"","","","","",""};

    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        back_button = (ImageButton) findViewById(R.id.back_button);
        settings_button = (ImageButton) findViewById(R.id.settings_button);
        addPetButton = findViewById(R.id.addpettbutton);
        //linking variables to ids
        peticon1 = (ImageView)findViewById(R.id.peticon1);
        peticon2 = (ImageView)findViewById(R.id.peticon2);
        peticon3 = (ImageView)findViewById(R.id.peticon3);
        peticon4 = (ImageView)findViewById(R.id.peticon4);
        peticon5 = (ImageView)findViewById(R.id.peticon5);
        peticon6 = (ImageView)findViewById(R.id.peticon6);

        petIconArray[0]=peticon1;
        petIconArray[1]=peticon2;
        petIconArray[2]=peticon3;
        petIconArray[3]=peticon4;
        petIconArray[4]=peticon5;
        petIconArray[5]=peticon6;

        petname1 = (TextView)findViewById(R.id.petname1);
        petname2 = (TextView)findViewById(R.id.petname2);
        petname3 = (TextView)findViewById(R.id.petname3);
        petname4 = (TextView)findViewById(R.id.petname4);
        petname5 = (TextView)findViewById(R.id.petname5);
        petname6 = (TextView)findViewById(R.id.petname6);

        petNameArray[0]=petname1;
        petNameArray[1]=petname2;
        petNameArray[2]=petname3;
        petNameArray[3]=petname4;
        petNameArray[4]=petname5;
        petNameArray[5]=petname6;

        //firestore variables
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userid = fAuth.getCurrentUser().getUid();


        intent = new Intent(Pet.this, PetProfile.class);

        //pulling data from db and displaying pets
        DocumentReference documentReference = fStore.collection("Users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting num of pets (nop stands for number of pets)
                numberofpets = documentSnapshot.getString("Number of Pets");
                intent.putExtra("nop", numberofpets);

                int nop = Integer.parseInt(numberofpets);

                //displaying pet icons
                for (int i = 0; i < nop; i++)
                {
                    petIconArray[i].setVisibility(View.VISIBLE);
                    petNameArray[i].setVisibility(View.VISIBLE);
                    SetPetNames(i);

                    storage = FirebaseStorage.getInstance();
                    storageReference= storage.getReference();




                    DocumentReference docRef = fStore.collection("Users").document(userid).collection("Pets").document("PetDocNames");
                    int finalI1 = i;
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    //increase their number of pets by 1
                                    String m = document.getData().get("PetDocName"+(finalI1 +1)).toString();
                                    Log.d("boop", m);

                                    storageReference.child("images/Pets/" +m +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;

                                            Log.d("boop", downloadUrl.toString());
                                            getImage(downloadUrl.toString(), petIconArray[finalI1]);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // File not found
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



                }

            }
        });
        //peticons onclicks

        peticon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[0].getText().toString());
                intent.putExtra("petid", petIdArray[0]);
                intent.putExtra("petdocid", "1");
                startActivity(intent);

            }
        });
        peticon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[1].getText().toString());
                intent.putExtra("petid", petIdArray[1]);
                intent.putExtra("petdocid", "2");

                startActivity(intent);

            }
        });
        peticon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[2].getText().toString());
                intent.putExtra("petid", petIdArray[2]);
                intent.putExtra("petdocid", "3");

                startActivity(intent);

            }
        });
        peticon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[3].getText().toString());
                intent.putExtra("petid", petIdArray[3]);
                intent.putExtra("petdocid", "4");
                startActivity(intent);

            }
        });
        peticon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[4].getText().toString());
                intent.putExtra("petid", petIdArray[4]);
                intent.putExtra("petdocid", "5");
                startActivity(intent);

            }
        });
        peticon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("petname", petNameArray[5].getText().toString());
                intent.putExtra("petid", petIdArray[5]);
                intent.putExtra("petdocid", "6");
                startActivity(intent);

            }
        });
        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pet.this, Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pet.this, Dashboard.class);
                startActivity(intent);
            }
        });

        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if pets are less than 6, let them make a pet or else dont
                int nop = Integer.parseInt(numberofpets);
                if (nop == 6)
                {
                    Toast.makeText(Pet.this, "You can only hold 6 pets!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent(Pet.this, AddPet.class);
                    startActivity(intent);
                }
            }
        });


    }


    private void getImage(String ref, ImageView imageView) {
        ImageView imgview = (ImageView) findViewById(R.id.imageView);

        Glide.with(this)
                .load(ref)
                .into(imageView);
    }


    private void SetPetNames(int i) {
        PetId(i);
    }

    private void PetId(int i) {
        DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets").document("PetDocNames");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable
                petNameArray[i].setText(documentSnapshot.getString("PetDocName"+(i+1)));
                petIdArray[i]=petNameArray[i].getText().toString();
                PetName(petNameArray[i].getText().toString(),  i);

            }
        });
    }

    private void PetName(String s, int i) {

        DocumentReference documentReference = fStore.collection("Users").document(userid).collection("Pets").document(s);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //getting number of pets from the db and setting it to variable
                petNameArray[i].setText(documentSnapshot.getString("Pet Name"));

            }
        });
    }


}