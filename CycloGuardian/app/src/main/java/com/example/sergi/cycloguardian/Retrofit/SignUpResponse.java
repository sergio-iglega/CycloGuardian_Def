package com.example.sergi.cycloguardian.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Pojo para obtener la respuesta del servidor ante una petición de registro
 * Created by sergi on 21/05/2018.
 */

public class SignUpResponse {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("registry")
    @Expose
    private String registry;
    @SerializedName("rval")
    @Expose
    private String rval;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getRval() {
        return rval;
    }

    public void setRval(String rval) {
        this.rval = rval;
    }
}
