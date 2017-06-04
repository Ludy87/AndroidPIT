/*
 * Copyright (c) 2017. by Ludy
 * Die Projekte dienen zur Anschauung und können frei genutzt werden.
 */

package org.astra_g.androidpit.locallocation.interfaces;

import android.location.Location;

public interface LocationListenerInterface {
    void onLocationChanged(Location location);

    void onStatusChanged(String provider);

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);
}
