package com.example.niyat.gpsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView textView;
    TextView address;
    TextView distance;
    LocationManager locationManager;
    List<Address> addresses;
    Location oldLocation;
    Geocoder geocoder;
    double distanceTraveled;
    DecimalFormat decimalFormat;
    DecimalFormat decimalFormatLatLong;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.id_textView);
        address = findViewById(R.id.id_address);
        distance = findViewById(R.id.id_distance);
        distanceTraveled = 0;
        decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        decimalFormatLatLong = new DecimalFormat("#.######");


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        geocoder = new Geocoder(this, Locale.US);
        addresses = new ArrayList<>();

    }


    @Override
    public void onLocationChanged(Location location) {
        if(oldLocation == null) {
            oldLocation = location;
        }

        textView.setText("("+ decimalFormatLatLong.format(location.getLatitude()) + ", " + decimalFormatLatLong.format(location.getLongitude())+")");


        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0 ) {
            address.setText(addresses.get(0).getAddressLine(0));
        }
        else{
            address.setText("");
        }

        distanceTraveled+=location.distanceTo(oldLocation)/1609.344;

        distance.setText(decimalFormat.format(distanceTraveled) + " miles");

        oldLocation = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}