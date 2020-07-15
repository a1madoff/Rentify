package com.example.rentingapp;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap map;

    List<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // START RECYCLERVIEW
        RecyclerView rvMapListings = findViewById(R.id.rvMapListings);
        listings = new ArrayList<>();
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());

        ListingsAdapter adapter = new ListingsAdapter(this, listings);
        rvMapListings.setAdapter(adapter);
        rvMapListings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // END RECYCLERVIEW
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setBackground(ContextCompat.getDrawable(this, R.drawable.text_box));
        Bitmap bitmap = iconGenerator.makeIcon("$29");
        // Uses BitmapDescriptorFactory to create the marker
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("$29")
                .icon(icon));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(sydney);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setStyle(3); // sets text to white
        iconGenerator.setBackground(ContextCompat.getDrawable(this, R.drawable.text_box_color_accent));
        Bitmap bitmap = iconGenerator.makeIcon(marker.getTitle());
        // Uses BitmapDescriptorFactory to create the marker
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        marker.setIcon(icon);

//        Toast.makeText(MapsActivity.this, "Marker clicked", Toast.LENGTH_SHORT).show();
        return true;
    }
}