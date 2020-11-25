package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class Heatmap extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton back_button;
    ImageButton settings_button;

    GoogleMap map;
    List<LatLng> coordinateList = new ArrayList<>();

    int[] colors = {
            Color.GREEN,
            Color.YELLOW,
            Color.rgb(255,165,0),
            Color.RED,
    };

    float[] startPoints = {
            0.2F, 0.6F,0.8F, 1.0F
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        back_button=(ImageButton)findViewById(R.id.back_button);
        settings_button=(ImageButton)findViewById(R.id.settings_button);

        Intent i = getIntent();
        coordinateList = (List<LatLng>) i.getSerializableExtra("ARRAY");


        //on press settings button
        settings_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Heatmap.this,Settings.class);
                startActivity(intent);
            }
        });

        //on press go back
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;

        if(coordinateList!=null) {
            double lt = coordinateList.get(0).latitude;
            double ln = coordinateList.get(0).longitude;
            LatLng petLoc = new LatLng(lt, ln);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(petLoc, 16), 2000, null);
            addHeatMap(coordinateList);
        }


    }

    public void addHeatMap(List<LatLng> coordinateList) {

        Gradient gradient = new Gradient(colors, startPoints);

        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(coordinateList)
                .gradient(gradient)
                .build();

        provider.setRadius(100);

        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
        overlay.clearTileCache();



    }
}