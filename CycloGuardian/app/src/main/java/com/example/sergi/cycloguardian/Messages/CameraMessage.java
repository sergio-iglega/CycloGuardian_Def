package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 27/02/2018.
 */

public abstract class CameraMessage {

    public int msg_id;
    public int token;


    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }


}
