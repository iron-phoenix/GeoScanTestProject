package com.kluchikhin.geoscantestproject;

import com.google.android.gms.maps.model.LatLng;

public interface CoordInterpolator {
    LatLng interpolate(float fraction, LatLng a, LatLng b);
}
