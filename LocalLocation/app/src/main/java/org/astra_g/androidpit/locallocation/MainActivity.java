/*
 * Copyright (c) 2017. by Ludy
 * Die Projekte dienen zur Anschauung und können frei genutzt werden.
 */

package org.astra_g.androidpit.locallocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.astra_g.androidpit.locallocation.customs.CustomLocationListener;
import org.astra_g.androidpit.locallocation.interfaces.CustomLocationListenerInterface;


public class MainActivity extends Activity {

    private LocationManager locationManager;
    private CustomLocationListener customLocationListener;
    private TextView textViewHinweis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewHinweis = findViewById(R.id.textViewHinweis);
        textViewHinweis.setText(getString(R.string.loading));
        customLocationListener = new CustomLocationListener(locationListener);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, customLocationListener);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(customLocationListener);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, customLocationListener);
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private CustomLocationListenerInterface locationListener = new CustomLocationListenerInterface() {
        @Override
        public void onLocationChanged(Location location) {
            textViewHinweis.setText(String.format(getString(R.string.result_text_view),
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()), String.valueOf(location.getAccuracy())));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(getBaseContext(), getString(R.string.gps_not_show),
                    Toast.LENGTH_SHORT).show();
        }
    };

}
