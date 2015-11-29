package com.kluchikhin.geoscantestproject;

import com.google.android.gms.maps.model.LatLng;

public class SimpleLinearInterpolator implements CoordInterpolator {
    @Override
    public LatLng interpolate(float fraction, LatLng a, LatLng b) {
        double lat = (b.latitude - a.latitude) * fraction + a.latitude;

        double lngDelta = b.longitude - a.longitude;
        if (Math.abs(lngDelta) > 180) {
            lngDelta -= Math.signum(lngDelta) * 360;
        }
        double lng = lngDelta * fraction + a.longitude;

        return new LatLng(lat, lng);
    }
}
