package com.app.nikhil.newsapp.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.nikhil.newsapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmentNewsFragment extends Fragment {


    public EntertainmentNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entertainment_news, container, false);
    }

}
