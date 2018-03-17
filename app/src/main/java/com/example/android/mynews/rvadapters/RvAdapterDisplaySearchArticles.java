package com.example.android.mynews.rvadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mynews.R;
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

    public RvAdapterDisplaySearchArticles(Context context, List<String> searchArticlesList) {
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_fragment;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        for (int i = 0; i < 10; i++) {

            searchArticlesList.add("Random Number" + Math.random() * 50 + 1);

        }

        View view = layoutInflater.inflate(layoutIdForListItem,
                viewGroup,
                shouldAttachToParentImmediately);

        ViewHolder viewHolder = new ViewHolder(view);

        // TODO: 15/03/2018 Delete this list
        searchArticlesList = new ArrayList<>();


        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.d(TAG, "#" + position);

        holder.title.setText(searchArticlesList.get((int) Math.random() * 10 + 1));
        holder.section.setText(searchArticlesList.get((int) Math.random() * 10 + 1));
        holder.published_date.setText(searchArticlesList.get((int) Math.random() * 10 + 1));
        holder.imageOnLeft.setImageResource(R.drawable.rajoy);



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

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
