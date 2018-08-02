package com.example.sergi.cycloguardian.Intro;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sergi.cycloguardian.Activities.LoginActivity;
import com.example.sergi.cycloguardian.R;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Actividad para la introducción de ayuda
 * @author sergi
 */
public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        addSlide(new SimpleSlide.Builder()
                .title(getText(R.string.title_slide_1))
                .description(getText(R.string.description_slide_1))
                .image(R.drawable.bicycle)
                .scrollable(false)
                .background(R.color.color_material_slide1)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(getText(R.string.title_slide_2))
                .description(getText(R.string.description_slide_2))
                .image(R.drawable.useradd)
                .background(R.color.color_slide2)
                .scrollable(false)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(getText(R.string.title_slide3))
                .description(getText(R.string.description_slide3))
                .image(R.drawable.settings)
                .background(R.color.color_slide3)
                .scrollable(false)
                .build());



        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_slide_4)
                .description(R.string.description_slide_4)
                .image(R.drawable.marker)
                .background(R.color.third_slide_background)
                .scrollable(false)
                .permissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION})
                .build());



    }


}
