package com.example.sergi.cycloguardian.Job;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;


import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sergi.cycloguardian.Activities.StartActivity;
import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.IncidenceEntity;
import com.example.sergi.cycloguardian.Database.PhotoEntity;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Files.Photo;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Retrofit.APIClient;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Retrofit.UploadPhotoResponse;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sergi on 23/04/2018.
 */

public class SyncJobIncidence extends Job {

    public static final String TAG = "job_sync_tag";
    AppDataBase dataBase;
    RestInterface restInterface;
    Boolean successSignIncidence = true;

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        Log.i("JOB INCIDENCE", "Realizando trabajo");

        dataBase = AppDataBase.getAppDataBase(getContext());
        PhotoEntity photoEntity;
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

            //Obtain all the incidences from the DB
            List<IncidenceEntity> incidenceEntityList = dataBase.incidenceDao().getAll();
            for (int i = 0; i < incidenceEntityList.size(); i++) {
                photoEntity = dataBase.photoDao().selectPhotoOfIncidence(incidenceEntityList.get(i).getUuid());


                //Obtain the file of the foto
                File file = Photo.getPhotoFile(photoEntity.getNamePhoto()); //TODO comprobar que exista sino no hacer trabajo
                if (file.exists()) {
                    //Create the multipart
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), reqFile);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_incidence");
                    RequestBody uuidIncidencia = RequestBody.create(MediaType.parse("text/plain"), incidenceEntityList.get(i).getUuid());
                    RequestBody uuidSesion = RequestBody.create(MediaType.parse("text/plain"), incidenceEntityList.get(i).getIdSession());
                    double latitud = incidenceEntityList.get(i).getLatitude();
                    double longitud = incidenceEntityList.get(i).getLongitude();
                    RequestBody timeIncidence = RequestBody.create(MediaType.parse("text/plain"), incidenceEntityList.get(i).getTimeIncidence());
                    float distanceSensor = incidenceEntityList.get(i).getDistanceSensor();
                    RequestBody uuidPhoto = RequestBody.create(MediaType.parse("text/plain"), photoEntity.getUuidPhoto());
                    RequestBody namePhoto = RequestBody.create(MediaType.parse("text/plain"), photoEntity.getNamePhoto());
                    RequestBody rutaAndroid = RequestBody.create(MediaType.parse("text/plain"), photoEntity.getNamePhoto());

                    //Obtain the token of the user
                    List<UserEntity> userEntityList = dataBase.userDao().getAll();
                    RequestBody token = RequestBody.create(MediaType.parse("text"), String.valueOf(userEntityList.get(0)));

                    //Call retrofit service
                    Call<UploadPhotoResponse> uploadPhotoResponseCall = restInterface.uploadPhoto(body, name, uuidIncidencia,
                            uuidSesion, latitud, longitud, timeIncidence, distanceSensor, uuidPhoto, namePhoto, rutaAndroid, token);
                    uploadPhotoResponseCall.enqueue(new Callback<UploadPhotoResponse>() {
                        @Override
                        public void onResponse(Call<UploadPhotoResponse> call, Response<UploadPhotoResponse> response) {
                            UploadPhotoResponse uploadPhotoResponse = response.body();
                            String type = uploadPhotoResponse.getType();
                            String rval = uploadPhotoResponse.getRval();
                            String upload = uploadPhotoResponse.getUpload();

                            Log.i("JOB INCIDENCE", "En trabajo" + upload + rval);

                            if (upload.equals("fail")) {  //Comprobar el tipo de error
                                if (!rval.equals("existing_incidence")) {
                                    successSignIncidence = false;
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<UploadPhotoResponse> call, Throwable t) {
                            successSignIncidence = false;
                            call.cancel();
                            t.printStackTrace();
                        }
                    });
                }

                if(successSignIncidence)
                    //Remove the incidence and the photo from the DB
                    dataBase.incidenceDao().deleteIncidence(incidenceEntityList.get(i));
                    dataBase.photoDao().deletePhoto(photoEntity);

            }



        if (successSignIncidence)
            return Result.SUCCESS;
        else
            return Result.RESCHEDULE;
    }


    @Override
    protected void onReschedule(int newJobId) {
        // the rescheduled job has a new ID
        Log.i("JOB INCIDENCE", "No se ha realizado el trabajo");
    }

    public static void scheduleJob() {
       Log.i("JOB INCIDENCE", "Lanzando trabajo");
        new JobRequest.Builder(SyncJobIncidence.TAG)
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
