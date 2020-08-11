package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TrackWeight extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    TextView weight;
    Button save_weight;

    private FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_weight);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        weight=(TextView)findViewById(R.id.weight);
        save_weight=(Button)findViewById(R.id.save_weight);


        mFirestore= FirebaseFirestore.getInstance();

        Calendar calendar= Calendar.getInstance();
        final String currentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackWeight.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackWeight.this,HealthWeight.class);
                startActivity(intent);
            }
        });

        save_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String petWeight=weight.toString();
                String petWeight = weight.getText().toString();

                Map<String, String> userMap=new HashMap<>();

                userMap.put("Date",currentDate);
                userMap.put("Weight",petWeight);

                mFirestore.collection("Users").document("Shruti").collection("Pets").document("Cookie").collection("Weight").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(TrackWeight.this,"Weight has been added!",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();

                        Toast.makeText(TrackWeight.this,"Error: "+error,Toast.LENGTH_LONG).show();
                    }
                });{

                }

            }
        });




    };
}