package com.example.sergi.cycloguardian.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sergi on 15/05/2018.
 */

public interface RestInterface {
    @GET("/serviceLogin/{email}/{password}")
    Call<LoginResponse> loginUser(@Path("email") String email, @Path("password") String password);

    @GET("/serviceRegistro/{name}/{lastname}/{phone}/{email}/{password}")
    Call<SignUpResponse> signUpUser(@Path("name") String name, @Path("lastname") String lastname,
                                    @Path("phone") String phone, @Path("email") String email,
                                    @Path("password") String password);

    @Multipart
    @POST("/api/uploadPhoto")
    Call<UploadPhotoResponse> uploadPhoto(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("/registrarSesion/{uuidSesion}/{idUser}/{sesionStart}/{sesionEnd}/{timeElapsed}")
    Call<ServerDataResponse> signUpSession(@Path("uuidSesion") String uuidSesion, @Path("idUser") String idUser,
                                           @Path("sesionStart") String sesionStart, @Path("sesionEnd") String sesionEnd,
                                           @Path("timeElapsed") long timeElapsed);

    @GET("/registrarIncidencia/{uuidIncidencia}/{uuidSesion}/{latitud}/{longitud}/{timeIncidence}/{distanceSensor}")
    Call<ServerDataResponse> signUpIncidence(@Path("uuidIncidencia") String uuidIncidencia, @Path("uuidSesion") String uuidSesion,
                                             @Path("latitud") double latitud, @Path("longitud") double longitud,
                                             @Path("timeIncidence") String timeIncidence, @Path("distanceSensor") float distanceSensor);


    @GET("/registrarFoto/{uuidPhoto}/{uuidIncidencia}/{namePhoto}/{rutaAndroid}")
    Call<ServerDataResponse> signUpPhoto(@Path("uuidPhoto") String uuidPhoto, @Path("uuidIncidencia") String uuidIncidencia,
                                         @Path("namePhoto") String namePhoto, @Path("rutaAndroid") String rutaAndroid);
}
