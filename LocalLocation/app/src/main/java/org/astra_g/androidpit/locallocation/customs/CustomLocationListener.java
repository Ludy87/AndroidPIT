/*
 * Copyright (c) 2017. by Ludy
 * Die Projekte dienen zur Anschauung und können frei genutzt werden.
 */

package org.astra_g.androidpit.locallocation.customs;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import org.astra_g.androidpit.locallocation.interfaces.LocationListenerInterface;


public class CustomLocationListener implements LocationListener {

    private LocationListenerInterface listenerInterface;

    public CustomLocationListener(LocationListenerInterface locationListenerInterface) {
        this.listenerInterface = locationListenerInterface;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.listenerInterface.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.listenerInterface.onStatusChanged(provider);

    }

    @Override
    public void onProviderEnabled(String provider) {
        this.listenerInterface.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        this.listenerInterface.onProviderDisabled(provider);
    }
}
