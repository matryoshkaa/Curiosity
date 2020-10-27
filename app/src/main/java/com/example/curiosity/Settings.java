package com.example.curiosity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

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

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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
           mFirebaseAuth.signOut();
           mGoogleSignInClient.signOut();

            finish();
          startActivity(new Intent(this, Login.class));

    }

    public void goToPetSelect (View view)
    {
            startActivity(new Intent(this, PetFromSettings.class));

    }
}