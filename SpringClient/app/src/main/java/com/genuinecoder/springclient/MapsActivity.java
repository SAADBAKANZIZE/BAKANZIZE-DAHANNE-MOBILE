package com.genuinecoder.springclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.genuinecoder.springclient.adapter.PharmacieAdapter;
import com.genuinecoder.springclient.directionHelpers.FetchURL;
import com.genuinecoder.springclient.model.Pharmacie;
import com.genuinecoder.springclient.reotrfit.EmployeeApi;
import com.genuinecoder.springclient.reotrfit.RetrofitService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Context context;
    double distance;
    int i;
    private Circle mCircle;
    String adresse_nearest = "";
    private Spinner spinner_zones, spinner_villes;
    Polyline current_polyline;
    MarkerOptions place1,place2;
    List<MarkerOptions> markerOptions = new ArrayList<>();
    Button search;
    ImageView search_garde_nuit,search_garde_jour,getNearest;


    double radiusInMeters = 100.0;
    int strokeColor = 0xffff0000; //Color Code you want
    int shadeColor = 0x44ff0000; //opaque red fill

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setTitle("Map Location Activity");

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        search = findViewById(R.id.search);
        spinner_zones = findViewById(R.id.spinner_zones);
        spinner_villes = findViewById(R.id.spinner_villes);
        search_garde_jour  = findViewById(R.id.search_garde_jour);
        search_garde_nuit = findViewById(R.id.search_garde_nuit);
        getNearest = findViewById(R.id.search_nearest);
        i = 0;
        context = this.getApplicationContext();
        getNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearest(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString(), mLastLocation.getLatitude(), mLastLocation.getLongitude());

            }
        });
        search_garde_jour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.clear();
                LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude() );
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                mCircle = mGoogleMap.addCircle(addCircle);
                loadPharmaciesByZoneVilleGardeJour(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());
                i = 1;

            }
        });

        search_garde_nuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.clear();
                LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude() );
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                mCircle = mGoogleMap.addCircle(addCircle);
                loadPharmaciesByZoneVilleGardeNuit(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());
                i = 2;

            }
        });
        loadVilles();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleMap.clear();
                LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude() );
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                mCircle = mGoogleMap.addCircle(addCircle);
                loadPharmaciesByZoneVille(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());
            }
        });
        spinner_villes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                loadZone(spinner_villes.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void loadPharmaciesByZoneVille(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVille(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateMapPharmacie(response.body(), mGoogleMap);
                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }


    private void loadPharmaciesByZoneVilleGardeJour(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVilleJour(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateMapPharmacie(response.body(), mGoogleMap);
                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }

    private void getNearest(String zone, String ville, double latitude, double longitude){
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
     System.out.println(i);
     if (i == 0){

         employeeApi.findMinDistance(zone, ville, latitude, longitude)
                 .enqueue(new Callback<Pharmacie>() {
                     @Override
                     public void onResponse(Call<Pharmacie> call, Response<Pharmacie> response) {
                         Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                         System.out.println("response !!! : "+response.body().getAdresse());
                         adresse_nearest = response.body().getAdresse();
                         float[] results = new float[1];
                        Location.distanceBetween(response.body().getLatitude(),response.body().getLongitude(),latitude,longitude,results);
                        float distance = results[0];
                        System.out.println("the distance is "+distance);
                         System.out.println("Nearest is " + response.body().getAdresse());
                         AlertDialog .Builder builder = new AlertDialog.Builder(MapsActivity.this);
                         builder.setMessage( "Near by " + distance/1000 + " Km")
                                 .setIcon(R.drawable.ic_baseline_local_pharmacy_24)
                                 .setTitle(response.body().getAdresse())
                                 .setNegativeButton("show details", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                                        //intent.putExtra("date", response.body().getDateCreation());
                                         intent.putExtra("name", response.body().getNom());
                                         intent.putExtra("pharmacien", response.body().getPharmacien().getNom());
                                         intent.putExtra("contact", response.body().getPharmacien().getEmail());
                                         intent.putExtra("adresse", response.body().getAdresse());
                                         intent.putExtra("lat", response.body().getLatitude());
                                         intent.putExtra("long",response.body().getLongitude());

                                         startActivity(intent);
                                     }
                                 })
                                 .setPositiveButton("show route", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         drawPolyline(latitude, response.body().getLatitude(), longitude, response.body().getLongitude());
                                     }
                                 })
                                 .show();


                     }

                     @Override
                     public void onFailure(Call<Pharmacie> call, Throwable t) {
                         Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                         System.out.println("error is" + t);
                     }
                 });

     }
     else if(i==1){

         employeeApi.findMinDistanceJour(zone, ville, latitude, longitude)
                 .enqueue(new Callback<Pharmacie>() {
                     @Override
                     public void onResponse(Call<Pharmacie> call, Response<Pharmacie> response) {
                         Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                         System.out.println("response !!! : "+response.body().getAdresse());
                         adresse_nearest = response.body().getAdresse();
                         float[] results = new float[1];
                         Location.distanceBetween(response.body().getLatitude(),response.body().getLongitude(),latitude,longitude,results);
                         float distance = results[0];
                         AlertDialog .Builder builder = new AlertDialog.Builder(MapsActivity.this);
                         builder.setMessage( "Near by " + distance/1000 + " Km")
                                 .setIcon(R.drawable.ic_baseline_local_pharmacy_24)
                                 .setTitle(response.body().getAdresse())
                                 .setNegativeButton("show details", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                                         intent.putExtra("date", "date");
                                         intent.putExtra("name", response.body().getNom());
                                         intent.putExtra("pharmacien", response.body().getPharmacien().getNom());
                                         intent.putExtra("contact", response.body().getPharmacien().getEmail());
                                         intent.putExtra("adresse", response.body().getAdresse());
                                         intent.putExtra("lat", response.body().getLatitude());
                                         intent.putExtra("long",response.body().getLongitude());

                                         startActivity(intent);
                                     }
                                 })
                                 .setPositiveButton("show route", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         drawPolyline(latitude, response.body().getLatitude(), longitude, response.body().getLongitude());
                                     }
                                 })
                                 .show();

                     }

                     @Override
                     public void onFailure(Call<Pharmacie> call, Throwable t) {
                         Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                         System.out.println("error is" + t);

                     }
                 });

     }
     else{

         employeeApi.findMinDistanceNuit(zone, ville, latitude, longitude)
                 .enqueue(new Callback<Pharmacie>() {
                     @Override
                     public void onResponse(Call<Pharmacie> call, Response<Pharmacie> response) {
                         Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                         System.out.println("response !!! : "+response.body().getAdresse());
                         adresse_nearest = response.body().getAdresse();
                         float[] results = new float[1];
                         Location.distanceBetween(response.body().getLatitude(),response.body().getLongitude(),latitude,longitude,results);
                         float distance = results[0];
                         AlertDialog .Builder builder = new AlertDialog.Builder(MapsActivity.this);
                         builder.setMessage( "Near by " + distance/1000 + " Km")
                                 .setIcon(R.drawable.ic_baseline_local_pharmacy_24)
                                 .setTitle(response.body().getAdresse())
                                 .setNegativeButton("show details", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         Intent intent = new Intent(MapsActivity.this, DetailsActivity.class);
                                         intent.putExtra("date", "date");
                                         intent.putExtra("name", response.body().getNom());
                                         intent.putExtra("pharmacien", response.body().getPharmacien().getNom());
                                         intent.putExtra("contact", response.body().getPharmacien().getEmail());
                                         intent.putExtra("adresse", response.body().getAdresse());
                                         intent.putExtra("lat", response.body().getLatitude());
                                         intent.putExtra("long",response.body().getLongitude());

                                         startActivity(intent);
                                     }
                                 })
                                 .setPositiveButton("show route", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialogInterface, int i) {
                                         drawPolyline(latitude, response.body().getLatitude(), longitude, response.body().getLongitude());
                                     }
                                 })
                                 .show();

                     }

                     @Override
                     public void onFailure(Call<Pharmacie> call, Throwable t) {
                         Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                         System.out.println("error is" + t);
                     }

                 }

                 );
         /*AlertDialog .Builder builder = new AlertDialog.Builder(this);
         builder.setMessage(this.adresse_nearest)
                 .setTitle("Nearest pharmacie to yours location")
                 .show();*/

     }

    }
    private void drawPolyline(double latitudeStart, double latitudeEnd, double longitudeStart, double longitudeEnd){
        Polyline line =mGoogleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(latitudeStart, longitudeStart), new LatLng(latitudeEnd, longitudeEnd))
                .width(5)
                .color(Color.RED));


    }
    private String getUrl(LatLng origin, LatLng destination, String directionMode){
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "origin=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=" + directionMode;
        String parameter = str_origin + "&" + str_dest + "&" + mode;
        String format = "json";
        String url = "https://maps.googleapis.com/maps/api/direction/" + format + "?"
                + parameter + "AIzaSyAkqPXBoMobJYFE_FXqrZOBh4gVkea59GQ";
        return url;
    }
    private void loadPharmaciesByZoneVilleGardeNuit(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVilleNuit(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateMapPharmacie(response.body(), mGoogleMap);
                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void populateMapPharmacie(List<Pharmacie> pharmacieList, GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        for(int i=0; i<pharmacieList.size();i++){
            Pharmacie position = pharmacieList.get(i);
            mMap.addMarker(new MarkerOptions().position(new
                    LatLng(position.getLatitude(),
                    position.getLongitude())).title(position.getAdresse()) .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.	HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new
                    LatLng(position.getLatitude(),
                    position.getLongitude())));
            //mMap.moveCamera(CameraUpdateFactory.zoomTo(1));
        }
    }
    private void loadVilles() {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findAllVilles()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewVilles(response.body());
                        loadZone(spinner_villes.getSelectedItem().toString());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void loadZone(String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findAllZones(ville)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Toast.makeText(MapsActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewZones(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void populateListViewZones(List<String> zoneList) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,zoneList);
        spinner_zones.setAdapter(stringArrayAdapter);
    }

    private void populateListViewVilles(List<String> zoneList) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,zoneList);
        spinner_villes.setAdapter(stringArrayAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        Intent intent = getIntent();
        Double latitude = intent.getDoubleExtra("lat", 0.00);
        Double longitude = intent.getDoubleExtra("long", 0.00);
        String nom = intent.getStringExtra("name");


        // Add a marker in Sydney and move the camera
        LatLng pharmacie = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(pharmacie).title(nom));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(pharmacie));
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        mCircle = mGoogleMap.addCircle(addCircle);

        //move map camera


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    }