package com.example.sergi.cycloguardian.Job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.SessionEntity;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Retrofit.APIClient;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Retrofit.ServerDataResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sergi on 07/06/2018.
 */

public class SyncJobSesion extends Job {
    public static final String TAG = "job_sync_session_tag";
    AppDataBase dataBase;
    RestInterface restInterface;
    Boolean successSignSesion = true;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        Log.i("JOB SESION", "En trabajo");

        dataBase = AppDataBase.getAppDataBase(getContext());
        try {
            restInterface = APIClient.getRetrofit(getContext()).create(RestInterface.class);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            //Obtain sessions from database
            List<SessionEntity> sessionEntityList = dataBase.sessionDao().getAll();
            for (int i=0; i<sessionEntityList.size(); i++) {
                //Obtain the token
                List<UserEntity> userEntityList = dataBase.userDao().getAll();

                //Call rest interface
                Call<ServerDataResponse> serverDataResponseCall = restInterface.signUpSesion(
                        sessionEntityList.get(i).getUuid(), sessionEntityList.get(i).getUserId(),
                        sessionEntityList.get(i).getSessionStart(), sessionEntityList.get(i).getSessionEnd(),
                        sessionEntityList.get(i).getTimeElapssed(), userEntityList.get(0).getToken());

                serverDataResponseCall.enqueue(new Callback<ServerDataResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<ServerDataResponse> call, Response<ServerDataResponse> response) {
                        ServerDataResponse serverDataResponse = response.body();
                        String type = serverDataResponse.getType();
                        String rval = serverDataResponse.getRval();
                        String upload = serverDataResponse.getUpload();

                        Log.i("JOB SESION", "En trabajo" + upload);

                        if (upload.equals("fail")) {  //Comprobar el tipo de fallo
                            if (!rval.equals("existing_session"))
                                successSignSesion = false;
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ServerDataResponse> call, Throwable t) {
                        successSignSesion = false;
                        call.cancel();
                        t.printStackTrace();
                    }
                });

                if (successSignSesion)
                    dataBase.sessionDao().deleteSession(sessionEntityList.get(i));

            }

        if (successSignSesion)
            return Result.SUCCESS;
        else {
            return Result.RESCHEDULE;
        }
    }

    @Override
    protected void onReschedule(int newJobId) {
        // the rescheduled job has a new ID
        Log.i("JOB SESION", "El trabajo falló");
    }

    public static void scheduleJob() {
        Log.i("JOB SESION", "Lanzando trabajo");
        new JobRequest.Builder(SyncJobSesion.TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setBackoffCriteria(1000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setExecutionWindow(30_000L, 40_000L)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
