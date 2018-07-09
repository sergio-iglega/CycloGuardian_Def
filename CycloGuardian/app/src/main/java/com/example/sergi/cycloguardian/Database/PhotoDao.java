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

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photos")
    List<PhotoEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPhtoos(PhotoEntity... photoEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(PhotoEntity photoEntity);

    @Delete
    void deletePhoto(PhotoEntity photoEntity);

    @Query("DELETE FROM photos")
    void deleteAllPhotos();

    @Query("SELECT * FROM photos WHERE uuidIncidence LIKE :uuidIncidencia")
    PhotoEntity selectPhotoOfIncidence(String uuidIncidencia);

    @Update
    void updatePhoto(PhotoEntity photoEntity);
}
