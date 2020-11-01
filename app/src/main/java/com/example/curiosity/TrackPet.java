package com.example.curiosity;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;


public class TrackPet extends FragmentActivity implements OnMapReadyCallback {

    ImageButton back_button;
    ImageButton settings_button;

    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_pet);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        //Sms testing

        ActivityCompat.requestPermissions(TrackPet.this,new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public String readSMS(){

        String strbody="";

        Uri uri = Uri.parse("content://sms/");

        ContentResolver contentResolver = getContentResolver();

        String phoneNumber = "+971562427888";
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

        String petCoordinates=readSMS();
        //System.out.println("COORDINATES ARE"+petCoordinates);

        if(petCoordinates!="") {
            String[] location = petCoordinates.split(",");
            double lat = Double.parseDouble(location[0]);
            double lng = Double.parseDouble(location[1]);

            //System.out.println("LATLNG"+lat+" "+lng);

            LatLng petLocation = new LatLng(lat, lng);
            map.addMarker(new MarkerOptions().position(petLocation).title("Your pet"));
            map.moveCamera(CameraUpdateFactory.newLatLng(petLocation));
        }
        else
            Toast.makeText(TrackPet.this, "No location to display", Toast.LENGTH_SHORT).show();

    }
}