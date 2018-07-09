package com.example.sergi.cycloguardian.Job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;


import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.IncidenceEntity;
import com.example.sergi.cycloguardian.Database.PhotoEntity;
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Models.Photo;
import com.example.sergi.cycloguardian.Retrofit.APIClient;
import com.example.sergi.cycloguardian.Retrofit.RestInterface;
import com.example.sergi.cycloguardian.Retrofit.UploadPhotoResponse;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Created by sergi on 23/04/2018.
 */

public class SyncJobIncidence extends Job {

    public static final String TAG = "job_sync_tag";
    AppDataBase dataBase;
    RestInterface restInterface;
    Boolean successSignIncidence = true;

    /**
     * Trabajo de subida
     * @param params
     * @return
     */
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
                    RequestBody token = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userEntityList.get(0).getToken()));

                  /*  ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
                    if (connectivityManager == null) throw new AssertionError();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        for (Network net : connectivityManager.getAllNetworks()) {
                            NetworkInfo info = connectivityManager.getNetworkInfo(net);
                            if (info != null && info.getType() == TYPE_MOBILE) {
                                Log.i("NET", info.getExtraInfo());
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
                        }
                    }*/
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

                if(successSignIncidence) {
                    //Remove the incidence and the photo from the DB
                    dataBase.incidenceDao().deleteIncidence(incidenceEntityList.get(i));
                    photoEntity.setSyncronized(true);
                    dataBase.photoDao().updatePhoto(photoEntity);
                } else {
                    photoEntity.setSyncronized(false);
                }

            }



        if (successSignIncidence)
            return Result.SUCCESS;
        else
            return Result.RESCHEDULE;
    }


    /**
     * Metodo que se ejecuta cuándo no se ha podido completar el trabajo
     * @param newJobId
     */
    @Override
    protected void onReschedule(int newJobId) {
        // the rescheduled job has a new ID
        Log.i("JOB INCIDENCE", "No se ha realizado el trabajo");
    }

    /**
     * Programa un trabajo de subida de una incidencia
     */
    public static void scheduleJob() {
       Log.i("JOB INCIDENCE", "Lanzando trabajo");
        new JobRequest.Builder(SyncJobIncidence.TAG)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setBackoffCriteria(1000L, JobRequest.BackoffPolicy.EXPONENTIAL)
                .setExecutionWindow(30_000L, 40_000L)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

}
