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
import com.example.android.mynews.activities.WebViewNotificationsActivity;
import com.example.android.mynews.activities.WebViewSearchActivity;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

/** Recycler View Adapter used to display all the articles that are related to Articles Search API.
 * This RV displays those articles that are found using the Notifications functionality.
 * */
public class RvAdapterDisplayNotificationArticles extends RecyclerView.Adapter<RvAdapterDisplayNotificationArticles.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = "RvAdapterDisplayNotific";

    //List of Search articles
    private List<ArticlesSearchAPIObject> listOfArticlesSearchAPIObjects = new ArrayList<>();

    //List that stores the database urls
    private List<String> listOfArticlesReadInTheDatabase;

    //Context of the activity
    private Context mContext;

    //Constructor of the RvAdapter
    public RvAdapterDisplayNotificationArticles(Context context, List<ArticlesSearchAPIObject> listOfObjects, List<String> listOfUrls) {
        this.mContext = context;
        this.listOfArticlesSearchAPIObjects = new ArrayList<>();
        this.listOfArticlesSearchAPIObjects = listOfObjects;
        this.listOfArticlesReadInTheDatabase = new ArrayList<>();
        this.listOfArticlesReadInTheDatabase = listOfUrls;

    }

    @Override
    public RvAdapterDisplayNotificationArticles.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_fragment;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem,
                viewGroup,
                shouldAttachToParentImmediately);

        RvAdapterDisplayNotificationArticles.ViewHolder viewHolder = new RvAdapterDisplayNotificationArticles.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RvAdapterDisplayNotificationArticles.ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        Log.i("POSITION: " + position, "listOfArticlesInDatabase = " + listOfArticlesReadInTheDatabase.size());

        /** We check if the article url (of the object) is in the database (a list has been filled with
         * the articles' url of the database). If it is, we change the Typeface to bold.
         * */
        if (listOfArticlesReadInTheDatabase.contains(listOfArticlesSearchAPIObjects.get(position).getWebUrl())){
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        /** We set the texts displayed in the layout (textviews) and the pictures
         *  according to the information provided by the objects of the list. If the image
         * is null, we display the New York Times logo
         * */
        holder.title.setText(listOfArticlesSearchAPIObjects.get(position).getSnippet());
        holder.section.setText("Top Stories < " + listOfArticlesSearchAPIObjects.get(position).getNewDesk());
        holder.published_date.setText(listOfArticlesSearchAPIObjects.get(position).getPubDate());

        if (listOfArticlesSearchAPIObjects.get(position).getImageUrl() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(listOfArticlesSearchAPIObjects.get(position).getImageUrl())
                    .into(holder.imageOnLeft);
        }

        /** If the user clicks on any place of the view (rectangle that gathers all the textviews and
         * imageview) then a listener is triggered. It displays the article in a web view (next
         * activity)
         * */
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** When the user clicks the article to read it, we add it to the database (read articles)
                 * We will add the url to the database in the next activity (if it is not there yet)
                 */
                Log.i("ONCLICK - POSITION", "#" + " CLICKED");
                Context context = v.getContext();

                /** We pass the webUrl that the next activity has to load and show
                 * in the webView
                 * */
                Intent intent = new Intent(context, WebViewNotificationsActivity.class);
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
