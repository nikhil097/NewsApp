package com.app.nikhil.newsapp.UI.Fragments.OnlineFragments;


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
import com.app.nikhil.newsapp.Pojo.Tab;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;
import com.bumptech.glide.Glide;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<Tab> tabsList;

    FragmentManager childFragmentManager;

    RotateLoading fectNewsProgress;


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
        newsCategoryTabs.setSelectedTabIndicator(getResources().getDrawable(android.R.color.holo_blue_dark));
        newsCategoryTabs.setupWithViewPager(homeViewPager);

        apiService=new ApiService();

        childFragmentManager=getChildFragmentManager();

        fectNewsProgress=view.findViewById(R.id.fetchNewsDataProgress);
        fectNewsProgress.setLoadingColor(getResources().getColor(R.color.colorPrimary));
        fectNewsProgress.start();

        populateTabsList();

        fetchUrlCustomTabBackgroundImage();

        return  view;

    }


    public void fetchUrlCustomTabBackgroundImage() {


        counter=0;

        tabsList = new ArrayList<>();

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
                        tabsList.add(new Tab(tabTitles.get(finalI),articles.get(0).getUrlToImage(),finalI));
                    }
                    else{
                        tabImageUrls.add("");
                    }
                    if (counter ==tabTitles.size())
                    {
                        Log.v("imageurl",ImagesUrl);
                        setUpCustomCategoryTabs();
                    }
                }

                @Override
                public void failure(TopHeadlinesResponse topHeadlinesResponse) {

                }
            });
        }

    }
    public void setUpCustomCategoryTabs()
    {
        setupViewPager(homeViewPager);

        fectNewsProgress.stop();

        Collections.sort(tabsList);
        for(Tab tab:tabsList)
        {
            Log.v("tab1",tab.getId()+" "+tab.getCategoryName());
        }
        for(int i=0;i<tabTitles.size();i++) {
            final TabLayout.Tab customTab = newsCategoryTabs.getTabAt(i);

            customTab.setCustomView(R.layout.custom_category_news_tab);

        //    customTab.getCustomView().findViewById(R.id.customTabBackgroundImage).setBackground();
            TextView tabTitle = customTab.getCustomView().findViewById(R.id.customTabCategoryTv);
            tabTitle.setText(tabsList.get(i).getCategoryName());
            ImageView tabView=customTab.getCustomView().findViewById(R.id.customTabBackgroundImage);

            if(isAdded()) {
                Glide.with(getActivity()).load(tabsList.get(i).getUrlToFirstPostImage()).into(tabView);
            }
          //  customTab.setIcon(getResources().getDrawable(R.drawable.ic_launcher_background));



        }
    }

    public void populateTabsList()
    {
        tabTitles.add("Trending");
        tabTitles.add("Business");
        tabTitles.add("Entertainment");
        tabTitles.add("General");
        tabTitles.add("Health");
        tabTitles.add("Science");
        tabTitles.add("Sports");
        tabTitles.add("Technology");

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(childFragmentManager);
        adapter.addFragment(TrendingNewsFragment.newInstance(""), "Trending");
        adapter.addFragment(TrendingNewsFragment.newInstance("Business"),"Business");
        adapter.addFragment(TrendingNewsFragment.newInstance("Entertainment"),"Entertainment");
        adapter.addFragment(TrendingNewsFragment.newInstance("General"),"General");
        adapter.addFragment(TrendingNewsFragment.newInstance("Health"),"Health");
        adapter.addFragment(TrendingNewsFragment.newInstance("Science"),"Science");
        adapter.addFragment(TrendingNewsFragment.newInstance("Sports"),"Sports");
        adapter.addFragment(TrendingNewsFragment.newInstance("Technology"),"Technology");
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
