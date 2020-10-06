package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterUser extends AppCompatActivity {

    private static final String TAG = "-----------------------";

    private TextView loginText;
    private EditText userName, userEmail,password,confirmPass;
    private Button registerButton;

    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    DocumentReference documentReference;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


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
        userName = (EditText)findViewById(R.id.userProfileName);
        userEmail = (EditText)findViewById(R.id.userEmail);
        password = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirmPass);
        registerButton = (Button) findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener(){

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
                else if(!(email.isEmpty() && userPassword.isEmpty() && confirmPassword.isEmpty() && user.isEmpty())){
                    //CHECK IF PASSWORD IS >= 6 characters
                    progressDialog.setMessage("Creating account...");
                    progressDialog.show();
                    if(userPassword.equals(confirmPassword)){

                        mAuth.createUserWithEmailAndPassword(email, userPassword)
                                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();

                                        if (!task.isSuccessful()) {
                                            password.setError("Registration unsuccessful.");
                                        }else {
                                            Toast.makeText(RegisterUser.this, "Register Successful", Toast.LENGTH_LONG);

                                        // insert data in firestore
                                            userId = mAuth.getCurrentUser().getUid();
                                            documentReference = db.collection("Users").document(userId);


                                            Map<String, String> appuser = new HashMap<>();
                                            appuser.put("Email", userEmail.getText().toString());
                                            appuser.put("User Name", userName.getText().toString());

                                            documentReference.set(appuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "User Created" );
                                                    Toast.makeText(RegisterUser.this, "Account has been created!", Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure" + e.getMessage());
                                                }
                                            });

                                        }
                                    }

                                });
                    }else{
                        progressDialog.dismiss();
                        password.setError("Password is not the same");
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterUser.this, "Error occured", Toast.LENGTH_SHORT);
                }
            }
        });

    }

}


