package com.example.curiosity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
//
//public class Heatmap extends AppCompatActivity implements OnMapReadyCallback {
//
//    ImageButton back_button;
//    ImageButton settings_button;
//
//    GoogleMap map;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_heatmap);
//
//        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        back_button=(ImageButton)findViewById(R.id.back_button);
//        settings_button=(ImageButton)findViewById(R.id.settings_button);
//
//        //on press settings button
//        settings_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent=new Intent(Heatmap.this,Settings.class);
//                startActivity(intent);
//            }
//        });
//
//        //on press go back
//        back_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent=new Intent(Heatmap.this,TrackPet.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
////    @Override
////    public void onMapReady(GoogleMap googleMap) {
////        map=googleMap;
////
////            if (!petCoordinates.equals("")) {
////                String[] location = petCoordinates.split(",");
////
////                double lat = Double.parseDouble(location[0]);
////                double lng = Double.parseDouble(location[1]);
////
////                LatLng petLocation = new LatLng(lat, lng);
////                map.addMarker(new MarkerOptions().position(petLocation).title("Your pet"));
////                map.moveCamera(CameraUpdateFactory.newLatLng(petLocation));
////            } else
////                Toast.makeText(Heatmap.this, "No location to display", Toast.LENGTH_SHORT).show();
////        }
////
////    private void addHeatMap() {
////
////        List<LatLng> latLngs = null;
////
////        // Get the data: latitude/longitude positions of police stations.
////
////        // Create a heat map tile provider, passing it the latlngs of the police stations.
////        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
////                .data(latLngs)
////                .build();
////
////        // Add a tile overlay to the map, using the heat map tile provider.
////        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
////    }
//
//
//}