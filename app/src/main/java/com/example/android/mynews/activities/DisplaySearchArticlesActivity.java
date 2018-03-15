package com.example.android.mynews.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.mynews.R;
import com.example.android.mynews.rvadapters.RvAdapterSearchArticles;

public class DisplaySearchArticlesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_articles);

        recyclerView = (RecyclerView) findViewById(R.id.);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new RvAdapterSearchArticles(this));

    }
}
