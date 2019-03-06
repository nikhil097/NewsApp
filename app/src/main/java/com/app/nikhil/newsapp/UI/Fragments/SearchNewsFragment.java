package com.app.nikhil.newsapp.UI.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.nikhil.newsapp.Adapter.SourcesAdapter;
import com.app.nikhil.newsapp.Adapter.TrendingNewsAdapter;
import com.app.nikhil.newsapp.NewsResponseBody.SearchNewsResponseBody;
import com.app.nikhil.newsapp.NewsResponseBody.SourcesResponse;
import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.Pojo.NewsSource;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.ApiCredentals;
import com.app.nikhil.newsapp.Rest.ApiService;
import com.app.nikhil.newsapp.Rest.ResponseCallback;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.app.nikhil.newsapp.UI.Activity.ArticleDetailActivity;
import com.chootdev.recycleclick.RecycleClick;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

public class SearchNewsFragment extends Fragment {

    EditText searchNewsEt;
    ApiService apiService;


    RecyclerView searchNewsRv;
    TrendingNewsAdapter searchNewsAdapter;

    String sourcesSelectedString="";

    RotateLoading searchFragmentLoadingProgress;

    Button searchNewsFilterBtn;

    ArrayList<NewsSource> sources;
    Spinner sortBySpinner;
    String sortByQuery;


    ArrayList<Article> savedArticlesList;
    private int selectedSortByMode=0;

