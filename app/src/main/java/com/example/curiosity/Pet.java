package com.example.curiosity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Pet extends AppCompatActivity {

    ImageButton back_button;
    ImageButton settings_button;
    FloatingActionButton addPetButton;
    Button temporarynfcButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        addPetButton=findViewById(R.id.addpettbutton);
        temporarynfcButton=findViewById(R.id.temporaryNFCButton);
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

        //go to add pet page
        addPetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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