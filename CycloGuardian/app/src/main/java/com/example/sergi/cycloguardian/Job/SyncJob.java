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
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), StartActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getContext())
                .setContentTitle("Android Job Demo")
                .setContentText("Notification from Android Job Demo App.")
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.logo)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getContext())
                .notify(new Random().nextInt(), notification);

        return Result.SUCCESS;
    }

    public static void schedulePeriodic() {
        new JobRequest.Builder(SyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
