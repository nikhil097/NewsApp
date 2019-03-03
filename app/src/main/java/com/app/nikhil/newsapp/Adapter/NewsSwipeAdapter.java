package com.app.nikhil.newsapp.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nikhil.newsapp.Pojo.Article;
import com.app.nikhil.newsapp.R;
import com.app.nikhil.newsapp.Rest.SQLiteDB;
import com.app.nikhil.newsapp.UI.Activity.ArticleDetailActivity;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

public class NewsSwipeAdapter extends RecyclerSwipeAdapter<NewsSwipeAdapter.SimpleViewHolder> {

    private Context mContext;
    private ArrayList<Article> articlesList;
    SQLiteDatabase sqLiteDatabase;


    public NewsSwipeAdapter(Context context, ArrayList<Article> articlesList) {
        this.mContext = context;
        this.articlesList = articlesList;
    }



    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_item, parent, false);
        SQLiteDB sqLiteDB=new SQLiteDB(mContext);
        sqLiteDatabase=sqLiteDB.getWritableDatabase();
        return new SimpleViewHolder(view);
    }

    public void saveNewsArticle(Article article)
    {
        ContentValues contentValues=new ContentValues();

        contentValues.put(SQLiteDB.TITLE,article.getTitle());
        contentValues.put(SQLiteDB.DESCRIPTION,article.getDescription());
        contentValues.put(SQLiteDB.URLTOIMAGE,article.getUrlToImage());
        contentValues.put(SQLiteDB.CONTENT,article.getContent());
        sqLiteDatabase.insert("ARTICLEDETAILS",null,contentValues);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {

            final Article article = articlesList.get(position);

            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        viewHolder.articleTitleTv.setText(article.getTitle());
        Glide.with(mContext).load(article.getUrlToImage()).into(viewHolder.articleImageView);
        viewHolder.articleDescriptionTv.setText(article.getDescription());

        if(article.getIsSaved())
        {
            viewHolder.btnSaveNews.setImageResource(R.drawable.icons8_save_close_961);
        }

            // Drag From Left
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

            viewHolder.swipeLayout.setLeftSwipeEnabled(false);

            // Handling different events when swiping
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            });

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/

            viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
           //         Toast.makeText(mContext, " onClick : ", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext,ArticleDetailActivity.class).putExtra("article",articlesList.get(position)));

                }
            });



            viewHolder.btnSaveNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    viewHolder.btnSaveNews.setImageResource(0);
                    viewHolder.btnSaveNews.setBackground(mContext.getResources().getDrawable(R.drawable.icons8_save_close_961));

             //       Toast.makeText(v.getContext(), "Clicked on Map ", Toast.LENGTH_SHORT).show();
                    saveNewsArticle(articlesList.get(position));
                }
            });


            // mItemManger is member in RecyclerSwipeAdapter Class
            mItemManger.bindView(viewHolder.itemView, position);

        }


    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }





     public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageButton btnSaveNews;
         ImageView articleImageView;
         TextView articleTitleTv,articleDescriptionTv;


         public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout =  itemView.findViewById(R.id.swipe);
            btnSaveNews =  itemView.findViewById(R.id.btnSaveNews);
            articleImageView=itemView.findViewById(R.id.newsImage);
            articleTitleTv=itemView.findViewById(R.id.articleTitleTv);
            articleDescriptionTv=itemView.findViewById(R.id.articleDescriptionTv);


        }
    }

}
