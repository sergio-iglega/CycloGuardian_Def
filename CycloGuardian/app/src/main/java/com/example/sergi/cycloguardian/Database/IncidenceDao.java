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

/**
 * Dao de Incidence
 */
@Dao
public interface IncidenceDao {
    /**
     * Query para obtener todas las incidencias
     * @return Listado de incidencias
     */
    @Query("SELECT * FROM incidences")
    List<IncidenceEntity> getAll();

    /**
     * Inserta una lista de incidencias en la Base de datos
     * @param incidenceEntities
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllIncidences(IncidenceEntity... incidenceEntities);

    /**
     * Inserta una incidencia en la base de datos
     * @param incidenceEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIncidence(IncidenceEntity incidenceEntity);

    /**
     * Elimina una incidencia de la base de datos
     * @param incidenceEntity
     */
    @Delete
    void deleteIncidence(IncidenceEntity incidenceEntity);

    /**
     * Elimina todas las incidencias existentes en la base de datos
     */
    @Query("DELETE FROM incidences")
    void deleteAllIncidences();
}
