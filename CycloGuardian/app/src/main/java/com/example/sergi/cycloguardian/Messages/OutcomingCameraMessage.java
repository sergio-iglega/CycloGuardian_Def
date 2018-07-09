package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 28/02/2018.
 */

public abstract class OutcomingCameraMessage extends CameraMessage {

    /**
     * Compone un mensaje a partir de los atributos para enviarlo
     * a través del socket
     * @return el mensaje
     */
    public abstract String componerMensajePhoto();


}
