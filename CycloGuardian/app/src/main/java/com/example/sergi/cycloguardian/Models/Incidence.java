package com.example.sergi.cycloguardian.Models;

import android.content.Intent;

import com.example.sergi.cycloguardian.Files.Photo;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sergi on 13/04/2018.
 */

public class Incidence {
    LatLng posicion;
    UUID incidenceUUID;
    UUID sessionUUID;
    Photo image;
    ArrayList<Double> datosSensor;
    Date timeIncidence;
    String incidenceDirection = null;

    //Constructor of the class
    public Incidence() {

    }

    //Getters and setters
    public UUID getMyUUID() {
        return incidenceUUID;
    }

    public void setMyUUID(UUID myUUID) {
        this.incidenceUUID = myUUID;
    }

    public UUID getSessionUUID() {
        return sessionUUID;
    }

    public void setSessionUUID(UUID sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    public String getIncidenceDirection() {
        return incidenceDirection;
    }

    public void setIncidenceDirection(String incidenceDirection) {
        this.incidenceDirection = incidenceDirection;
    }

    public LatLng getPosicion() {
        return posicion;
    }

    public void setPosicion(LatLng posicion) {
        this.posicion = posicion;
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public ArrayList<Double> getDatosSensor() {
        return datosSensor;
    }

    public void setDatosSensor(ArrayList<Double> datosSensor) {
        this.datosSensor = datosSensor;
    }

    public Date getTimeIncidence() {
        return timeIncidence;
    }

    public void setTimeIncidence(Date timeIncidence) {
        this.timeIncidence = timeIncidence;
    }
}
