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
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.activities.WebViewSearchActivity;
import com.example.android.mynews.extras.interfaceswithconstants.Keys;
import com.example.android.mynews.pojo.MostPopularAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

/** Recycler View Adapter used to display all the articles that are related to Most Popular API
 * */
public class RvAdapterMostPopular extends RecyclerView.Adapter<RvAdapterMostPopular.ViewHolder> {

    // TODO: 23/04/2018 Adapt the RecyclerView to display the required information

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterMostPopular.class.getSimpleName();

    //Array that will store TopStoriesAPIObjects after request
    private List<MostPopularAPIObject> listOfMostPopularAPIObjects;

    //List that stores the database urls
    private List<String> listOfArticlesReadInTheDatabase;

    //Context of the activity
    private Context mContext;

    //Constructor of the RvAdapter
    public RvAdapterMostPopular(Context context, List<MostPopularAPIObject> listOfObjects, List<String> listOfUrls) {
        this.mContext = context;
        this.listOfMostPopularAPIObjects = new ArrayList<>();
        this.listOfMostPopularAPIObjects = listOfObjects;
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

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        Log.i("POSITION: " + position, "listOfArticlesInDatabase = " + listOfArticlesReadInTheDatabase.size());

        /** We check if the article url (of the object) is in the database (a list has been filled with
         * the articles' url of the database). If it is, we change the Typeface to bold.
         * */
        if (listOfArticlesReadInTheDatabase.contains(listOfMostPopularAPIObjects.get(position).getArticleUrl())){
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        /** We set the texts displayed in the layout (textviews) and the pictures
         *  according to the information provided by the objects of the list. If the image
         * is null, we display the New York Times logo
         * */
        holder.title.setText(listOfMostPopularAPIObjects.get(position).getTitle());
        holder.section.setText("Top Stories < " + listOfMostPopularAPIObjects.get(position).getSection());
        holder.update_date.setText(listOfMostPopularAPIObjects.get(position).getPublishedDate());

        if (listOfMostPopularAPIObjects.get(position).getImage_thumbnail() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(listOfMostPopularAPIObjects.get(position).getImage_thumbnail())
                    .into(holder.imageOnLeft);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** When the user clicks the article to read it, we add it to the database (read articles)
                 * We will add the url to the database in the next activity (if it is not there yet)
                 * */
                Log.i("ONCLICK - POSITION", "#" + " CLICKED");
                Context context = v.getContext();

                /** We pass the webUrl that the next activity has to load and show
                 * in the webView
                 * */
                Intent intent = new Intent(context, WebViewMainActivity.class);
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, listOfMostPopularAPIObjects.get(position).getArticleUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listOfMostPopularAPIObjects == null) { return 0; }
        return listOfMostPopularAPIObjects.size();
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

}
