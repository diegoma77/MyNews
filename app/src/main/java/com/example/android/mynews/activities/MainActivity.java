package com.example.android.mynews.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.mynews.asynctaskloaders.atlhelper.AsyncTaskLoaderHelper;
import com.example.android.mynews.fragmentstrial.PageFragmentBusinessTrial;
import com.example.android.mynews.fragmentstrial.PageFragmentSportsTrial;
import com.example.android.mynews.fragmentstrial.PageFragmentTopStoriesTrial;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.fragmentadapters.FragmentPageAdapter;
import com.example.android.mynews.R;
import com.example.android.mynews.fragmentstrial.PageFragmentMostPopularTrial;
import com.example.android.mynews.groupwaiting.DisplaySearchArticlesActivityTrial;
import com.example.android.mynews.groupwaiting.SearchArticlesActivityTrial;

// TODO: 30/03/2018 The tablet crashes when showing the DF image
// TODO: 24/04/2018 Pay attention to loader IDs
// TODO: 24/04/2018 Add button in notifications to see the articles displayed by notifications

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int LOADER_DATABASE_CREATION = 1;

    private DatabaseHelper dbH;

    //Toolbar variable
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Changes the color of the Toolbar Overflow Button
        //Listener to white
        setOverflowButtonColor(toolbar, Color.WHITE);

        //Checking if the tables of the database exist. If they don't,
        //we create them
        loadLoaderCreateDatabase(LOADER_DATABASE_CREATION);

        //Instantiation of the DatabaseHelper
        dbH = new DatabaseHelper(this);

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
            case R.id.menu_search_button:
                // TODO: 24/04/2018 Changes SearchArticlesActivity to SAActivityTrial
                Intent intent1 = new Intent(MainActivity.this, SearchArticlesActivityTrial.class);
                startActivity(intent1);
                break;
            case R.id.menu_notifications_button:
                Intent intent2 = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent2);
                break;
            case R.id.menu_delete_database:
                alertDialogDeleteHistory();
                break;
            case R.id.menu_help_button:
                startActivity (new Intent(MainActivity.this, HelpActivity.class));
                break;
            case R.id.menu_about_button:
                startActivity (new Intent(MainActivity.this, DisplaySearchArticlesActivityTrial.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Uses de FragmentPageAdapter to link the PageFragmentTopStories to the ViewPager
    private void setupViewPager(ViewPager viewPager) {
        FragmentPageAdapter fragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        fragmentPageAdapter.addFragment(new PageFragmentTopStoriesTrial(), getResources().getString(R.string.top_stories_tag));
        fragmentPageAdapter.addFragment(new PageFragmentMostPopularTrial(), getResources().getString(R.string.most_popular_tag));
        fragmentPageAdapter.addFragment(new PageFragmentBusinessTrial(), getResources().getString(R.string.business_tag));
        fragmentPageAdapter.addFragment(new PageFragmentSportsTrial(), getResources().getString(R.string.sports_tag));

        viewPager.setAdapter(fragmentPageAdapter);
    }

    //Changes the color of the Toolbar Overflow ButtonListener to white
    public static void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if(drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    // TODO: 23/04/2018 Do it in a different thread

    private void alertDialogDeleteHistory () {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.delete_history_message))
                .setTitle(getResources().getString(R.string.delete_history_title))
                .setPositiveButton(getResources().getString(R.string.delete_history_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbH.deleteAllRowsFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
                        dbH.resetAutoIncrement(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.delete_history_toast), Toast.LENGTH_SHORT).show();

                        //Code used to restart the activity
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.delete_history_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing happens
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /*****************************/
    /** METHODS TO INIT LOADERS **/
    /*****************************/

    /** The loaders use the LoaderCallbacks to call AsyncTaskLoaders
     * and check if the tables of the database exist
     * */
    private void loadLoaderCreateDatabase(int id) {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Boolean> loader = loaderManager.getLoader(id);

        if (loader == null) {
            Log.i(TAG, "loadLoaderUpdateSwitchTable: ");
            loaderManager.initLoader(id, null, loaderCreateDatabase);
        } else {
            Log.i(TAG, "loadLoaderUpdateSwitchTable: ");
            loaderManager.restartLoader(id, null, loaderCreateDatabase);
        }
    }


    /**********************/
    /** LOADER CALLBACKS **/
    /**********************/

    /** This LoaderCallback
     * checks if the tables in the database
     * exist and, if they don't, it fills them
     * with the necessary information
     * */
    private LoaderManager.LoaderCallbacks<Boolean> loaderCreateDatabase =
            new LoaderManager.LoaderCallbacks<Boolean>() {

                @Override
                public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                    return AsyncTaskLoaderHelper.createDatabaseIfDoesntExist(MainActivity.this);
                }

                @Override
                public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

                }

                @Override
                public void onLoaderReset(Loader<Boolean> loader) {

                }

            };

}