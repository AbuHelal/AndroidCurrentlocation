package com.kalid.getcurrentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Button btLocation;
    ProgressBar progressBar;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView tvLatitude;
    TextView tvLongitude;
    private Location location;
    private LocationManager locationManager;
    private LocationCallback locationCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btLocation = (Button) findViewById(R.id.bt_location);
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        progressBar =(ProgressBar) findViewById(R.id.simpleProgressBar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        btLocation.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_FINE_LOCATION") == 0 && ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                progressBar.setVisibility(View.VISIBLE);

                getCurrentLocation();




            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 100);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] + grantResults[1] == 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_LONG).show();
        }
    }

    private void getCurrentLocation() {
     /*   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            this.fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @SuppressLint("MissingPermission")
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public void onComplete(Task<Location> task) {
                    location = task.getResult();
                    if (location != null) {
                      tvLatitude.setText(String.valueOf(location.getLatitude()));
                      tvLongitude.setText(String.valueOf(location.getLongitude()));
                        return;
                    }

                   fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setPriority(100).setInterval(10000).setFastestInterval(1000).setNumUpdates(1), new LocationCallback() {

                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            Location location1 = locationResult.getLastLocation();
                            tvLatitude.setText(String.valueOf(location1.getLatitude()));
                            tvLongitude.setText(String.valueOf(location1.getLongitude()));
                        }
                    }, Looper.myLooper());
                }
            });
        }
    */
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 2, MainActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            progressBar.setVisibility(View.INVISIBLE);

            tvLatitude.setText(String.valueOf(location.getLatitude()));
            tvLongitude.setText(String.valueOf(location.getLongitude()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
