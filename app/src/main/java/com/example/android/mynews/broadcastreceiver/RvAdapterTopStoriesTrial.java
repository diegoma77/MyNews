package com.example.android.mynews.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.mynews.R;
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.asynctaskloaders.atlhelper.AsyncTaskLoaderHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterTopStoriesTrial extends RecyclerView.Adapter<RvAdapterTopStoriesTrial.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterTopStoriesTrial.class.getSimpleName();

    //Loader ID
    private static final int LOADER_READ_ARTICLES_DATABASE = 11;
    private static final int LOADER_INSERT_ARTICLE_DATABASE = 12;

    //Array that will store TopStoriesAPIObjects after request
    private List<TopStoriesAPIObject> topStoriesAPIObjectsList;

    //List that stores the database urls
    private List<String> articlesReadInTheDatabaseList;

    //Context of the activity
    private Context mContext;

    // TODO: 22/04/2018  Activity, check if it makes the loader to work
    private AppCompatActivity activity;

    //Constructor of the RvAdapter
    public RvAdapterTopStoriesTrial(Context context, List<TopStoriesAPIObject> list, AppCompatActivity activity) {
        this.mContext = context;
        this.activity = activity;
        topStoriesAPIObjectsList = new ArrayList<>();
        this.topStoriesAPIObjectsList = list;
        this.articlesReadInTheDatabaseList = new ArrayList<>();
        loadLoaderGetReadArticlesFromDatabase(LOADER_READ_ARTICLES_DATABASE);

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

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvAdapterTopStoriesTrial.ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        if (articlesReadInTheDatabaseList.contains(topStoriesAPIObjectsList.get(position).getArticleUrl())){
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        holder.title.setText(topStoriesAPIObjectsList.get(position).getTitle());
        holder.section.setText("Top Stories < " + topStoriesAPIObjectsList.get(position).getSection());
        holder.update_date.setText(topStoriesAPIObjectsList.get(position).getUpdatedDate());

        if (topStoriesAPIObjectsList.get(position).getImageThumblarge() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(topStoriesAPIObjectsList.get(position).getImageThumblarge())
                    .into(holder.imageOnLeft);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION", "#" + " CLICKED");
                Context context = v.getContext();
                //Checks that the article is not yet in the database. If it is, we don't add it.
                // If it's not, we add it. This way we keep the track of the articles the user has read
                if (!articlesReadInTheDatabaseList.contains(topStoriesAPIObjectsList.get(position).getArticleUrl())) {
                    loadLoaderInsertArticleInDatabase(LOADER_INSERT_ARTICLE_DATABASE, topStoriesAPIObjectsList.get(position).getArticleUrl());
                }

                Intent intent = new Intent(context, WebViewMainActivity.class);
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, topStoriesAPIObjectsList.get(position).getArticleUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topStoriesAPIObjectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private ImageView imageOnLeft;
        private TextView section;
        private TextView update_date;
        private TextView title;

        public ViewHolder(View view) {
            super(view);

            mView = view.findViewById(R.id.list_item_globalLayout);
            imageOnLeft = view.findViewById(R.id.list_item_image_news);
            section = view.findViewById(R.id.list_item_continent);
            update_date = view.findViewById(R.id.list_item_date);
            title = view.findViewById(R.id.list_item_news_text);

        }

    }

    /***********************
     * LOADER **************
     * *********************/

    private void loadLoaderGetReadArticlesFromDatabase(int id) {

        android.support.v4.app.LoaderManager loaderManager = activity.getSupportLoaderManager();
        Loader<List<String>> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.initLoader(id, null, loaderGetReadArticlesFromDatabase);
        } else {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.restartLoader(id, null, loaderGetReadArticlesFromDatabase);
        }
    }

    private void loadLoaderInsertArticleInDatabase(int id, String url) {

        Bundle bundle = new Bundle();
        bundle.putString("KEY", url);

        android.support.v4.app.LoaderManager loaderManager = activity.getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.initLoader(id, bundle, loaderInsertArticleInDatabase);
        } else {
            Log.i(TAG, "loadLoaderGetReadArticlesFromDatabase: ");
            loaderManager.restartLoader(id, bundle, loaderInsertArticleInDatabase);
        }
    }


    /***********************
     * LOADER CALLBACKS ****
     * *********************/

    private android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>> loaderGetReadArticlesFromDatabase =
            new android.support.v4.app.LoaderManager.LoaderCallbacks<List<String>>() {

                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.getArticlesReadFromDatabase(mContext);
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    articlesReadInTheDatabaseList.addAll(data);
                }

                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<String> loaderInsertArticleInDatabase =
            new LoaderManager.LoaderCallbacks<String>() {
                @Override
                public Loader<String> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.insertArticleUrlInDatabase(mContext, args.getString("KEY"));
                }

                @Override
                public void onLoadFinished(Loader<String> loader, String data) {

                }

                @Override
                public void onLoaderReset(Loader<String> loader) {

                }
            }




}
