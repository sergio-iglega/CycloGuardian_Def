package com.example.sergi.cycloguardian.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sergi.cycloguardian.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * Actividad encargada de mostrar las librerias usadas en la aplicación
 * @author sergi
 */
public class AboutActivity extends LibsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LibsBuilder builder = new LibsBuilder()
                .withActivityTitle((String) getText(R.string.license))
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAutoDetect(true)
                .withVersionShown(true)
                .withLicenseShown(true);

        setIntent(builder.intent(this));
        super.onCreate(savedInstanceState);
    }
}
