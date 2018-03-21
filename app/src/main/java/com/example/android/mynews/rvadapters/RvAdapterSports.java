package com.example.android.mynews.rvadapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.mynews.R;
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.SportsObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterSports extends RecyclerView.Adapter<RvAdapterSports.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterSports.class.getSimpleName();

    //Array that will store TopStoriesObject after request
    private ArrayList<SportsObject> sportsObjectArrayList = new ArrayList<>();

    //Context of the activity
    private Context mContext;

    //Constructor of the RvAdapter
    public RvAdapterSports(Context context) {
        //Context to work with Fragments
        this.mContext = context;

    }

    public void setSportsData(ArrayList<SportsObject> sportsObjectArrayList) {
        this.sportsObjectArrayList = sportsObjectArrayList;
        notifyDataSetChanged();
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
    public void onBindViewHolder(RvAdapterSports.ViewHolder holder, final int position) {

        holder.title.setText(sportsObjectArrayList.get(position).getTitle());
        holder.section.setText("Sports < " + sportsObjectArrayList.get(position).getSubsection());
        holder.update_date.setText(sportsObjectArrayList.get(position).getUpdatedDate());

        if (sportsObjectArrayList.get(position).getImageNormal() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(sportsObjectArrayList.get(position).getImageNormal())
                    .into(holder.imageOnLeft);
        }

        Log.d(TAG, "#" + position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

                Intent intent = new Intent(context, WebViewMainActivity.class);
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, sportsObjectArrayList.get(position).getArticleUrl());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        if (sportsObjectArrayList == null) { return 0; };
        return sportsObjectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private ImageView imageOnLeft;
        private TextView section;
        private TextView update_date;
        private TextView title;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            imageOnLeft = view.findViewById(R.id.list_item_image_news);
            section = view.findViewById(R.id.list_item_continent);
            update_date = view.findViewById(R.id.list_item_date);
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
