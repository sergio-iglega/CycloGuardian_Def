package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;


/**
 * Created by sergi on 23/04/2018.
 */

@Entity(tableName = "sessions")
public class SessionEntity {
    @PrimaryKey @NonNull
    String uuidSession;

    int userId;
    String sessionStart;
    String sessionEnd;
    long timeElapssed;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuidSession;
    }

    public void setUuid(String uuid) {
        this.uuidSession = uuid;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public long getTimeElapssed() {
        return timeElapssed;
    }

    public void setTimeElapssed(long timeElapssed) {
        this.timeElapssed = timeElapssed;
    }


}
