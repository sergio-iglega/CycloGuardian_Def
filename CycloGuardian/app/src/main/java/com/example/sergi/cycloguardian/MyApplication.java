package com.example.sergi.cycloguardian;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.example.sergi.cycloguardian.Job.SyncJobCreator;
import com.example.sergi.cycloguardian.Models.Session;
import com.facebook.stetho.Stetho;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.internal.RxBleLog;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sergi on 16/04/2018.
 */

public class MyApplication extends Application {


    private RxBleClient rxBleClient;

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new SyncJobCreator());
        Stetho.initializeWithDefaults(this);
        rxBleClient = RxBleClient.create(this);
        RxBleClient.setLogLevel(RxBleLog.VERBOSE);
    }

    /**
     * In practise you will use some kind of dependency injection pattern.
     */
    public static RxBleClient getRxBleClient(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.rxBleClient;
    }


    public Session mySession = new Session();
}
