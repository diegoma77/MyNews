package com.example.android.mynews.rvadapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.activities.WebViewActivity;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.SearchArticlesObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterDisplaySearchArticles extends RecyclerView.Adapter<RvAdapterDisplaySearchArticles.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterDisplaySearchArticles.class.getSimpleName();

    private List<SearchArticlesObject> searchArticlesList = new ArrayList<>();

    private Context mContext;

    public RvAdapterDisplaySearchArticles(Context context, List<SearchArticlesObject> searchArticlesList) {
        this.mContext = context;
        this.searchArticlesList = searchArticlesList;
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

        SearchArticlesObject currentObject = searchArticlesList.get(position);

        holder.title.setText(currentObject.getSnippet());
        holder.section.setText(currentObject.getNew_desk());
        holder.published_date.setText(currentObject.getPub_date());
        holder.imageOnLeft.setImageResource(R.drawable.rajoy);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, searchArticlesList.get(position).getWeb_url());
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

        private final View mView;
        private ImageView imageOnLeft;
        private TextView section;
        private TextView published_date;
        private TextView title;

        public ViewHolder(View view) {
            super(view);

            mView = view;
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
}
