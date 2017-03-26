package com.example.user.app_matnas;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_contactUs extends AppCompatActivity implements OnMapReadyCallback {

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

    public void onClickMail(View view) {
        Uri uri = Uri.parse("mailto:matnaspat@fannykaplan.org");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void onClickFacebook(View view) {
        Uri uri = Uri.parse("http://m.facebook.com/matnaspat");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void onClickWhatsapp(View view) {
        Uri uri = Uri.parse("https://chat.whatsapp.com/EvtmaQvQRQc9WLMjwd9ihv");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

    public void onClickPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:026782858"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


    }

    public void onClickFacebookMessenger(View view) {
        Uri uri = Uri.parse("http://m.me/matnaspat");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }

}
