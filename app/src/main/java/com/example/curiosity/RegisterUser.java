package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterUser extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    private TextView loginText;
    private EditText userName, userEmail,password,confirmPass;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //FirebaseAuth mFirebaseAuth;

    String userId;
    ProgressDialog progressDialog;

    DocumentReference documentReference;
    //final Context mcontext = new Context(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        //Directs user back to login page on clicking 'Login'
        loginText= (TextView) findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(RegisterUser.this,Login.class);
                startActivity(intent);
            }
        });



        //Getting variable values using their IDs
        userName = (EditText)findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        registerButton = (Button) findViewById(R.id.registerButton);




        registerButton.setOnClickListener(new View.OnClickListener(){

            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view){



                String user=userName.getText().toString();
                String email = userEmail.getText().toString();
                String userPassword = password.getText().toString();
                String confirmPassword = confirmPass.getText().toString();

                if(email.isEmpty()){
                    userEmail.setError("Please enter your email");
                    userEmail.requestFocus(); }
                else if(userPassword.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();}
                else if(userPassword.length()<7){
                    password.setError("Password must be more than 6 characters");
                    password.requestFocus();
                }
                else if(user.isEmpty()){
                    userName.setError("Please enter your username");
                    userName.requestFocus(); }
                else if(confirmPassword.isEmpty()){
                    confirmPass.setError("Please enter your password");
                    confirmPass.requestFocus(); }
                else if(!user.matches("") && !email.matches("") && !userPassword.matches("") && !confirmPassword.matches("")){
                    System.out.println("Creating account....");

                    if(userPassword.equals(confirmPassword)){

                        mAuth.createUserWithEmailAndPassword(email, userPassword)
                                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.

                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegisterUser.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });


                        mAuth.signInWithEmailAndPassword(email, userPassword)
                                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(RegisterUser.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });

                    }
                }else{
                    password.setError("Password does not match.");
                    password.requestFocus();

                }


            }
        });


    }





}


