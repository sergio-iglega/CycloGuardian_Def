package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageVideo extends OutcomingCameraMessage {

    public OutcomingCameraMessageVideo(int msg_id) {
        this.msg_id = msg_id;
    }


    @Override
    public String componerMensajePhoto() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + "}";
        return cadena;
    }
}
