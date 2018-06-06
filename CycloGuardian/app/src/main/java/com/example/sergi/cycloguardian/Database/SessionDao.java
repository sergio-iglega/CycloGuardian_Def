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
public interface SessionDao {

    @Query("SELECT * FROM sessions")
    List<SessionEntity> getAll();

    @Query("SELECT * FROM sessions WHERE uuidSession LIKE :uuid")
    SessionEntity getSessionByUUID(String uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSession(SessionEntity session);

    @Delete
    void deleteSession(SessionEntity session);

    @Query("UPDATE sessions SET sessionEnd = :sessionEnd AND sessionStart = :sessionStart AND" +
            " timeElapssed = :timeElapsed WHERE uuidSession = :uuidSession")
    int updateSession(String uuidSession, String sessionEnd, String sessionStart, long timeElapsed);

    @Update
    void updateSession(SessionEntity sessionEntity);

    @Query("DELETE FROM sessions")
    void deleteAllSessions();

}
