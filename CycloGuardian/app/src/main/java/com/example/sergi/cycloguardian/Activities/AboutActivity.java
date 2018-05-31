package com.example.sergi.cycloguardian.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sergi.cycloguardian.R;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

public class AboutActivity extends LibsActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LibsBuilder builder = new LibsBuilder()
                .withActivityTitle((String) getText(R.string.license))
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .withAutoDetect(true)
                .withVersionShown(true)
                .withLicenseShown(true);

        setIntent(builder.intent(this));
        super.onCreate(savedInstanceState);
    }
}
