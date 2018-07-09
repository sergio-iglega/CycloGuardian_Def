package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 27/02/2018.
 * Mensajes enviados por la camara
 */
public abstract class CameraMessage {

    /**
     * Identificador de mensaje
     */
    public int msg_id;
    /**
     * Token de comunicación
     */
    public int token;


    /**
     * Obtiene el token
     * @return el token
     */
    public int getToken() {
        return token;
    }

    /**
     * Establece el token
     * @param token
     */
    public void setToken(int token) {
        this.token = token;
    }

    /**
     * Obtiene el identificador de mensaje
     * @return el identificador de mensaje
     */
    public int getMsg_id() {
        return msg_id;
    }

    /**
     * Establece el identificador de mensaje
     * @param msg_id
     */
    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }


}
