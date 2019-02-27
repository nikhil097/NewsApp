package com.app.nikhil.newsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;
import com.app.nikhil.newsapp.Rest.ApiService;

import com.app.nikhil.newsapp.Rest.ResponseCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements LocationListener {

    ApiService apiService;
    LocationManager locationManager;

    boolean checkForLocation=true;

    private static final String API_KEY="5f13303ba2034ca5ab641e4849ac9b55";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     apiService=new ApiService();

     locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

     apiService.getTopHeadlines(API_KEY, "in", new ResponseCallback<TopHeadlinesResponse>() {
         @Override
         public void success(TopHeadlinesResponse topHeadlinesResponse) {

         }

         @Override
         public void failure(TopHeadlinesResponse topHeadlinesResponse) {

         }
     });

     checkLocationPermission();






    }


    public void fetchTrendingNews(String countryCode)
    {
        
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Allow Location Access")
                        .setMessage("Please allow us to access your location so that we can provide news according to your country.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            onPermissionsGranted();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                       onPermissionsGranted();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    @SuppressLint("MissingPermission")
    public void onPermissionsGranted()
    {
        provider = locationManager.getBestProvider(new Criteria(), true);

        //Request location updates:
        locationManager.requestLocationUpdates(provider,400,1,MainActivity.this);





    }


    public String fetchCurrentCountry(long lat,long lng)
    {
        String countryName = null;

        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0)
        {
            countryName=addresses.get(0).getCountryName();
        }
        return  countryName;
    }



    public String getCountryCode(String countryName) {

        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            Locale locale = new Locale("", code);
            // Get country name for each code.
            String name = locale.getDisplayCountry();
            // Map all country names and codes in key - value pairs.
            countryMap.put(name, code);
        }
        // Get the country code for the given country name using the map.
        // Here you will need some validation or better yet
        // a list of countries to give to user to choose from.
        String countryCode = countryMap.get(countryName); // "NL" for Netherlands.

        return countryCode;

    }



    @Override
    public void onLocationChanged(Location location) {

        if(checkForLocation) {
            long lat = (long) location.getLatitude();
            long lng = (long) location.getLongitude();
            String countryName = fetchCurrentCountry(lat, lng);

            Log.v("countryCode", getCountryCode(countryName));
            fetchTrendingNews(getCountryCode(countryName));
            checkForLocation=false;
        }
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
