package com.example.android.mynews.rvadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.pojo.TopStoriesObject;

import java.util.ArrayList;

/**
 * Created by Diego Fajardo on 25/02/2018.
 */

public class RvAdapterBusiness extends RecyclerView.Adapter<RvAdapterBusiness.ViewHolder> {

    //Variable that allows to control the Adapter using "logs" (used in onBindViewHolder method)
    private static final String TAG = RvAdapterBusiness.class.getSimpleName();

    //Array that will store TopStoriesObject after request
    private ArrayList<TopStoriesObject> topStoriesObjectArrayList= new ArrayList<>();

    //Necessary for the context of the constructor of the RvAdapter
    private final TypedValue mTypedValue = new TypedValue();

    //Constructor of the RvAdapter
    public RvAdapterBusiness(Context context) {

        //Context to work with Fragments
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);

    }

    public void setTopStoriesData(ArrayList<TopStoriesObject> topStoriesObjectArrayList) {
        this.topStoriesObjectArrayList = topStoriesObjectArrayList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item_fragment_top_stories;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem,
                viewGroup,
                shouldAttachToParentImmediately);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvAdapterBusiness.ViewHolder holder, int position) {

        holder.title.setText(topStoriesObjectArrayList.get(position).getTitle());
        holder.section.setText(topStoriesObjectArrayList.get(position).getSection());
        holder.update_date.setText(topStoriesObjectArrayList.get(position).getUpdatedDate());
        holder.imageOnLeft.setImageResource(R.drawable.rajoy);

        Log.d(TAG, "#" + position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ONCLICK - POSITION","#" + " CLICKED");
                Context context = v.getContext();

                //Intent intent = new Intent(context, OtherActivity.class);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (topStoriesObjectArrayList == null) { return 0; };
        return topStoriesObjectArrayList.size();
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
