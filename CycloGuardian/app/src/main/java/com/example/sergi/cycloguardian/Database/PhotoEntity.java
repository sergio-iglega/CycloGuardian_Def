package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by sergi on 23/04/2018.
 */

@Entity(tableName = "photos")
public class PhotoEntity {
    @PrimaryKey @NonNull
    String uuidPhoto;

    String uuidIncidence;
    String namePhoto;
    String rutaPhoto;
    String rutaPhotoServer;

    public String getRutaPhotoServer() {
        return rutaPhotoServer;
    }

    public void setRutaPhotoServer(String rutaPhotoServer) {
        this.rutaPhotoServer = rutaPhotoServer;
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
