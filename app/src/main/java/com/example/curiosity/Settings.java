package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable goBack = getResources().getDrawable(R.drawable.ic_black_back);
        getSupportActionBar().setHomeAsUpIndicator(goBack);

        //go back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void goToTermsAndCondition (View view){
        startActivity(new Intent(this, TermsAndCondition.class));
    }

    public void goToFaq (View view){
        startActivity(new Intent(this, Faq.class));
    }

    public void goToFeedbackAndSupport (View view){
        startActivity(new Intent(this, FeedbackAndSupport.class));
    }


    public void logout (View view){
//            mFirebaseAuth.signOut();
//            mGoogleSignInClient.signOut();
//
//            finish();
//            startActivity(new Intent(this, Login.class));

    }


}