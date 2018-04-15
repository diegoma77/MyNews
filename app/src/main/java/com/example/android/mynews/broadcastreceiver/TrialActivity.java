package com.example.android.mynews.broadcastreceiver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.mynews.R;
import com.example.android.mynews.extras.Keys;
import com.example.android.mynews.pojo.ArticlesAPIObject;

import java.util.ArrayList;
import java.util.List;

public class TrialActivity extends AppCompatActivity {

    String REFERENCE_TO_OBJECT = Keys.ApiFetcher.ARTICLES_API_REFERENCE;

    List<ArticlesAPIObject> listOfObjects;

    TextView tv;
    Button button;

    Button button1;
    Button button2;

    ApiFetcher apiFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        listOfObjects = new ArrayList<>();

        apiFetcher = new ApiFetcher(
                REFERENCE_TO_OBJECT);

        apiFetcher.setUrl1("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=arts+business&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");
        apiFetcher.setUrl2("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=arts+business&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");
        apiFetcher.setUrl3("http://api.nytimes.com/svc/search/v2/articlesearch.json?fq=arts+business&begin_date=20180411&end_date=20180413&sort=newest&page=1&api-key=a27a66145d4542d28a719cecee6de859");


        button = findViewById(R.id.button_trial);
        tv = findViewById(R.id.tv_trial);

        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);

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

                apiFetcher.startJSONRequestArticlesAPI(apiFetcher.getUrl1(), TrialActivity.this);
                Thread.sleep(2000);
                publishProgress();
                apiFetcher.startJSONRequestArticlesAPI(apiFetcher.getUrl2(), TrialActivity.this);
                Thread.sleep(2000);
                publishProgress();
                apiFetcher.startJSONRequestArticlesAPI(apiFetcher.getUrl3(), TrialActivity.this);
                Thread.sleep(2000);
                publishProgress();

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
            button1.setText(listOfObjects.get(0).getSnippet());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            listOfObjects = apiFetcher.getListOfObjects();
        }
    }
}

/**

    @Override
    protected List<ArticlesAPIObject> doInBackground() {



        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
*/