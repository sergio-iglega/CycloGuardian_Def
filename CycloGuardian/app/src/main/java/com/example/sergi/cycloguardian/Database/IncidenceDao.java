package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sergi on 23/04/2018.
 */

@Dao
public interface IncidenceDao {
    @Query("SELECT * FROM incidences")
    List<IncidenceEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllIncidences(IncidenceEntity... incidenceEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIncidence(IncidenceEntity incidenceEntity);

    @Delete
    void deleteIncidence(IncidenceEntity incidenceEntity);

    @Query("DELETE FROM incidences")
    void deleteAllIncidences();
}
