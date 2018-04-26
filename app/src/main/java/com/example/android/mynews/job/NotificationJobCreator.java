package com.example.android.mynews.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Diego Fajardo on 26/04/2018.
 */

/** Evernote Android Job Library: JOBCREATOR.
 * Used to create jobs based on its TAG
 */
public class NotificationJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {

        switch (tag) {

            case NotificationDailyJob.TAG:
                return new NotificationDailyJob();
            default:
                return null;
        }
    }
}
