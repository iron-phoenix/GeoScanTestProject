package com.kluchikhin.geoscantestproject;

import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class MotionAnimator {
    public MotionAnimator(GoogleMap googleMap, Polyline polyLine, CoordInterpolator coordInterpolator) {
        this.googleMap = googleMap;
        this.polyLine = polyLine;
        this.coordInterpolator = coordInterpolator;
    }

    public void animate() throws NotEnoughPointsException {
        if (polyLine.getPoints().size() < 2) {
            throw new NotEnoughPointsException("Not enough points for motion");
        }

        final Marker trackMarker = googleMap.addMarker(new MarkerOptions().position(polyLine.getPoints().get(0)));
        trackMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.plane_flight_icon));

        handler.post(new Runnable() {

            @Override
            public void run() {
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                trackMarker.setPosition(coordInterpolator.interpolate(v,
                        polyLine.getPoints().get(currentPoint - 1),
                        polyLine.getPoints().get(currentPoint)));

                if (t < 1) {
                    handler.postDelayed(this, 16);
                } else {
                    if (++currentPoint != polyLine.getPoints().size()) {
                        start = SystemClock.uptimeMillis();
                        durationInMs = getDistance(polyLine.getPoints().get(currentPoint - 1), polyLine.getPoints().get(currentPoint)) / 1000;
                        handler.postDelayed(this, 16);
                    } else {
                        trackMarker.remove();
                    }
                }
            }

            private long elapsed;
            private float t;
            private float v;
            private int currentPoint = 1;
            private long start = SystemClock.uptimeMillis();
            private float durationInMs = getDistance(polyLine.getPoints().get(0), polyLine.getPoints().get(1)) / 1000;
        });
    }

    private static float getDistance(LatLng point1, LatLng point2) {
        float[] results = new float[1];
        Location.distanceBetween(point1.latitude, point1.longitude,
                point2.latitude, point2.longitude,
                results);
        return results[0];
    }

    private final GoogleMap googleMap;
    private final Polyline polyLine;
    private final CoordInterpolator coordInterpolator;

    private final Handler handler = new Handler();
    private final Interpolator interpolator = new LinearInterpolator();
}
