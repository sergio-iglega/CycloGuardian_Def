package com.example.sergi.cycloguardian.Events;

/**
 * Created by sergi on 29/05/2018.
 */

public class BluetoothMessage {

    int sensor1;
    int sensor2;
    String rawMessage;

    public BluetoothMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        String[] sensors = rawMessage.split(",");

        if(sensors.length == 2){
            setSensor1(Integer.parseInt(sensors[0]));
            setSensor2(Integer.parseInt(sensors[1]));
        }
    }

    public int getSensor1() {
        return sensor1;
    }

    public void setSensor1(int sensor1) {
        this.sensor1 = sensor1;
    }

    public int getSensor2() {
        return sensor2;
    }

    public void setSensor2(int sensor2) {
        this.sensor2 = sensor2;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
}
