package com.example.android.mynews.rvadapters;

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
import com.example.android.mynews.activities.WebViewSearchActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.ArticlesSearchAPIObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterDisplaySearchArticles extends RecyclerView.Adapter<RvAdapterDisplaySearchArticles.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterDisplaySearchArticles.class.getSimpleName();

    private List<ArticlesSearchAPIObject> searchArticlesList = new ArrayList<>();

    //Variable used to avoid crashing when WebViewSearchActivity returns to DisplaySearchArticlesActivity
    //We carry these urls to the WebViewSearchActivity so it can bring them back when home back button
    //or back button are pressed
    private List<String> searchArticlesListOfUrls;

    //Context of the activity
    private Context mContext;

    //Cursor to check if an article is in the database
    private Cursor mCursor;

    //DatabaseHelper to add an article_url to the database if it hasn't been read
    private DatabaseHelper dbH;

    public RvAdapterDisplaySearchArticles(Context context,
                                          List<ArticlesSearchAPIObject> searchArticlesList,
                                          Cursor cursor,
                                          List<String> searchArticlesListOfUrls) {
        this.mContext = context;
        this.searchArticlesList = searchArticlesList;
        this.mCursor = cursor;
        this.searchArticlesListOfUrls = searchArticlesListOfUrls;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        if (checkIfArticleUrlIsInTheDatabase(searchArticlesList.get(position).getWeb_url())) {
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        holder.title.setText(searchArticlesList.get(position).getSnippet());
        holder.section.setText(searchArticlesList.get(position).getNew_desk());
        holder.published_date.setText(searchArticlesList.get(position).getPub_date());

        if (searchArticlesList.get(position).getImage_url() == null ||
                searchArticlesList.get(position).getImage_url().equals("")) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(searchArticlesList.get(position).getImage_url())
                    .into(holder.imageOnLeft);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

                //Checks that the article is not yet in the database. If it is, we don't add it.
                //If it's not, we add it. This way we keep the track of the articles the user has read
                if (!checkIfArticleUrlIsInTheDatabase(searchArticlesList.get(position).getWeb_url())){
                    dbH.insertDataToAlreadyReadArticlesTable(searchArticlesList.get(position).getWeb_url());
                }

                Intent intent = new Intent(context, WebViewSearchActivity.class);

                //Puts an extra that will be the url that the webView will read
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, searchArticlesList.get(position).getWeb_url());

                //Puts 3 extras that will be the urls sent to API when we return with back buttons
                intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE1, searchArticlesListOfUrls.get(0));
                intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE2, searchArticlesListOfUrls.get(1));
                intent.putExtra(Keys.PutExtras.INTENT_SA_PAGE3, searchArticlesListOfUrls.get(2));

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (searchArticlesList == null) { return 0; }
        return searchArticlesList.size();
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

        public void bindViewHolder (int position) {

            //section.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.SECTION)));
            //title.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.TITLE)));
            //update_date.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseContract.Database.UPDATE_DATE)));
            //imageOnLeft.setImageResource(R.drawable.rajoy);

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
