package com.example.sergi.cycloguardian.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.PhotoEntity;
import com.example.sergi.cycloguardian.Models.Photo;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;

/**
 * Actividad que muestra el resumen de la sesión
 * @author sergi
 */
public class SummaryActivity extends AppCompatActivity {

    TextView textViewDateStart, textViewDateStop, textViewTimeElapsed, textViewIncidencesNumber,
            textViewAverageOvertaking;
    Queue<Float> summaryQueue;
    Queue<Float> summaryQueue2;
    MyApplication myApplication;
    ImageView imageView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goHome();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        long hour, minute, restHour, restMinute, seconds, restSeconds;
        String fechaIni, fechaFin;
        Float dateSum = 0.0f;
        Float distanceAverage = 0.0f;
        Float dateSum2 = 0.0f;
        myApplication = ((MyApplication)this.getApplication());

        //Instances of the xml
        textViewDateStart = (TextView) findViewById(R.id.textViewFechaIni);
        textViewDateStop = (TextView) findViewById(R.id.textViewFechaFin);
        textViewTimeElapsed = (TextView) findViewById(R.id.textViewTimeElapsed);
        textViewIncidencesNumber = (TextView) findViewById(R.id.textViewNumberIncidences);
        textViewAverageOvertaking = (TextView) findViewById(R.id.textViewAverage);
        imageView = (ImageView) findViewById(R.id.imageView);

        //SimpleDateFormat for the Date
        String pattern = "EEE, d MMM yyyy  HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        fechaIni = simpleDateFormat.format(myApplication.mySession.getSessionStart());
        fechaFin = simpleDateFormat.format(myApplication.mySession.getSessionEnd());

        //Average of overtaking in the session
        summaryQueue = myApplication.mySession.getSensorDatesQueue();
        Float dateQueue;
        int numberOfDates = 0;
        if (summaryQueue != null) {
            numberOfDates = summaryQueue.size();
            dateQueue = summaryQueue.poll();
            while (dateQueue != null) {
                dateSum = dateSum + dateQueue;
                dateQueue = summaryQueue.poll();
            }
        }

        summaryQueue2 = myApplication.mySession.getSensorDatesQueue2();
        Float dateQueue2;
        int numberOfDates2 = 0;
        if (summaryQueue2 != null) {
            numberOfDates2 = summaryQueue2.size();
            dateQueue2 = summaryQueue2.poll();
            while (dateQueue2 != null) {
                dateSum2 = dateSum2 + dateQueue2;
                dateQueue2 = summaryQueue2.poll();
            }
        }


        distanceAverage = ((dateSum / numberOfDates) + (dateSum2 / numberOfDates2)) / 2;

        //Convert miliseconds to hour:minute:seconds
        hour = myApplication.mySession.getTimeElapsedSession() / 3600000;
        restHour = myApplication.mySession.getTimeElapsedSession() % 3600000;

        minute = restHour / 60000;
        restMinute = restHour % 60000;

        seconds =  restMinute / 1000;
        restSeconds = restMinute % 1000;

        //Put date to the screen
        textViewDateStart.append( "  " + fechaIni);
        textViewDateStop.append("  " + fechaFin);
        textViewTimeElapsed.append("  " + hour + ":" + minute + ":" + seconds);
        textViewIncidencesNumber.append("  " + myApplication.mySession.getIncidenceArryList().size());
        textViewAverageOvertaking.append("  " + distanceAverage);

        if (myApplication.mySession.getIncidenceArryList().isEmpty()) {
            Glide.with(this).load(R.drawable.correct).into(imageView);
        } else {
            Glide.with(this).load(R.drawable.fail).into(imageView);
        }


    }

    /**
     * Botón para redirigir a la página principal
     * @param view
     */
    public void goToHomeButton(View view) {
        goHome();
        this.finish();
    }

    /**
     * Metodo que retorna al usuario a la página principal
     */
    public void goHome() {
        //TODO delete all photos from database
        removePhotosSession();
        //Change Activity
        Intent intentMain = new Intent(SummaryActivity.this, MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentMain);
    }

    /**
     * Método que borra todas las fotos de las incidencias de la sesión
     */
    private void removePhotosSession() {
        File photoFile;
        AppDataBase myDb = AppDataBase.getAppDataBase(this.getApplicationContext());
        List<PhotoEntity> photoEntityList = myDb.photoDao().getAll();
        for (int i = 0; i < photoEntityList.size(); i++) {
            if (photoEntityList.get(i).getSyncronized() == true) {
                photoFile = Photo.getPhotoFile(photoEntityList.get(i).getNamePhoto());
                if (photoFile.exists())
                    photoFile.delete();

                myDb.photoDao().deletePhoto(photoEntityList.get(i));
            }
        }

    }

}
