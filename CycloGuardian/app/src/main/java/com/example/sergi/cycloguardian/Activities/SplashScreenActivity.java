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
import com.example.sergi.cycloguardian.Database.UserEntity;
import com.example.sergi.cycloguardian.Intro.IntroActivity;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Utils.Constants;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Actividad que se encarga de cargar los componentes de la aplicación
 * y de mostrar el splashScreen
 * @author sergi
 */
public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    AppDataBase myAppDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAppDataBase = AppDataBase.getAppDataBase(this);
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

                    if (isUserInDataBase(myAppDataBase)) {
                        //User just logged
                        Intent mainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(mainActivity);

                    } else {
                        // Start the login activity --> No logged
                        if (getFirstTimeSharedPreferences()) {
                            saveFirstTimeOnSharedPreferences(false);
                            Intent intentIntro = new Intent(SplashScreenActivity.this, IntroActivity.class);
                            Intent intentLogin = new Intent(SplashScreenActivity.this, LoginActivity.class);

                            startActivities(new Intent[]{intentLogin, intentIntro});

                        } else { //Is not the first time
                            Intent loginIntent = new Intent().setClass(
                                    SplashScreenActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        }
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

    /**
     * Metodo para guardar en el shared preferences que el usuario ya ha usado
     * por primera vez la aplicación
     * @param b
     */
    private void saveFirstTimeOnSharedPreferences(boolean b) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("first_time", b);
        editor.apply();
    }

    /**
     * Compreuba si el usuario ya tiene iniciada la sesión
     * @param myAppDataBase base de datos
     * @return logeado
     */
    private boolean isUserInDataBase(AppDataBase myAppDataBase) {
        Boolean logged = false;

        List<UserEntity> myUserlist = myAppDataBase.userDao().getAll();
        Log.i("SIZE", String.valueOf(myUserlist.size()));
        if(myUserlist.size() == 1){
            logged = true;
        }

        return logged;
    }

    /**
     * Comprueba si es la primera vez que el usuario utiliza la aplicación
     * @return boolean
     */
    private Boolean getFirstTimeSharedPreferences() {
        return prefs.getBoolean("first_time", true);
    }

}
