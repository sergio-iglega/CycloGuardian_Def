package com.example.sergi.cycloguardian;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.sergi.cycloguardian.Job.SyncJobCreator;
import com.example.sergi.cycloguardian.Models.Session;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sergi on 16/04/2018.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new SyncJobCreator());
    }

    public Session mySession = new Session();
}
