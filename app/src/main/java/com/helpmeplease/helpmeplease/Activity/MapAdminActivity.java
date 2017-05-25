package com.helpmeplease.helpmeplease.Activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.helpmeplease.helpmeplease.R;

public class MapAdminActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TrackGPS gps;
    GoogleMap mMap;
    Marker mMarker;
    double lat, lng;
    SupportMapFragment mapFragment;
    String currentLocation;
    private SharedPreferences sp;
    private final String P_NAME = "Helpme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);

        gps = new TrackGPS(MapAdminActivity.this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CurrentLocation(){
        currentLocation = sp.getString("location","");


        String[] arr = currentLocation.split(",");

        Log.i("mylog",arr[0]);
        Log.i("mylog",arr[1]);
        Double Latitude = Double.valueOf(arr[0]);
        Double Longitude = Double.valueOf(arr[1]);
        if (mMarker != null) {
            mMarker.remove();
        }
        if (gps.canGetLocation()) {
            lat = gps.getLatitude();
            lng = gps.getLongitude();
            Toast.makeText(MapAdminActivity.this, "Longitude:" + Double.toString(lng)
                    + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(Longitude,Latitude);


            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, 16));

        } else {
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        CurrentLocation();
    }
}
