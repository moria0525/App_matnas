package com.example.user.app_matnas;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_contactUs extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private float zoomLevel = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in matnas and move the camera
        LatLng LocationMatnas = new LatLng(31.749968, 35.200593);
        mMap.addMarker(new MarkerOptions().position(LocationMatnas).title("מרכז קהילתי פאני קפלן פת"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LocationMatnas, zoomLevel));
    }
}
