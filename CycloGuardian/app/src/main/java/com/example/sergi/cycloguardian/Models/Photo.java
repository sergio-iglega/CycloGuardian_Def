package com.example.sergi.cycloguardian.Models;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Clase que representa una foto
 * Created by sergi on 20/03/2018.
 */

public class Photo {
    String url = null;
    String namePhoto = null;
    String rutaInterna = null;
    UUID incidenceUUID;
    UUID uuidPhoto;


    public UUID getUuidPhoto() {
        return uuidPhoto;
    }

    public void setUuidPhoto(UUID uuidPhoto) {
        this.uuidPhoto = uuidPhoto;
    }

    public UUID getIncidenceUUID() {
        return incidenceUUID;
    }

    public void setIncidenceUUID(UUID incidenceUUID) {
        this.incidenceUUID = incidenceUUID;
    }

    public String getRutaInterna() {
        return rutaInterna;
    }

    public void setRutaInterna(String rutaInterna) {
        this.rutaInterna = rutaInterna;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNamePhoto() {
        return namePhoto;
    }

    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }

    public Photo (String url, String namePhoto) {
        this.url = url;
        this.namePhoto = namePhoto;
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static File getPhotoFile(String namePhoto) {
        File file = null;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CycloGuardian");
        file = new File (myDir, namePhoto);

        return file;
    }

    public static String obtainPath(String namePhoto) {
        String ruta = null;
        File file;
        file = Photo.getPhotoFile(namePhoto);
        ruta = file.getAbsolutePath();
        return ruta;
    }

    public static String generateFileHash(String namePhoto) throws NoSuchAlgorithmException {
        String checksum = null;
        File file = null;

        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        //Obtain the file of the photo
        file = Photo.getPhotoFile(namePhoto);

        //Get the checksum
        try {
            checksum = Photo.getFileChecksum(md5Digest, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return checksum;
    }
}
