package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 14/03/2018.
 */

public class OutcomingCameraMessageTakePhoto extends OutcomingCameraMessage {
    public String param = null;
    public int offset;
    public int fetch_size;

    public  OutcomingCameraMessageTakePhoto(int msg_id, String param, int token, int fetch_size) {
        this.param = param;
        this.msg_id = msg_id;
        this.token = token;
        this.offset = 0;
        this.fetch_size = fetch_size;
    }


    @Override
    public String componerMensajePhoto() {
        String cadena = "{\"msg_id\":" + msg_id + ",\"token\":" + token + ",\"param\":" + param + "}";
        return cadena;
    }
}
