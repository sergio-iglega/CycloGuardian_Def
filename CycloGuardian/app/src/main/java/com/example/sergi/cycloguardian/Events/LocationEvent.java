package com.example.sergi.cycloguardian.Events;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sergi on 09/04/2018.
 */

public class LocationEvent {

    public LocationEvent(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocationEvent() {

    }


    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
        this.ubicacion = ubicacion;
    }

    LatLng ubicacion;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index;


}
