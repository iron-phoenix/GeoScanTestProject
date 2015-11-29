package com.kluchikhin.geoscantestproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_motion:
                try {
                    animator.animate();
                } catch (NotEnoughPointsException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_clear:
                reset();
                break;
        }
        return true;
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mMap != null) {
                setUpMap();
                animator = new MotionAnimator(mMap, polyLine, new SimpleLinearInterpolator());
            }
        }
    }

    private void setUpMap() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.addMarker(new MarkerOptions().position(point));
                updatePolyLine(point);
            }
        });

        initPolyline();
    }

    private void initPolyline() {
        PolylineOptions rectOptions = new PolylineOptions();
        polyLine = mMap.addPolyline(rectOptions);
    }

    private void reset() {
        if (mMap != null) {
            mMap.clear();
            initPolyline();
            animator = new MotionAnimator(mMap, polyLine, new SimpleLinearInterpolator());
        }
    }

    private void updatePolyLine(LatLng latLng) {
        List<LatLng> points = polyLine.getPoints();
        points.add(latLng);
        polyLine.setPoints(points);
    }

    private GoogleMap mMap;
    private Polyline polyLine;
    private MotionAnimator animator;
}
