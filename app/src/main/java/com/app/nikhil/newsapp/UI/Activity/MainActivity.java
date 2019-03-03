package com.app.nikhil.newsapp.UI.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;

import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.UI.Fragments.OnlineFragments.OnlineNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.SavedNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.SearchNewsFragment;

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
        }


        appBottomNavigationTabs=findViewById(R.id.appBottomNavigationTabs);

        appBottomNavigationTabs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.onlineNews)
                {
                    loadFragment(new OnlineNewsFragment());
                }
                else if(menuItem.getItemId()==R.id.savedNews)
                {
                    loadFragment(new SavedNewsFragment());
                }
                else if(menuItem.getItemId()==R.id.searchNews){
                    loadFragment(new SearchNewsFragment());

                }


                return true;
            }
        });

        loadFragment(new OnlineNewsFragment());



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
        transaction.replace(R.id.bottomNavigationFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
