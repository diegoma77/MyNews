package com.example.android.mynews.rvadapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.mynews.R;
import com.example.android.mynews.activities.WebViewSearchActivity;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterDisplaySearchArticles extends RecyclerView.Adapter<RvAdapterDisplaySearchArticles.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterDisplaySearchArticles.class.getSimpleName();

    //Loader ID
    private static final int LOADER_INSERT_ARTICLE_DATABASE = 99;

    //List of Search articles
    private List<ArticlesSearchAPIObject> listOfArticlesSearchAPIObjects = new ArrayList<>();

    //List that stores the database urls
    private List<String> listOfArticlesReadInTheDatabase;

    //Context of the activity
    private Context mContext;

    //Constructor of the RvAdapter
    public RvAdapterDisplaySearchArticles(Context context, List<ArticlesSearchAPIObject> listOfObjects, List<String> listOfUrls) {
        this.mContext = context;
        this.listOfArticlesSearchAPIObjects = new ArrayList<>();
        this.listOfArticlesSearchAPIObjects = listOfObjects;
        this.listOfArticlesReadInTheDatabase = new ArrayList<>();
        this.listOfArticlesReadInTheDatabase = listOfUrls;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_fragment;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem,
                viewGroup,
                shouldAttachToParentImmediately);

        RvAdapterDisplaySearchArticles.ViewHolder viewHolder = new RvAdapterDisplaySearchArticles.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        Log.i("POSITION: " + position, "listOfArticlesInDatabase = " + listOfArticlesReadInTheDatabase.size());

        if (listOfArticlesReadInTheDatabase.contains(listOfArticlesSearchAPIObjects.get(position).getWebUrl())){
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        holder.title.setText(listOfArticlesSearchAPIObjects.get(position).getSnippet());
        holder.section.setText("Top Stories < " + listOfArticlesSearchAPIObjects.get(position).getNewDesk());
        holder.published_date.setText(listOfArticlesSearchAPIObjects.get(position).getPubDate());

        Glide.with(mContext)
                .load(listOfArticlesSearchAPIObjects.get(position).getImageUrl())
                .into(holder.imageOnLeft);

        if (listOfArticlesSearchAPIObjects.get(position).getImageUrl() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("ONCLICK - POSITION", "#" + " CLICKED");
                Context context = v.getContext();

                /** Since we cannot call here "getSupportLoaderManager()", we will add the url
                 * to the database in the next activity (if it is not there yet) */

                Intent intent = new Intent(context, WebViewSearchActivity.class);

                /** We pass the webUrl that the next activity has to load and show
                 * in the webView */
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, listOfArticlesSearchAPIObjects.get(position).getWebUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listOfArticlesSearchAPIObjects == null) { return 0; }
        return listOfArticlesSearchAPIObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView imageOnLeft;
        private TextView section;
        private TextView published_date;
        private TextView title;

        public ViewHolder(View view) {
            super(view);

            mView = view.findViewById(R.id.list_item_globalLayout);
            imageOnLeft = view.findViewById(R.id.list_item_image_news);
            section = view.findViewById(R.id.list_item_continent);
            published_date = view.findViewById(R.id.list_item_date);
            title = view.findViewById(R.id.list_item_news_text);

        }

    }

}
