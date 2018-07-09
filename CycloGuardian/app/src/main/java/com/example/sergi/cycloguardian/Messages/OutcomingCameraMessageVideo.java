package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageVideo extends OutcomingCameraMessage {

    /**
     * Constructor con argumentos
     * @param msg_id
     */
    public OutcomingCameraMessageVideo(int msg_id) {
        this.msg_id = msg_id;
    }


    /**
     * Compone el mensaje
     * @return
     */
    @Override
    public String componerMensajePhoto() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + "}";
        return cadena;
    }
}
