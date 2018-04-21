package com.example.android.mynews.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.ArticlesAPIObject;
import com.example.android.mynews.pojo.MostPopularAPIObject;
import com.example.android.mynews.pojo.TopStoriesAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterTrial extends RecyclerView.Adapter<RvAdapterTrial.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterTrial.class.getSimpleName();

    //Array that will store ArticlesAPIObjects after request
    private List<ArticlesAPIObject> articlesAPIObjectsList;

    //Array that will store MostPopularAPIObjects after request
    private List<MostPopularAPIObject> mostPopularAPIObjectsList;

    //Array that will store TopStoriesAPIObjects after request
    private List<TopStoriesAPIObject> topStoriesAPIObjectsList;

    //Reference to API
    private String referenceToAPI;

    //Context of the activity
    private Context mContext;

    //Cursor to check if an article is in the database
    private Cursor mCursor;

    //DatabaseHelper to add an article_url to the database if it hasn't been read
    private DatabaseHelper dbH;

    //Constructor of the RvAdapter
    public RvAdapterTrial(String referenceToApi, Context context, Cursor cursor) {
        this.referenceToAPI = referenceToApi;
        this.mContext = context;
        this.mCursor = cursor;

        switch (referenceToApi) {
            case Keys.ApiFetcher.ARTICLES_API_REFERENCE:
                articlesAPIObjectsList = new ArrayList<>();
                break;
            case Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE:
                mostPopularAPIObjectsList = new ArrayList<>();
                break;
            case Keys.ApiFetcher.TOPSTORIES_API_REFERENCE:
                topStoriesAPIObjectsList = new ArrayList<>();
                break;
        }
    }

    public void setDataFromArticlesAPI(List<ArticlesAPIObject> list) {
        if (articlesAPIObjectsList != null) {
            articlesAPIObjectsList = list;
            notifyDataSetChanged();
        }
    }

    public void setDataFromMostPopularAPI(List<MostPopularAPIObject> list) {
        if (mostPopularAPIObjectsList != null) {
            mostPopularAPIObjectsList = list;
            notifyDataSetChanged();
        }
    }

    public void setDataFromATopStoriesAPI(List<TopStoriesAPIObject> list) {
        if (topStoriesAPIObjectsList != null) {
            topStoriesAPIObjectsList = list;
            notifyDataSetChanged();
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        dbH = new DatabaseHelper(mContext);

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
    public void onBindViewHolder(RvAdapterTrial.ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        switch (referenceToAPI){

            case Keys.ApiFetcher.ARTICLES_API_REFERENCE: {
                if (checkIfArticleUrlIsInTheDatabase(articlesAPIObjectsList.get(position).getWeb_url())) {
                    Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
                    holder.title.setTypeface(bold);
                }

                holder.title.setText(articlesAPIObjectsList.get(position).getSnippet());
                holder.section.setText("Top Stories < " + articlesAPIObjectsList.get(position).getNew_desk());
                holder.update_date.setText(articlesAPIObjectsList.get(position).getPub_date());

                if (articlesAPIObjectsList.get(position).getImage_url() == null) {
                    Glide.with(mContext)
                            .load(R.drawable.nyt)
                            .into(holder.imageOnLeft);
                }
                else {
                    Glide.with(mContext)
                            .load(articlesAPIObjectsList.get(position).getImage_url())
                            .into(holder.imageOnLeft);
                }

                break;
            }

            case Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE: {
                if (checkIfArticleUrlIsInTheDatabase(mostPopularAPIObjectsList.get(position).getArticle_url())) {
                    Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
                    holder.title.setTypeface(bold);
                }

                holder.title.setText(mostPopularAPIObjectsList.get(position).getTitle());
                holder.section.setText("Top Stories < " + mostPopularAPIObjectsList.get(position).getSection());
                holder.update_date.setText(mostPopularAPIObjectsList.get(position).getPublished_date());

                if (mostPopularAPIObjectsList.get(position).getImage_thumbnail() == null) {
                    Glide.with(mContext)
                            .load(R.drawable.nyt)
                            .into(holder.imageOnLeft);
                }
                else {
                    Glide.with(mContext)
                            .load(mostPopularAPIObjectsList.get(position).getImage_thumbnail())
                            .into(holder.imageOnLeft);
                }

                break;

            }

            case Keys.ApiFetcher.TOPSTORIES_API_REFERENCE: {
                if (checkIfArticleUrlIsInTheDatabase(topStoriesAPIObjectsList.get(position).getArticleUrl())) {
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

                break;

            }

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION", "#" + " CLICKED");
                Context context = v.getContext();

                switch (referenceToAPI) {

                    case Keys.ApiFetcher.ARTICLES_API_REFERENCE: {

                        //Checks that the article is not yet in the database. If it is, we don't add it.
                        // If it's not, we add it. This way we keep the track of the articles the user has read
                        if (!checkIfArticleUrlIsInTheDatabase(articlesAPIObjectsList.get(position).getWeb_url())) {
                            dbH.insertDataToAlreadyReadArticlesTable(articlesAPIObjectsList.get(position).getWeb_url());
                        }

                        Intent intent = new Intent(context, WebViewMainActivity.class);
                        intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, articlesAPIObjectsList.get(position).getWeb_url());
                        context.startActivity(intent);

                        break;

                    }

                    case Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE: {

                        //Checks that the article is not yet in the database. If it is, we don't add it.
                        // If it's not, we add it. This way we keep the track of the articles the user has read
                        if (!checkIfArticleUrlIsInTheDatabase(mostPopularAPIObjectsList.get(position).getArticle_url())) {
                            dbH.insertDataToAlreadyReadArticlesTable(mostPopularAPIObjectsList.get(position).getArticle_url());
                        }

                        Intent intent = new Intent(context, WebViewMainActivity.class);
                        intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, mostPopularAPIObjectsList.get(position).getArticle_url());
                        context.startActivity(intent);
                    }

                    case Keys.ApiFetcher.TOPSTORIES_API_REFERENCE: {

                        //Checks that the article is not yet in the database. If it is, we don't add it.
                        // If it's not, we add it. This way we keep the track of the articles the user has read
                        if (!checkIfArticleUrlIsInTheDatabase(topStoriesAPIObjectsList.get(position).getArticleUrl())) {
                            dbH.insertDataToAlreadyReadArticlesTable(topStoriesAPIObjectsList.get(position).getArticleUrl());
                        }

                        Intent intent = new Intent(context, WebViewMainActivity.class);
                        intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, topStoriesAPIObjectsList.get(position).getArticleUrl());
                        context.startActivity(intent);

                    }

                }
            }

        });
    }

    @Override
    public int getItemCount() {
        if (referenceToAPI.equals(Keys.ApiFetcher.ARTICLES_API_REFERENCE)){
            if (articlesAPIObjectsList == null) { return 0; }
            return articlesAPIObjectsList.size();

        } else if (referenceToAPI.equals(Keys.ApiFetcher.MOSTPOPULAR_API_REFERENCE)){
            if (mostPopularAPIObjectsList == null) { return 0; }
            return mostPopularAPIObjectsList.size();

        } else if (referenceToAPI.equals(Keys.ApiFetcher.TOPSTORIES_API_REFERENCE)){
            if (topStoriesAPIObjectsList == null) { return 0; }
            return topStoriesAPIObjectsList.size();
        }
        else { return 0; }
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

    /**
     * Checks if the article is in the database.
     * It's used to check if an article has already been read or not.
     * */
    private boolean checkIfArticleUrlIsInTheDatabase(String web_url) {

        int counter = 0;

        for (int i = 0; i < mCursor.getCount() ; i++) {
            mCursor.moveToPosition(i);
            if (mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.ARTICLE_URL)).equals(web_url)){
                counter++;
            }
        }

        if (counter != 0) return true;
        else return false;
    }

}
