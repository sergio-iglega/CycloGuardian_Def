package com.example.sergi.cycloguardian.Fragments;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sergi.cycloguardian.Events.ThersholdEvent;
import com.example.sergi.cycloguardian.Files.Photo;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;



public class FragmentGallery extends Fragment {

    ImageView imageView;
    View mView;
    ConstraintLayout constraintLayout;
    TextView textViewTitle;
    ViewGroup cont;

    //Object myAplication
    MyApplication myApplication;

    int lastImage = -1;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public FragmentGallery() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = ((MyApplication)getActivity().getApplication());
        EventBus.getDefault().register(this); //Registro al bus de evnetos
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_galeria, container, false);
        imageView = (ImageView) mView.findViewById(R.id.imageViewPhoto);
        textViewTitle = (TextView) mView.findViewById(R.id.textViewTitle);
        constraintLayout = (ConstraintLayout) mView.findViewById(R.id.relativeGallery);

        if (lastImage != -1) {
            Glide.with(this.getActivity())
                    .load(myApplication.mySession.getIncidenceArryList().get(lastImage).getImage().getUrl())
                    .into(imageView);
        }


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    //Capture of event
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEvent(ThersholdEvent thersholdEvent) {
        //Toast.makeText(getActivity(), myApplication.mySession.getIncidenceArryList().get(thersholdEvent.getPosIncidence()).getImage().getNamePhoto(), Toast.LENGTH_SHORT).show();;
        lastImage = thersholdEvent.getPosIncidence();
        showImage(myApplication.mySession.getIncidenceArryList().get(thersholdEvent.getPosIncidence()).getImage(), thersholdEvent.getPosIncidence());
    }


    private void showImage(final Photo photo, int index) {
        this.lastImage = index;
        textViewTitle.setText(photo.getNamePhoto());
        Uri pathUri = Uri.parse(photo.getRutaInterna());
        Glide.with(this.getActivity())
                    .load(photo.getUrl())
                    .into(imageView);


    }



}
