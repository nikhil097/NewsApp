package com.app.nikhil.newsapp.UI.Activity;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.UI.Fragments.OnlineNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.SavedNewsFragment;
import com.app.nikhil.newsapp.UI.Fragments.TrendingNewsFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{


    BottomNavigationView appBottomNavigationTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                else{

                }


                return true;
            }
        });

        loadFragment(new OnlineNewsFragment());



    }



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomNavigationFragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





}
