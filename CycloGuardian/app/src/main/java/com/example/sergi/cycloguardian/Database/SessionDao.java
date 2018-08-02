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
 * Dao de Sesion
 */
@Dao
public interface SessionDao {

    /**
     * Query para obtener todas las sesiones de la base de datos
     * @return
     */
    @Query("SELECT * FROM sessions")
    List<SessionEntity> getAll();

    /**
     * Obtiener una sesión
     * @param uuid
     * @return
     */
    @Query("SELECT * FROM sessions WHERE uuidSession LIKE :uuid")
    SessionEntity getSessionByUUID(String uuid);

    /**
     * Inserta una sesión
     * @param session
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSession(SessionEntity session);

    /**
     * Elimina una sesión
     * @param session
     */
    @Delete
    void deleteSession(SessionEntity session);

    /**
     * Actualiza una sesión
     * @param uuidSession
     * @param sessionEnd
     * @param sessionStart
     * @param timeElapsed
     * @return
     */
    @Query("UPDATE sessions SET sessionEnd = :sessionEnd AND sessionStart = :sessionStart AND" +
            " timeElapssed = :timeElapsed WHERE uuidSession = :uuidSession")
    int updateSession(String uuidSession, String sessionEnd, String sessionStart, long timeElapsed);

    /**
     * Actualiza una sesión
     * @param sessionEntity
     */
    @Update
    void updateSession(SessionEntity sessionEntity);

    /**
     * Elimina todas las sesiones de la base de datos
     */
    @Query("DELETE FROM sessions")
    void deleteAllSessions();

}
