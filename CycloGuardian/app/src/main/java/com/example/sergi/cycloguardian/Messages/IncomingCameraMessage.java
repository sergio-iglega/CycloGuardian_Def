package com.example.sergi.cycloguardian.Messages;

/**
 * Created by sergi on 28/02/2018.
 */

public abstract class IncomingCameraMessage extends CameraMessage {
    /**
     * Valor de retorno de la cámara
     */
    public int rval = 0;
    /**
     * Parámetro de retorno de la cámara
     */
    public String param = null;
    /**
     * Tipo de mensaje
     */
    public String type = null;
    /**
     * Token de comunicación
     */
    public int paramToken = 0;

    /*----------GETTERS AND SETTERS---------------------*/

    /**
     * Obtiente el rval
     * @return el rval
     */
    public int getRval() {
        return rval;
    }

    /**
     * Establece el rval
     * @param rval
     */
    public void setRval(int rval) {
        this.rval = rval;
    }

    /**
     * Obtiene el param
     * @return el param
     */
    public String getParam() {
        return param;
    }

    /**
     * Establece el param
     * @param param
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Obtiene el tipo de mensaje
     * @return el type
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de mensaje
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el token de comunicacion
     * @return el token
     */
    public int getParamToken() {
        return paramToken;
    }

    /**
     * Establece el token
     * @param paramToken
     */
    public void setParamToken(int paramToken) {
        this.paramToken = paramToken;
    }


    /**
     * Constructor sin argumentos
     */
    public IncomingCameraMessage() {
        this.rval = -1;
    }

    /**
     * Parsea en los distintos atributos la cadena enviada a través del socket
     * @param cadena
     */
    public abstract void parserMessage(String cadena);


}
