package com.example.curiosity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class TrackPet extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "-----------------------";

    ImageButton back_button;
    ImageButton settings_button;
    Button heatmapButton;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    DocumentReference documentReference;
    CollectionReference ref;
    String userId, name;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    String pet;
    String username;
    String user;
    String trackerId;

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_pet);

        pet = getIntent().getStringExtra("REF");
        trackerId = getIntent().getStringExtra("PET_TRACKER_ID");

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);
        heatmapButton=(Button) findViewById(R.id.heatmapButton);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();
            documentReference = db.collection("Users").document(userId);
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(TrackPet.this, Login.class));
                }
            }
        };


        // google user retrieval
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            user = acct.getDisplayName();
        }

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("User Name");
                } else {
                    name = user;

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAILURE " + e.getMessage());
            }
        });


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackPet.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackPet.this,Dashboard.class);
                startActivity(intent);
            }
        });

        heatmapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(TrackPet.this,Heatmap.class);
                startActivity(intent);
            }
        });

        //SMS PERMISSION
        ActivityCompat.requestPermissions(TrackPet.this,new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }


    public String readSMS(){

        String strbody="";
        Uri uri = Uri.parse("content://sms/");

        ContentResolver contentResolver = getContentResolver();

        System.out.println("TRACKER ID"+trackerId);
        String phoneNumber = trackerId;
        String sms = "address='"+ phoneNumber + "'";
        Cursor cursor = contentResolver.query(uri, new String[] { "_id", "body" }, sms, null,   null);

        System.out.println ( cursor.getCount() );

        while (cursor.moveToNext())
        {
            strbody = cursor.getString( cursor.getColumnIndex("body") );
            System.out.println ( strbody );
            break;
        }

        return strbody;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

        if(pet!=null && !pet.equals("") && trackerId!=null) {
            String petCoordinates = readSMS();

            if (!petCoordinates.equals("")) {
                String[] location = petCoordinates.split(",");

                double lat = Double.parseDouble(location[0]);
                double lng = Double.parseDouble(location[1]);

                addCoordinates(lat,lng);

                LatLng petLocation = new LatLng(lat, lng);
                map.addMarker(new MarkerOptions().position(petLocation).title("Your pet"));
                map.moveCamera(CameraUpdateFactory.newLatLng(petLocation));
            } else
                Toast.makeText(TrackPet.this, "No location to display", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(TrackPet.this, "No location to display", Toast.LENGTH_SHORT).show();
    }

    public void addCoordinates(double lat,double lng){

        Map<String, Number> coordinateMap=new HashMap<>();
        coordinateMap.put("Latitude",lat);
        coordinateMap.put("Longitude",lng);

            if(pet!=null && !pet.equals("")){
                db.collection("Users")
                        .document(userId)
                        .collection("Pets")
                        .document(pet)
                        .collection("Location")
                        .add(coordinateMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("TAG", "Coordinates has been added to the database!");
                                System.out.println("Coordinates got added");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error=e.getMessage();
                        Log.d("TAG", "Error adding coordinates");System.out.println("ERROR ADDING COORD");
                    }
                });
            }else
                Log.d("TAG", "Error adding coordinates");System.out.println("ERROR ADDING COORD");
        }


}