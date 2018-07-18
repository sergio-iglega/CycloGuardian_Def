package com.example.sergi.cycloguardian.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.example.sergi.cycloguardian.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Activity que muestra información de la aplicación
 * @author sergi
 */
public class InfoActivity extends MaterialAboutActivity {

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        //Add items to card
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(R.string.title_about)
                .desc(R.string.desc_about)
                .icon(R.mipmap.ic_launcher)
                .build());

        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(context,
                new IconicsDrawable(context)
                    .icon(GoogleMaterial.Icon.gmd_info)
                    .sizeDp(18), "Version", false));

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.license)
                .icon(new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_bookmark)
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(context, AboutActivity.class);
                        context.startActivity(intent);
                    }
                })
                .build());
        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title(R.string.author);

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.author_name)
                .subText(R.string.country)
                .icon(new IconicsDrawable(context)
                    .icon(GoogleMaterial.Icon.gmd_person)
                    .sizeDp(18))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.github)
                .icon(new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_account_circle)
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse(String.valueOf(R.string.user_github_url))))
                .build());


        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();
        convenienceCardBuilder.title(R.string.contact);

        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(context,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_web)
                        .sizeDp(18),
                "Visit Website",
                true,
                Uri.parse("http://www.cycloguardian.com")));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(context,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_star)
                        .sizeDp(18),
                "Rate this app",
                null
        ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(context,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_email)
                        .sizeDp(18),
                "Send an email",
                true,
                "cycloguardian@gmail.com",
                "Question concerning MaterialAboutLibrary"));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(context,
                new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_phone)
                        .sizeDp(18),
                "Call me",
                true,
                "+34 690 87 38 69"));


        return new MaterialAboutList(appCardBuilder.build(),authorCardBuilder.build(), convenienceCardBuilder.build());
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
