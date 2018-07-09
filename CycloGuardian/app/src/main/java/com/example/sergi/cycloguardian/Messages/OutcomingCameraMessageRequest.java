package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageRequest extends OutcomingCameraMessage {

    /**
     * Constructor con argumentos
     * @param msg_id
     */
    public OutcomingCameraMessageRequest(int msg_id) {
        this.msg_id = msg_id;
        this.token = 0;
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
