package com.example.sergi.cycloguardian.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.example.sergi.cycloguardian.Database.AppDataBase;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Utils.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().hide();

        setContentView(R.layout.splash_background);

        //Get the shared preferences
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (!TextUtils.isEmpty(getUserMailSharedPreferences()) && !TextUtils.isEmpty(getUserPasswordSharedPreferences())) {
                    //User just logged
                    Intent mainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainActivity);

                } else {
                    // Start the login activity --> No logged
                    Intent loginIntent = new Intent().setClass(
                            SplashScreenActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, Constants.SPLASH_SCREEN_DELAY);
    }

    private String getUserMailSharedPreferences() {
        return prefs.getString("email", "");
    }

    private String getUserPasswordSharedPreferences() {
        return prefs.getString("password", "");
    }
}
