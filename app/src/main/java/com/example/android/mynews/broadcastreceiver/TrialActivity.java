package com.example.android.mynews.broadcastreceiver;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.data.DatabaseContract;
import com.example.android.mynews.data.DatabaseHelper;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.ArticlesAPIObject;

import java.util.ArrayList;
import java.util.List;

public class TrialActivity extends AppCompatActivity {

    String REFERENCE_TO_API = Keys.ApiFetcher.ARTICLES_API_REFERENCE;

    List<ArticlesAPIObject> listOfObjects;

    TextView tv;
    Button button;

    Button button1;
    Button button2;

    APIFetcher apiFetcher;

    DatabaseHelper dbH;
    Cursor mCursor;

    RecyclerView recyclerView;
    RvAdapterTrial rvAdapterTrial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        dbH = new DatabaseHelper(this);
        mCursor = dbH.getAllDataFromTableName(DatabaseContract.Database.ALREADY_READ_ARTICLES_TABLE_NAME);

        listOfObjects = new ArrayList<>();

        button = findViewById(R.id.button_trial);
        tv = findViewById(R.id.tv_trial);

        apiFetcher = new APIFetcher(
                REFERENCE_TO_API);

        apiFetcher.addUrl("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=arts&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");
        apiFetcher.addUrl("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=business&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");
        apiFetcher.addUrl("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=politics&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");

        recyclerView = findViewById(R.id.recycler_view_trial);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TrialActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        rvAdapterTrial = new RvAdapterTrial(
                REFERENCE_TO_API,
                TrialActivity.this,
                mCursor);

        recyclerView.setAdapter(rvAdapterTrial);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestApi().execute();
            }
        });

    }

    //1.Params
    //2.Progress
    //3.Result
    class RequestApi extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                for (int i = 0; i < apiFetcher.getlistOfUrlsSize() ; i++) {
                    apiFetcher.startJSONRequestArticlesAPI(apiFetcher.getUrl(i), TrialActivity.this);
                    Thread.sleep(5000);
                    publishProgress();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            listOfObjects = apiFetcher.getListOfArticlesObjects();
            rvAdapterTrial.setDataFromArticlesAPI(listOfObjects);
            tv.setText(listOfObjects.get(listOfObjects.size()-1).getSnippet());
        }
    }
}

