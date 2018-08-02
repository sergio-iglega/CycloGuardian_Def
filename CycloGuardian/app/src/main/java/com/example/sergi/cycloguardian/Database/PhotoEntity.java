package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Entidad fotos
 * Created by sergi on 23/04/2018.
 */

@Entity(tableName = "photos")
public class PhotoEntity {
    /**
     * Identificador único de una foto
     */
    @PrimaryKey @NonNull
    String uuidPhoto;

    /**
     * Identificador de la incidencia a la que pertenece la foto
     */
    String uuidIncidence;

    /**
     * Nombre de la foto
     */
    String namePhoto;

    /**
     * Ruta de la foto en el dispositivo
     */
    String rutaPhoto;

    /**
     * Ruta de la foto en el servidor
     */
    String rutaPhotoServer;

    /**
     * Indica si la foto se ha reportado al servidor
     */
    Boolean syncronized;



    public String getRutaPhotoServer() {
        return rutaPhotoServer;
    }

    public void setRutaPhotoServer(String rutaPhotoServer) {
        this.rutaPhotoServer = rutaPhotoServer;
    }

    public Boolean getSyncronized() {
        return syncronized;
    }

    public void setSyncronized(Boolean syncronized) {
        this.syncronized = syncronized;
    }


    public String getUuidIncidence() {
        return uuidIncidence;
    }

    public void setUuidIncidence(String uuidIncidence) {
        this.uuidIncidence = uuidIncidence;
    }

    public String getUuidPhoto() {
        return uuidPhoto;
    }

    public void setUuidPhoto(String uuidPhoto) {
        this.uuidPhoto = uuidPhoto;
    }

    public String getRutaPhoto() {
        return rutaPhoto;
    }

    public void setRutaPhoto(String rutaPhoto) {
        this.rutaPhoto = rutaPhoto;
    }

    public String getNamePhoto() {
        return namePhoto;
    }

    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }


}
