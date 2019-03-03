package com.app.nikhil.newsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.nikhil.newsapp.Pojo.NewsSource;
import com.app.nikhil.newsapp.R;

import java.util.ArrayList;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.SourcesViewHolder>{

    Context context;
    ArrayList<NewsSource> sourcesList;

    @NonNull
    @Override
    public SourcesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(context).inflate(R.layout.source_item,null,false);
        return new SourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SourcesViewHolder sourcesViewHolder, final int i) {

        sourcesViewHolder.sourceTv.setText(sourcesList.get(i).getName());
        sourcesViewHolder.sourcesStateCB.setChecked(sourcesList.get(i).isIschecked());

        sourcesViewHolder.sourcesStateCB.setClickable(false);

        /*
        sourcesViewHolder.sourcesStateCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourcesList.get(i).setIschecked(!sourcesList.get(i).isIschecked());
                sourcesViewHolder.sourcesStateCB.setChecked(sourcesList.get(i).isIschecked());
            }
        });
*/

    }

    public SourcesAdapter(Context context, ArrayList<NewsSource> sourcesList) {
        this.context = context;
        this.sourcesList = sourcesList;
    }

    @Override
    public int getItemCount() {
        return sourcesList.size();
    }

    public class SourcesViewHolder extends RecyclerView.ViewHolder {

        CheckBox sourcesStateCB;
        TextView sourceTv;

        public SourcesViewHolder(@NonNull View itemView) {
            super(itemView);

            sourcesStateCB=itemView.findViewById(R.id.sourceStateCB);
            sourceTv=itemView.findViewById(R.id.sourceTv);

        }


    }



}