    public SearchNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search_news, container, false);
        apiService=new ApiService();

        searchFragmentLoadingProgress=view.findViewById(R.id.loadingProgressSearch);
        searchFragmentLoadingProgress.setLoadingColor(getResources().getColor(android.R.color.holo_blue_dark));
        searchFragmentLoadingProgress.start();

        fetchSources();

        searchNewsEt=view.findViewById(R.id.searchNewsET);
        searchNewsRv=view.findViewById(R.id.resultNewsRv);

        searchNewsFilterBtn=view.findViewById(R.id.filterNewsBtn);

        savedArticlesList=new ArrayList<>();
        fetchSavedNewsFromDatabase();

        searchNewsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 3) {
                   fetchResults("","");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchNewsFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFilterAlertDialog(sources);
            }
        });


        return view;
    }


    public void fetchResults(String sources,String sortBy)
    {
        Log.v("queries",sortBy+" "+sources);

        apiService.getSearchNewsResults(ApiCredentals.API_KEY, searchNewsEt.getText().toString(), sourcesSelectedString, sortByQuery, "en", 1, 20, new ResponseCallback<SearchNewsResponseBody>() {
            @Override
            public void success(SearchNewsResponseBody searchNewsResponseBody) {

                List<Article> articles=searchNewsResponseBody.getArticles();

                int totalResults=searchNewsResponseBody.getTotalResults();

                if(totalResults>20)
                {
                    totalResults=20;
                }

                searchNewsFilterBtn.setVisibility(View.VISIBLE);

                ArrayList<Article> trendingArticlesList=new ArrayList<>();

                for(int i=0;i<totalResults;i++)
                {

                    if(checkIfArticleAlreadySaved(articles.get(i)))
                    {
                        articles.get(i).setIsSaved(true);
                    }
                    trendingArticlesList.add(articles.get(i));
                }

                populateTrendingNewsView(trendingArticlesList);

            }

            @Override
            public void failure(SearchNewsResponseBody searchNewsResponseBody) {

            }
        });
    }




    public void displayFilterAlertDialog(final ArrayList<NewsSource> sources)
    {
        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Filter Results");
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.filter_search_results,null,false);


        RecyclerView sourcesRv=view.findViewById(R.id.sourcesRv);

        SourcesAdapter sourcesAdapter=new SourcesAdapter(getActivity(),sources);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        sourcesRv.setLayoutManager(mLayoutManager);
        sourcesRv.setItemAnimator(new DefaultItemAnimator());
        sourcesRv.setAdapter(sourcesAdapter);


        RecycleClick.addTo(sourcesRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                CheckBox sourceStateCB=v.findViewById(R.id.sourceStateCB);
                sources.get(position).setIschecked(!sources.get(position).isIschecked());
                sourceStateCB.setChecked(!sourceStateCB.isChecked());
            }
        });

        sortBySpinner=view.findViewById(R.id.sortBySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter(
                getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sortBy));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setSelection(selectedSortByMode);

        alertDialog.setView(view);
        sourcesSelectedString="";

        alertDialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int noOfSelectedSourcesCounter=0;

             for(int i=0;i<sources.size();i++)
             {
                 if(sources.get(i).isIschecked())
                 {
                     sourcesSelectedString+=sources.get(i).getId()+",";
                     noOfSelectedSourcesCounter++;
                 }
             }

             if(noOfSelectedSourcesCounter<20) {
                 if (sourcesSelectedString.length() > 0)
                     sourcesSelectedString = sourcesSelectedString.substring(0, sourcesSelectedString.length() - 1);

                 sortByQuery = sortBySpinner.getSelectedItem().toString();

                 if (sortByQuery.equalsIgnoreCase("Relevance")) {
                     sortByQuery = "relevancy";
                 } else if (sortByQuery.equalsIgnoreCase("Recent First")) {
                     sortByQuery = "publishedAt";
                 }

                 selectedSortByMode = sortBySpinner.getSelectedItemPosition();

                 fetchResults(sourcesSelectedString, sortByQuery);
             }
             else{
                 Toast.makeText(getActivity(), "At most 20 sources can be selected", Toast.LENGTH_SHORT).show();
        //         alertDialog.show();
                 displayFilterAlertDialog(sources);
             }
            }
        });

        alertDialog.setCancelable(false);

        alertDialog.show();

    }

    public void fetchSources()
    {
        apiService.getSources(ApiCredentals.API_KEY,new ResponseCallback<SourcesResponse>() {
            @Override
            public void success(SourcesResponse sourcesResponse) {

                sources= (ArrayList<NewsSource>) sourcesResponse.getSources();

                searchFragmentLoadingProgress.stop();

            }

            @Override
            public void failure(SourcesResponse sourcesResponse) {

            }
        });


    }


    public boolean checkIfArticleAlreadySaved(Article article)
    {
        for(Article article1:savedArticlesList)
        {
            if(article1.getTitle().equals(article.getTitle()))
            {
                return true;
            }
        }
        return false;

    }

    public void fetchSavedNewsFromDatabase() {
        SQLiteDB sqlhelper = new SQLiteDB(getActivity());
        SQLiteDatabase sqLiteDatabase = sqlhelper.getWritableDatabase();


        String[] columns = {"_id", SQLiteDB.TITLE, SQLiteDB.DESCRIPTION, SQLiteDB.URLTOIMAGE, SQLiteDB.CONTENT};
        Cursor cursor = sqLiteDatabase.query("ARTICLEDETAILS", columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int cid = cursor.getInt(0);
            String articleTitle = cursor.getString(1);
            String articleDescription = cursor.getString(2);
            String articleUrlToImage = cursor.getString(3);
            String articleContent = cursor.getString(4);
            Log.v("activity2", cid + " " + articleTitle + " " + articleDescription);


            Article article = new Article();
            article.setTitle(articleTitle);
            article.setDescription(articleDescription);
            article.setUrlToImage(articleUrlToImage);
            article.setContent(articleContent);

            savedArticlesList.add(article);
        }
    }

    public void populateTrendingNewsView(final ArrayList<Article> trendingArticlesList)
    {
        searchNewsAdapter=new TrendingNewsAdapter(trendingArticlesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        searchNewsRv.setLayoutManager(mLayoutManager);
        searchNewsRv.setItemAnimator(new DefaultItemAnimator());
        searchNewsRv.setAdapter(searchNewsAdapter);

        RecycleClick.addTo(searchNewsRv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                startActivity(new Intent(getActivity(),ArticleDetailActivity.class).putExtra("article",trendingArticlesList.get(position)));

            }
        });

    }


}
