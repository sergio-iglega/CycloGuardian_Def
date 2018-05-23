package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 28/02/2018.
 */

public abstract class IncomingCameraMessage extends CameraMessage {
    public int rval = 0;
    public String param = null;
    public String type = null;
    public int paramToken = 0;

    public int getRval() {
        return rval;
    }

    public void setRval(int rval) {
        this.rval = rval;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParamToken() {
        return paramToken;
    }

    public void setParamToken(int paramToken) {
        this.paramToken = paramToken;
    }


    public IncomingCameraMessage() {
        this.rval = -1;
    }

    public abstract void parserMessage(String cadena);


}
