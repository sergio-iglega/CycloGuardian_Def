package com.example.sergi.cycloguardian.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Pojo para obtener las respuestas del servidor ante una petición de login
 * Created by sergi on 21/05/2018.
 */

public class LoginResponse {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("acceso")
    @Expose
    private String acceso;
    @SerializedName("rval")
    @Expose
    private String rval;
    @SerializedName("idUser")
    @Expose
    private Integer idUser;
    @SerializedName("token")
    @Expose
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getRval() {
        return rval;
    }

    public void setRval(String rval) {
        this.rval = rval;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
