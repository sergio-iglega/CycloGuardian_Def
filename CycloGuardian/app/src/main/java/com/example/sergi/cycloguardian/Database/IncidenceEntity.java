package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by sergi on 23/04/2018.
 */

/**
 * Entidad de una incidencia
 */
@Entity(tableName = "incidences")
public class IncidenceEntity {
    /**
     * Identificador único de la incidencia
     */
    @PrimaryKey @NonNull
    String uuidIncidence;

    /**
     * Identificador de la sesión a la que pertenece la incidencia
     */
    String uuidSession;

    /**
     * Latitud de la ubicación de la incidencia
     */
    double latitude;

    /**
     * Longitud de la ubicación de la incidencia
     */
    double longitude;

    /**
     * Momento de la incidencia
     */
    String timeIncidence;

    /**
     * Distancia de adelantamiento en la incidencia
     */
    float distanceSensor;

/* -------------- GETTERS AND SETTERS  --------------------------------------------*/

    /**
     * Obtiene el identificador de la sesión
     * @return
     */
    public String getIdSession() {
        return uuidSession;
    }

    /**
     * Establece el identificador de la sesión
     * @param idSession
     */
    public void setIdSession(String idSession) {
        this.uuidSession = idSession;
    }

    /**
     * Obtiene la latitud
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Establece la latitud
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Obtiene la longitud
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Establece la longitud
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Obtiene el tiempo de la incidencia
     * @return
     */
    public String getTimeIncidence() {
        return timeIncidence;
    }

    /**
     * Establece el tiempo de la incidencia
     * @param timeIncidence
     */
    public void setTimeIncidence(String timeIncidence) {
        this.timeIncidence = timeIncidence;
    }

    /**
     * Obtiene el identificador de la incidencia
     * @return
     */
    public String getUuid() {
        return uuidIncidence;
    }

    /**
     * Establece el identificador de la incidencia
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuidIncidence = uuid;
    }

    /**
     * Obtiene la distancia de la incidencia
     * @return
     */
    public float getDistanceSensor() {
        return distanceSensor;
    }

    /**
     * Establece la distancia de la incidencia
     * @param distanceSensor
     */
    public void setDistanceSensor(float distanceSensor) {
        this.distanceSensor = distanceSensor;
    }


}
