package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by sergi on 23/04/2018.
 */

/**
 * DAO de foto
 */
@Dao
public interface PhotoDao {
    /**
     * Obtiene todas las fotos de la Base de Datos
     * @return Listado de fotos
     */
    @Query("SELECT * FROM photos")
    List<PhotoEntity> getAll();

    /**
     * Inserta un listado de fotos en la base de datos
     * @param photoEntities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPhtoos(PhotoEntity... photoEntities);

    /**
     * Inserta una foto en la base de datos
     * @param photoEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(PhotoEntity photoEntity);

    /**
     * Elimina una foto de la base de datos
     * @param photoEntity
     */
    @Delete
    void deletePhoto(PhotoEntity photoEntity);

    /**
     * Elimina todas las fotos de la base de datos
     */
    @Query("DELETE FROM photos")
    void deleteAllPhotos();

    /**
     * Obtiene una foto
     * @param uuidIncidencia
     * @return
     */
    @Query("SELECT * FROM photos WHERE uuidIncidence LIKE :uuidIncidencia")
    PhotoEntity selectPhotoOfIncidence(String uuidIncidencia);

    /**
     * Actualiza una foto
     * @param photoEntity
     */
    @Update
    void updatePhoto(PhotoEntity photoEntity);
}
