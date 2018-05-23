package com.example.sergi.cycloguardian.Events;

import java.util.Queue;

/**
 * Created by sergi on 06/04/2018.
 */

public class SensorEvent {
    float sensor1;
    float sensor2;

    public Queue<Float> getDates() {
        return dates;
    }

    public void setDates(Queue<Float> dates) {
        this.dates = dates;
    }

    Queue<Float> dates;

    public SensorEvent() {

    }

    public SensorEvent(float s1, float s2) {
        this.sensor1 = s1;
        this.sensor2 = s2;
    }

    public float getSensor1() {
        return sensor1;
    }

    public void setSensor1(float sensor1) {
        this.sensor1 = sensor1;
    }

    public float getSensor2() {
        return sensor2;
    }

    public void setSensor2(float sensor2) {
        this.sensor2 = sensor2;
    }

}
