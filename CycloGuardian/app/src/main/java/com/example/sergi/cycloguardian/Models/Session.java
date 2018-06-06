package com.example.sergi.cycloguardian.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by sergi on 23/04/2018.
 */

public class Session {

    ArrayList<Incidence> incidenceArryList;
    Date sessionStart;
    Date sessionEnd;
    long timeElapsedSession;
    Queue<Float> sensorDatesQueue;
    Queue<Float> sensorDatesQueue2;
    UUID sessionUUID;
    Integer userID;
    float limitOvertaking;


    public Session() {
        this.incidenceArryList = new ArrayList<>();
        this.sensorDatesQueue = new LinkedList<>();
        this.sensorDatesQueue2 = new LinkedList<>();
        this.limitOvertaking = 1.5f;
    }

    //Singleton of the class
    private static Session instance;
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }

        return instance;
    }

    //Getters and setters

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public UUID getSessionUUID() {
        return sessionUUID;
    }

    public void setSessionUUID(UUID sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    public ArrayList<Incidence> getIncidenceArryList() {
        return incidenceArryList;
    }

    public void setIncidenceArryList(ArrayList<Incidence> incidenceArryList) {
        this.incidenceArryList = incidenceArryList;
    }

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(Date sessionStart) {
        this.sessionStart = sessionStart;
    }

    public Date getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(Date sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public long getTimeElapsedSession() {
        return timeElapsedSession;
    }

    public void setTimeElapsedSession(long timeElapsedSession) {
        this.timeElapsedSession = timeElapsedSession;
    }

    public Queue<Float> getSensorDatesQueue() {
        return sensorDatesQueue;
    }

    public void setSensorDatesQueue(Queue<Float> sensorDatesQueue) {
        this.sensorDatesQueue = sensorDatesQueue;
    }

    public Queue<Float> getSensorDatesQueue2() {
        return sensorDatesQueue2;
    }

    public void setSensorDatesQueue2(Queue<Float> sensorDatesQueue2) {
        this.sensorDatesQueue2 = sensorDatesQueue2;
    }

    public float getLimitOvertaking() {
        return limitOvertaking;
    }

    public void setLimitOvertaking(float limitOvertaking) {
        this.limitOvertaking = limitOvertaking;
    }

}
