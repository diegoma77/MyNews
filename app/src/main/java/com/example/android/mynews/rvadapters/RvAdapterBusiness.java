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
import com.example.android.mynews.activities.WebViewMainActivity;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.BusinessObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterBusiness extends RecyclerView.Adapter<RvAdapterBusiness.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterBusiness.class.getSimpleName();

    //Array that will store TopStoriesObject after request
    private ArrayList<BusinessObject> businessObjectArrayList = new ArrayList<>();

    //Context of the activity
    private Context mContext;

    //Cursor to check if an article is in the database
    private Cursor mCursor;

    //DatabaseHelper to add an article_url to the database if it hasn't been read
    private DatabaseHelper dbH;

    //Constructor of the RvAdapter
    public RvAdapterBusiness(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    public void setBusinessData(ArrayList<BusinessObject> businessObjectArrayList) {
        this.businessObjectArrayList = businessObjectArrayList;
        notifyDataSetChanged();
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
    public void onBindViewHolder(RvAdapterBusiness.ViewHolder holder, final int position) {

        if (checkIfArticleUrlIsInTheDatabase(businessObjectArrayList.get(position).getArticleUrl())) {
            Typeface bold = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.title.setTypeface(bold);
        }

        holder.title.setText(businessObjectArrayList.get(position).getTitle());
        holder.section.setText("Business < " + businessObjectArrayList.get(position).getSubsection());
        holder.update_date.setText(businessObjectArrayList.get(position).getUpdatedDate());

        if (businessObjectArrayList.get(position).getImageNormal() == null) {
            Glide.with(mContext)
                    .load(R.drawable.nyt)
                    .into(holder.imageOnLeft);
        }
        else {
            Glide.with(mContext)
                    .load(businessObjectArrayList.get(position).getImageNormal())
                    .into(holder.imageOnLeft);
        }

        Log.d(TAG, "#" + position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

                //Checks that the article is not yet in the database. If it is, we don't add it.
                //If it's not, we add it. This way we keep the track of the articles the user has read
                if (!checkIfArticleUrlIsInTheDatabase(businessObjectArrayList.get(position).getArticleUrl())){
                    dbH.insertDataToAlreadyReadArticlesTable(businessObjectArrayList.get(position).getArticleUrl());
                }

                Intent intent = new Intent(context, WebViewMainActivity.class);
                intent.putExtra(Keys.PutExtras.ARTICLE_URL_SENT, businessObjectArrayList.get(position).getArticleUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (businessObjectArrayList == null) { return 0; };
        return businessObjectArrayList.size();
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
