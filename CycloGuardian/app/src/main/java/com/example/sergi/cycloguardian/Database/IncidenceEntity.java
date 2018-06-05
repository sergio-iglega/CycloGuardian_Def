package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by sergi on 23/04/2018.
 */

@Entity(tableName = "incidences")
public class IncidenceEntity {
    @PrimaryKey @NonNull
    String uuidIncidence;

    String uuidSession;
    double latitude;
    double longitude;
    String timeIncidence;
    float distanceSensor;


    public String getIdSession() {
        return uuidSession;
    }

    public void setIdSession(String idSession) {
        this.uuidSession = idSession;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimeIncidence() {
        return timeIncidence;
    }

    public void setTimeIncidence(String timeIncidence) {
        this.timeIncidence = timeIncidence;
    }

    public String getUuid() {
        return uuidIncidence;
    }

    public void setUuid(String uuid) {
        this.uuidIncidence = uuid;
    }

    public float getDistanceSensor() {
        return distanceSensor;
    }

    public void setDistanceSensor(float distanceSensor) {
        this.distanceSensor = distanceSensor;
    }


}
