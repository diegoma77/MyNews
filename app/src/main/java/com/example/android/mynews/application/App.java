package com.example.android.mynews.application;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.android.mynews.job.NotificationJobCreator;

/**
 * Created by Diego Fajardo on 26/04/2018.
 */

/** Used for Android Job, Evernote Library
 * */
public class App extends Application {

    // TODO: 30/04/2018 Put it in the manifest

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new NotificationJobCreator());
    }
}
