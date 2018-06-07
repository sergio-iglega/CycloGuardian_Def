package com.example.sergi.cycloguardian.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sergi on 23/05/2018.
 */

public class UploadPhotoResponse {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("upload")
    @Expose
    private String upload;
    @SerializedName("rval")
    @Expose
    private String rval;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getRval() {
        return rval;
    }

    public void setRval(String rval) {
        this.rval = rval;
    }
}
