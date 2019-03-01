package com.app.nikhil.newsapp.UI.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineNewsFragment extends Fragment {

    ViewPager homeViewPager;
    TabLayout newsCategoryTabs;
    ArrayList<String> tabTitles;
    ApiService apiService;

    public OnlineNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_online_news, container, false);
        homeViewPager=view.findViewById(R.id.homeViewPager);
        newsCategoryTabs=view.findViewById(R.id.newsCategoryTabs);
        tabTitles=new ArrayList<>();
        setupViewPager(homeViewPager);
        newsCategoryTabs.setupWithViewPager(homeViewPager);

        apiService=new ApiService();

        setUpCustomCategoryTabs();

        return  view;

    }




    public void setUpCustomCategoryTabs()
    {
        for(int i=0;i<tabTitles.size();i++) {
            TabLayout.Tab customTab = newsCategoryTabs.getTabAt(i).setCustomView(R.layout.custom_category_news_tab);
        //    customTab.getCustomView().findViewById(R.id.customTabBackgroundImage).setBackground();
            TextView tabTitle = customTab.getCustomView().findViewById(R.id.customTabCategoryTv);
            tabTitle.setText(tabTitles.get(i));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TrendingNewsFragment(), "Trending News");
        tabTitles.add("Trending News");
        adapter.addFragment(new BusinessNewsFrament(),"Business News");
        tabTitles.add("Business News");
        adapter.addFragment(new EntertainmentNewsFragment(),"Entertainment News");
        tabTitles.add("Entertainment News");
        adapter.addFragment(new GeneralNewsFragment(),"General News");
        tabTitles.add("General News");
        adapter.addFragment(new HealthNewsFragment(),"Health News");
        tabTitles.add("Health News");
        adapter.addFragment(new ScienceNewsFragment(),"Science News");
        tabTitles.add("Science News");
        adapter.addFragment(new SportsNewsFragment(),"Sports News");
        tabTitles.add("Sports News");
        adapter.addFragment(new TechnologyNewsFragment(),"Technology News");
        tabTitles.add("Technology News");
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new RotateUpTransformer());
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
