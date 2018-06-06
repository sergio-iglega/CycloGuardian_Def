package com.example.sergi.cycloguardian.Intro;

import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.sergi.cycloguardian.Activities.LoginActivity;
import com.example.sergi.cycloguardian.R;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });


        addSlide(new SlideFragmentBuilder()
                .title((String) getText(R.string.title_slide_1))
                .description((String) getText(R.string.description_slide_1))
                .image(R.drawable.bicycle)
                .backgroundColor(R.color.color_material_slide1)
                .build());

    }

    @Override
    public void onFinish() {
        super.onFinish();
        /*Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/
    }
}
