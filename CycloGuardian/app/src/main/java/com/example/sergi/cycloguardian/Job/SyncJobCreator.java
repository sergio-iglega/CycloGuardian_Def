package com.example.sergi.cycloguardian.Job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Clase encargada de la creación de los trabajos de subida
 * Created by sergi on 23/04/2018.
 */

public class SyncJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncJobIncidence.TAG:
                return new SyncJobIncidence();
            case SyncJobSesion.TAG:
                return new SyncJobSesion();
            default:
                return null;
        }
    }
}
