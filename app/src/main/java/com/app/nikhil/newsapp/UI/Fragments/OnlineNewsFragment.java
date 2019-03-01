package com.app.nikhil.newsapp.UI.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.app.nikhil.newsapp.NewsResponseBody.TopHeadlinesResponse;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineNewsFragment extends Fragment {

    ViewPager homeViewPager;
    TabLayout newsCategoryTabs;
    ArrayList<String> tabTitles;
    ArrayList<String> tabImageUrls;
    ApiService apiService;

    int counter=0;
    String ImagesUrl=null;

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
        tabImageUrls=new ArrayList<>();
        setupViewPager(homeViewPager);
        newsCategoryTabs.setupWithViewPager(homeViewPager);

        apiService=new ApiService();

        fetchUrlCustomTabBackgroundImage();

        return  view;

    }


    public void fetchUrlCustomTabBackgroundImage() {

     //   tabImageUrls.add("");

        counter=0;

        for (int i = 0; i < tabTitles.size(); i++) {
            SharedPreferences mPreferences = getActivity().getSharedPreferences("NewsDB", Context.MODE_PRIVATE);
            String countryCode = mPreferences.getString("userCountry", "in");
            final int finalI = i;
            String category="";
            if(i!=0)
            {
                category=tabTitles.get(i);
            }
            apiService.getTopHeadlines(ApiCredentals.API_KEY, countryCode, category, "", 1, new ResponseCallback<TopHeadlinesResponse>() {
                @Override
                public void success(TopHeadlinesResponse topHeadlinesResponse) {

                    counter++;

                    List<Article> articles = topHeadlinesResponse.getArticles();
                    int totalResults = topHeadlinesResponse.getTotalResults();
                    if (totalResults != 0) {
                        tabImageUrls.add(articles.get(0).getUrlToImage());
                        ImagesUrl+=articles.get(0).getUrlToImage()+"\n";
                    }
                    else{
                        tabImageUrls.add("");
                    }
                    if (counter ==tabTitles.size())
                    {
                        Log.v("sabkeurl",ImagesUrl);
                        setUpCustomCategoryTabs();
                    }
                }

                @Override
                public void failure(TopHeadlinesResponse topHeadlinesResponse) {

                }
            });
        }

    //    setUpCustomCategoryTabs();

    }
    public void setUpCustomCategoryTabs()
    {
        for(int i=0;i<tabTitles.size();i++) {
            TabLayout.Tab customTab = newsCategoryTabs.getTabAt(i).setCustomView(R.layout.custom_category_news_tab);
        //    customTab.getCustomView().findViewById(R.id.customTabBackgroundImage).setBackground();
            TextView tabTitle = customTab.getCustomView().findViewById(R.id.customTabCategoryTv);
            tabTitle.setText(tabTitles.get(i));
            ImageView tabView=customTab.getCustomView().findViewById(R.id.customTabBackgroundImage);
            Glide.with(getActivity()).load(tabImageUrls.get(i)).into(tabView);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TrendingNewsFragment(), "Trending News");
        tabTitles.add("Trending");
        adapter.addFragment(new BusinessNewsFrament(),"Business News");
        tabTitles.add("Business");
        adapter.addFragment(new EntertainmentNewsFragment(),"Entertainment News");
        tabTitles.add("Entertainment");
        adapter.addFragment(new GeneralNewsFragment(),"General News");
        tabTitles.add("General");
        adapter.addFragment(new HealthNewsFragment(),"Health News");
        tabTitles.add("Health");
        adapter.addFragment(new ScienceNewsFragment(),"Science News");
        tabTitles.add("Science");
        adapter.addFragment(new SportsNewsFragment(),"Sports News");
        tabTitles.add("Sports");
        adapter.addFragment(new TechnologyNewsFragment(),"Technology News");
        tabTitles.add("Technology");
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
