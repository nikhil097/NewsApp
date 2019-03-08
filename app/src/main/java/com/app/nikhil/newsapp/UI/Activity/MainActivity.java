package com.app.nikhil.newsapp.UI.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.UI.Fragments.OflineWarningFragment;
import com.app.nikhil.newsapp.UI.Fragments.OnlineFragments.OnlineNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.SavedNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.SearchNewsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Locale;


public class MainActivity extends AppCompatActivity{


    BottomNavigationView appBottomNavigationTabs;
    private Boolean firstTime=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchUserPresentCountry();

        if(checkFirstTimeLaunch())
        {
            SharedPreferences mPreferences = this.getSharedPreferences("NewsDB", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putString("userCountry",fetchUserPresentCountry());

            subscribeDeviceForNews();

        }


        appBottomNavigationTabs=findViewById(R.id.appBottomNavigationTabs);

        appBottomNavigationTabs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.onlineNews)
                {
                    if(checkInternetConnection()) {
                        loadFragment(new OnlineNewsFragment());
                    }
                    else {
                        loadFragment(new OflineWarningFragment());
                    }
                    menuItem.setIcon(R.drawable.home_menu_item);
                    MenuItem item=appBottomNavigationTabs.getMenu().findItem(R.id.savedNews);
                    item.setIcon(R.drawable.icons8_tv_off_100);
                    MenuItem item1=appBottomNavigationTabs.getMenu().findItem(R.id.searchNews);
                    item1.setIcon(R.drawable.search_menu_item);
                }
                else if(menuItem.getItemId()==R.id.savedNews)
                {
                    loadFragment(new SavedNewsFragment());
                    menuItem.setIcon(R.drawable.offline_news_menu);
                    MenuItem item=appBottomNavigationTabs.getMenu().findItem(R.id.onlineNews);
                    item.setIcon(R.drawable.home_menu_item1);
                    MenuItem item1=appBottomNavigationTabs.getMenu().findItem(R.id.searchNews);
                    item1.setIcon(R.drawable.search_menu_item);
                }
                else if(menuItem.getItemId()==R.id.searchNews){
                    if(checkInternetConnection()) {
                        loadFragment(new SearchNewsFragment());
                    }
                    else {
                        loadFragment(new OflineWarningFragment());
                    }
                    menuItem.setIcon(R.drawable.search_menu_item1);
                    MenuItem item=appBottomNavigationTabs.getMenu().findItem(R.id.onlineNews);
                    item.setIcon(R.drawable.home_menu_item1);
                    MenuItem item1=appBottomNavigationTabs.getMenu().findItem(R.id.savedNews);
                    item1.setIcon(R.drawable.icons8_tv_off_100);
                }


                return true;
            }
        });

        if (checkInternetConnection()) {
            loadFragment(new OnlineNewsFragment());
        }
        else {
            loadFragment(new OflineWarningFragment());
        }


    }


    public void subscribeDeviceForNews()
    {
        SharedPreferences mPreferences = this.getSharedPreferences("NewsDB", Context.MODE_PRIVATE);
        String countryName = mPreferences.getString("userCountry", "in");
        FirebaseMessaging.getInstance().subscribeToTopic(countryName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (!task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Unable to subscribe news in your country", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.v("err1","done");
                }

            }
        });


    }


    public boolean checkInternetConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    public boolean checkFirstTimeLaunch()
    {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("NewsDB", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }


    public String fetchUserPresentCountry()
    {
        final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkCountry = tm.getNetworkCountryIso();
        if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
            String countryCode = networkCountry.toLowerCase(Locale.US);
            Log.v("code1",countryCode);

            return countryCode;
        }
        return null;
    }




    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.disallowAddToBackStack();
        transaction.replace(R.id.bottomNavigationFragmentContainer, fragment);
        transaction.commit();
    }





}
