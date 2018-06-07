package com.example.sergi.cycloguardian.Job;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sergi.cycloguardian.Activities.StartActivity;
import com.example.sergi.cycloguardian.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by sergi on 23/04/2018.
 */

public class SyncJob extends Job {

    public static final String TAG = "job_sync_tag";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        return Result.SUCCESS;
    }
    

    @Override
    protected void onReschedule(int newJobId) {
        // the rescheduled job has a new ID
    }
}
