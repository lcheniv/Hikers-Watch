package com.ivchen.hikerswatchapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    TextView latitudeTV;
    TextView longitudeTV;
    TextView altitudeTV;
    TextView accuracyTV;
    TextView speedTV;
    TextView bearingTV;
    TextView addressTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTV = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTV = (TextView) findViewById(R.id.longitudeTextView);
        altitudeTV = (TextView) findViewById(R.id.altitudeTextView);
        accuracyTV = (TextView) findViewById(R.id.accuracyTextView);
        speedTV = (TextView) findViewById(R.id.speedTextView);
        bearingTV = (TextView) findViewById(R.id.bearingTextView);
        addressTV = (TextView) findViewById(R.id.addressTextView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        // don't have to get a location update to get values

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        //onLocationChanged(location);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Double altitude = location.getAltitude();
        float bearing = location.getBearing();
        float speed = location.getSpeed();
        float accuracy = location.getAccuracy();

        // getapcontext and locale.getdefault
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        // Get latitude and longitude and only want the first one
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if(addressList != null && addressList.size() > 0){
                // Get the first address list value and turn it into a string
                Log.i("Place information:", addressList.get(0).toString());

                String addressHolder = "";

                for(int i = 0; i <= addressList.get(0).getMaxAddressLineIndex(); i++){
                    // Getting the ith line each time
                    addressHolder += addressList.get(0).getAddressLine(i) + "\n";
                }

                addressTV.setText("Address: \n" + addressHolder);
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        latitudeTV.setText("Latitude: " + String.valueOf(latitude));
        longitudeTV.setText("Longitude: " + String.valueOf(longitude));
        altitudeTV.setText("Altitude: " + String.valueOf(altitude) + " m");
        bearingTV.setText("Bearing: " + String.valueOf(bearing));
        speedTV.setText("Speed: " + String.valueOf(speed) + " m/s");
        accuracyTV.setText("Accuracy: " + String.valueOf(accuracy) + " m");

        Log.i("Latitude: ", String.valueOf(latitude));
        Log.i("Longitude: ", String.valueOf(longitude));
        Log.i("Altitude: ", String.valueOf(altitude));
        Log.i("Bearing: ", String.valueOf(bearing));
        Log.i("Speed: ", String.valueOf(speed));
        Log.i("Accuracy: ", String.valueOf(accuracy));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
