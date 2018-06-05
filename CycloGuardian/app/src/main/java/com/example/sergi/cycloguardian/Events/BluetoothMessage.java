package com.example.sergi.cycloguardian.Events;

import android.util.Log;

/**
 * Created by sergi on 29/05/2018.
 */

public class BluetoothMessage {

    float sensor1;
    float sensor2;
    String rawMessage;

    public BluetoothMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        String[] sensors = rawMessage.split(",");

        if(sensors.length == 2){
            setSensor1(((float) Integer.parseInt(sensors[0])) / 100);
            setSensor2(((float) Integer.parseInt(sensors[1])) / 100);
        }

        Log.i("MSG", String.valueOf(getSensor1()));
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

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
}
