package com.example.sergi.cycloguardian.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.Database.SessionEntity;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.text.SimpleDateFormat;
import java.util.Queue;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon.gmd_done;

public class SummaryActivity extends AppCompatActivity {

    TextView textViewDateStart, textViewDateStop, textViewTimeElapsed, textViewIncidencesNumber,
            textViewAverageOvertaking;
    Queue<Float> summaryQueue;
    Queue<Float> summaryQueue2;
    MyApplication myApplication;

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


    }

}
