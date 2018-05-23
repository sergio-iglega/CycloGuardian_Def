package com.example.sergi.cycloguardian.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sergi.cycloguardian.Events.LocationEvent;
import com.example.sergi.cycloguardian.Events.ThersholdEvent;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

import de.greenrobot.event.EventBus;


public class FragmentMap extends Fragment implements OnMapReadyCallback{

    GoogleMap mGoogleMap = null;
    MarkerOptions options;
    MapView mMapView;
    SupportMapFragment mapFragment;
    View mView;
    ArrayList<LatLng> myLocations = null;
    MyApplication myApplication;
    int posIncidence;

    public FragmentMap() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this); //Registro al bus de evnetos
        myApplication = ((MyApplication)getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }


        MapsInitializer.initialize(getContext());
        mapFragment.getMapAsync(this);

        return mView;
    }


    // This method will be called when a ThersholEvent is posted
    public void onEvent(ThersholdEvent event){
        // Implementation when somo event was recive
        Log.i("MAP", String.valueOf(myApplication.mySession.getIncidenceArryList().get(event.getPosIncidence()).getPosicion()));
        posIncidence = event.getPosIncidence();
        addMarkerToMap(myApplication.mySession.getIncidenceArryList().get(event.getPosIncidence()).getPosicion(),
                myApplication.mySession.getIncidenceArryList().get(event.getPosIncidence()).getImage().getNamePhoto());
    }


   public void setupMapIfNeeded(GoogleMap googleMap) {
       if (mGoogleMap == null) {
          mGoogleMap = googleMap;
       }
   }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        float minX = 0.0f;  //Para calcular aleatoriamente el color del marcador
        float maxX = 360.0f;
        float finalX;
        Random rand = new Random();
        finalX = rand.nextFloat() * (maxX - minX) + minX;
        options = new MarkerOptions();


        setupMapIfNeeded(googleMap);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (myApplication.mySession.getIncidenceArryList().isEmpty()) {
            CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(40.968725, -5.663223))
                    .zoom(8).bearing(0).tilt(45).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            for (int i = 0; i < myApplication.mySession.getIncidenceArryList().size(); i++) {
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(myApplication.mySession.getIncidenceArryList().get(i).getPosicion())
                        .anchor(0.5f, 0.5f)
                        .title(myApplication.mySession.getIncidenceArryList().get(i).getImage().getNamePhoto())
                        .icon(BitmapDescriptorFactory.defaultMarker(finalX)));

                CameraPosition cameraPosition = CameraPosition.builder().target(myApplication.mySession.getIncidenceArryList().get(i).getPosicion())
                        .zoom(16).bearing(0).tilt(45).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }





    }

    public void addMarkerToMap(LatLng positionIncidence, String namePhoto) {

        float minX = 0.0f;  //Para calcular aleatoriamente el color del marcador
        float maxX = 360.0f;
        float finalX;
        Random rand = new Random();
        finalX = rand.nextFloat() * (maxX - minX) + minX;

        mGoogleMap.addMarker(new MarkerOptions()
                .position(positionIncidence)
                .anchor(0.5f, 0.5f)
                .title(namePhoto)
                .icon(BitmapDescriptorFactory.defaultMarker(finalX)));

        CameraPosition cameraPosition = CameraPosition.builder().target(positionIncidence)
                .zoom(16).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }



}
