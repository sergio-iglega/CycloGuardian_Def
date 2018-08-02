package com.example.sergi.cycloguardian.Events;

/**
 * Evento utilizado para conectar el BLE
 * Created by sergi on 29/05/2018.
 */

public class ConnectBLEEvent {

    public ConnectBLEEvent(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    private String macAddress;


}
