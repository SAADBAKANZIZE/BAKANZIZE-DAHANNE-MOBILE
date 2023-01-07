package com.genuinecoder.springclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity  implements OnMapReadyCallback {
    //
    TextView nom, adresse, dateCreation, owner, phone;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    Intent intent;
    double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (mGoogleMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }
        nom = findViewById(R.id.name);
        adresse = findViewById(R.id.adresse);
        dateCreation = findViewById(R.id.dateCreation);
        owner = findViewById(R.id.owner);
        phone = findViewById(R.id.phone);
        intent = getIntent();
        String name = intent.getStringExtra("name");
        //String date = intent.getStringExtra("date");
        String pharmacien = intent.getStringExtra("pharmacien");
        String contact = intent.getStringExtra("contact");
        String adress = intent.getStringExtra("adresse");

        System.out.println(lat);
        System.out.println(lon);
        nom.setText(name);
        adresse.setText(adress);
        //dateCreation.setText(date);
        owner.setText(pharmacien);
        phone.setText(contact);





    }

   @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       mGoogleMap=googleMap;
        System.out.println("ready");
        intent = getIntent();
       Double latitude = intent.getDoubleExtra("lat", 0.00);
       Double longitude = intent.getDoubleExtra("long", 0.00);
       lat = latitude;
       lon = longitude;
        System.out.println("lat" + this.lat);
        System.out.println("long" + this.lon);
        //Add a marker in Sydney and move the camera
        LatLng pharmacie = new LatLng(lat, lon);
        mGoogleMap.addMarker(new MarkerOptions().position(pharmacie).title("here"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pharmacie));




    }


}