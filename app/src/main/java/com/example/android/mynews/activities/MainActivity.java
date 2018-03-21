package com.example.android.mynews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.mynews.data.AndroidDatabaseManager;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.fragmentadapters.FragmentPageAdapter;
import com.example.android.mynews.fragments.PageFragmentBusiness;
import com.example.android.mynews.fragments.PageFragmentMostPopular;
import com.example.android.mynews.fragments.PageFragmentSports;
import com.example.android.mynews.fragments.PageFragmentTopStories;
import com.example.android.mynews.R;

public class MainActivity extends AppCompatActivity {

    // TODO: 14/03/2018 Change data structure in PageFragments dd/MM/yy

    // TODO: 21/03/2018 Images are not loading properly. Sometimes the size is incorrect and
    // TODO they take too long to upload

    private DatabaseHelper dbH;

    //Toolbar variable
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbH = new DatabaseHelper(this);

        //Sets the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Displays home button in toolbar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Changes the color of the Toolbar Overflow Button to white
        setOverflowButtonColor(toolbar, Color.WHITE);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(intent);
                break;
            case R.id.menu_search_button:
                Intent intent1 = new Intent(MainActivity.this, SearchArticlesActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_refresh_button:
                break;
            case R.id.menu_notifications_button:
                Intent intent2 = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent2);
                break;
            case R.id.menu_delete_database:
                dbH.deleteAllRows();
                break;
            case R.id.menu_help_button:
                Toast.makeText(this, "Help Button Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about_button:
                Toast.makeText(this, "About Button Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Uses de FragmentPageAdapter to link the PageFragmentTopStories to the ViewPager
    private void setupViewPager(ViewPager viewPager) {
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        fragmentPageAdapter.addFragment(new PageFragmentTopStories(), "TOP STORIES");
        fragmentPageAdapter.addFragment(new PageFragmentMostPopular(), "MOST POPULAR");
        fragmentPageAdapter.addFragment(new PageFragmentBusiness(), "BUSINESS");
        fragmentPageAdapter.addFragment(new PageFragmentSports(), "SPORTS");
        viewPager.setAdapter(fragmentPageAdapter);
    }

    //Changes the color of the Toolbar Overflow Button to white
    public static void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

}
