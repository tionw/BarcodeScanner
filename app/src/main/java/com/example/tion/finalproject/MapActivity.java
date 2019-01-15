package com.example.tion.finalproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private LatLng myLocation;
    private LatLng latLng;
    private double lat1,lon1;
    private Product eq;
    private String name;  //= "unknown country";
    private String country;
    private String link;
    private String rl;
    public static double lat, lng;
    Geocoder geocoder = null;
    ArrayList<String> locations; //will contain all the locations
    public static ArrayList<Location2> locas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();


        geocoder = new Geocoder(this);
        locations = new ArrayList<String>();

        // This fetches the addresses from a bundle and places them in an ArrayList
        // ArrayList will be used later by GeoCoder
        Intent arts = getIntent();
        Bundle bundle = arts.getExtras();

        eq = (Product) bundle.getSerializable("city");

        rl = eq.getLink();
        link = eq.getDomain();
        String pk = link.replace(".com","");
        String cap = pk.substring(0, 1).toUpperCase() + pk.substring(1);
        actionBar.setTitle(cap+" Locations Near You");
        actionBar.setDisplayHomeAsUpEnabled(true);  //helps with returning to our MainActivity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        //gets the maps to load
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
        mf.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("1. here here in go back");
                Intent intent = new Intent(this, SavedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.out.println("2. here here in go back");
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {// map is loaded but not laid out yet
        this.map = map;
        map.setOnMapLoadedCallback(this);      // calls onMapLoaded when layout done
        UiSettings mapSettings;
        mapSettings = map.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
    }

    // maps are loaded and this is where I should perform the getMoreInfo() to grab more data
    //note use of geocoder.getFromLocationName() to find LonLat from address
    @Override
    public void onMapLoaded() {
        // code to run when the map has loaded
        getMoreInfo(); // call this --> use a geoCoder to find the location of the eq

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "Opening site...", Toast.LENGTH_LONG).show();
                String url = "http://";
                String final_url = url + link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(final_url));
                startActivity(i);
            }
        });
    }

    public void getMoreInfo() {
        for (int i=0; i<locas.size(); i++) {
            String open = null;
            lat1 = locas.get(i).getLat();
            lon1 = locas.get(i).getLon();
            System.out.println("in getMoreInfo " + lat1 + " " + lon1);
            latLng = new LatLng(lat1, lon1);  //used in addMarker below for placing a marker at the Longitude/Latitude spot
            if (locas.get(i).getLink()){
                open = "Yes";
            }
            else{
                open = "No";
            }
            // puts marker icon at location
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(String.valueOf(locas.get(i).getName()))
                    .snippet("Open Now: " + open)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        latLng = new LatLng(42.333193,-71.174143);
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(String.valueOf("Current Location"))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12), 2500, null);
    }
}
