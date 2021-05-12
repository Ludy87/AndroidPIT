/*
 * Copyright (c) 2021 by Ludy
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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

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
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
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
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
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
            switch (android.os.Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.ICE_CREAM_SANDWICH:        // 14
                case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:    // 15
                case Build.VERSION_CODES.JELLY_BEAN:                // 16
                case Build.VERSION_CODES.JELLY_BEAN_MR1:            // 17
                case Build.VERSION_CODES.JELLY_BEAN_MR2:            // 18
                case Build.VERSION_CODES.KITKAT:                    // 19
                case Build.VERSION_CODES.KITKAT_WATCH:              // 20
                case Build.VERSION_CODES.LOLLIPOP:                  // 21
                case Build.VERSION_CODES.LOLLIPOP_MR1:              // 22
                case Build.VERSION_CODES.M:                         // 23
                case Build.VERSION_CODES.N:                         // 24
                case Build.VERSION_CODES.N_MR1:                     // 25
                case Build.VERSION_CODES.O:                         // 26
                case Build.VERSION_CODES.O_MR1:                     // 27
                case Build.VERSION_CODES.P:                         // 28
                case Build.VERSION_CODES.Q:                         // 29
                case Build.VERSION_CODES.R:                         // 30
                default:
                    textViewHinweis.setText("no support Device");
                    break;
            }
            // API 17 - API 25
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                textViewHinweis.setText(
                        String.format(getString(R.string.result_text_view),
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()),
                                String.valueOf(location.getAccuracy()),
                                String.valueOf(location.getSpeed()) // ToDo falscher Wert
                        )
                );
            }
            // API 26 - API 30
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textViewHinweis.setText(
                        String.format(getString(R.string.result_text_view),
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()),
                                String.valueOf(location.getAccuracy()),
                                String.valueOf(location.getVerticalAccuracyMeters()))
                );
            }
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
