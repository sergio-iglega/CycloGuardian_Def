package com.example.sergi.cycloguardian.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by sergi on 23/04/2018.
 */

public class SyncJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncJob.TAG:
                return new SyncJob();
            default:
                return null;
        }
    }
}
